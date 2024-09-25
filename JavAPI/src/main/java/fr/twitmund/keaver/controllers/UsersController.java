package fr.twitmund.keaver.controllers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.twitmund.keaver.JavApiApplication;
import fr.twitmund.keaver.config.JwtTokenUtil;
import fr.twitmund.keaver.db.Role;
import fr.twitmund.keaver.db.entities.UserEntity;
import fr.twitmund.keaver.db.repositories.UserRepository;
import fr.twitmund.keaver.db.service.UserService;
import fr.twitmund.keaver.utils.DbError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/users")
public class UsersController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //@RequestMapping(value="api/v1/users/fetch", method = RequestMethod.GET, produces = "application/json")
    //public List<UserEntity> studentFetchAll() {
    //    return userService.userFetch();
    //}

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public JsonObject createUser(@RequestParam(value = "firstName") String firstName,
                                 @RequestParam(value = "lastName") String lastName,
                                 @RequestParam(value = "username") String username,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam(value = "password") String passwd,
                                 @RequestParam(value = "token") String token,
                                 HttpServletRequest request) throws NoSuchAlgorithmException {
        JsonObject jsonObject = new JsonObject();
        UserEntity entity = new UserEntity(firstName, lastName, username, email, passwd, request.getRemoteAddr());
        JsonObject tokenJsonObject = jwtTokenUtil.retrieveUserFromToken(token);
        //System.out.println("tokenJsonObject: " + token);
        if (tokenJsonObject == null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Invalid token");
            return jsonObject;
        }
        if (tokenJsonObject.get("status").getAsString().equals("error")) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", tokenJsonObject.get("message").getAsString());
            return jsonObject;
        }
        try {
            JsonObject tokenPayloadJsonObject = tokenJsonObject.get("payload").getAsJsonObject();


            //System.out.println(tokenPayloadJsonObject);
            String issuerUsername = tokenPayloadJsonObject.get("username").getAsString();
            UserEntity issuer = userRepository.findByUsername(issuerUsername).get();

            //System.out.println();
            if (issuer.getRole().equals(Role.ADMIN)) {
                if (userService.createUser(entity) == DbError.Success) {
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message", "User created successfully");
                    return jsonObject;
                }
                if (userService.createUser(entity) == DbError.DuplicateKey) {
                    jsonObject.addProperty("status", "error");
                    jsonObject.addProperty("message", "User already exist");
                    return jsonObject;
                }
            } else {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Unauthorized");
                return jsonObject;
            }
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Something went wrong");
            return jsonObject;
        } catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message","invalid token");
            jsonObject.addProperty("error",e.getMessage());
            return jsonObject;
        }
    }

    @RequestMapping(value = "/fetch", method = RequestMethod.POST, produces = "application/json")
    public JsonObject fetchUser(
            @RequestParam(value = "token") String token
    ) {
        JsonObject jsonObject = new JsonObject();
        JsonObject tokenJsonObject = jwtTokenUtil.retrieveUserFromToken(token);

        if (tokenJsonObject == null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Invalid token");
            return jsonObject;
        }
        if (tokenJsonObject.get("status").getAsString().equals("error")) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", tokenJsonObject.get("message").getAsString());
            return jsonObject;
        }

        try {
            JsonObject tokenPayloadJsonObject = tokenJsonObject.get("payload").getAsJsonObject();


            //System.out.println(tokenPayloadJsonObject);
            String issuerUsername = tokenPayloadJsonObject.get("username").getAsString();
            UserEntity issuer = userRepository.findByUsername(issuerUsername).get();
            List<UserEntity> users = userRepository.findAll();

            //System.out.println();
            if (issuer.getRole().equals(Role.ADMIN)) {
                //System.out.println('A');
                jsonObject.addProperty("status", "success");
                JsonArray usersJsonArray = new JsonArray();
                users.forEach(user -> {
                    JsonObject usersJsonObject = new JsonObject();
                    usersJsonObject.addProperty("id", user.getId());
                    usersJsonObject.addProperty("firstName", user.getFirstName());
                    usersJsonObject.addProperty("lastName", user.getLastName());
                    usersJsonObject.addProperty("username", user.getUsername());
                    usersJsonObject.addProperty("email", user.getEmail());
                    usersJsonObject.addProperty("role", user.getRole().name());
                    usersJsonArray.add( usersJsonObject);
                });
                //jsonObject.add("message", users);
                jsonObject.add("users",usersJsonArray);

                return jsonObject;

            } else {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Unauthorized");
                return jsonObject;
            }
        } catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message","invalid token");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/authorize", method = RequestMethod.POST, produces = "application/json")
    public JsonObject authorize(
            @RequestParam(value = "token") String token
    ) {
        try {
            JsonObject jsonObject = new JsonObject();
            if (jwtTokenUtil.validateToken(token)){
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "Token is valid");
                return jsonObject;
            } else {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Token is invalid");
                return jsonObject;
            }
        } catch (SignatureException | ExpiredJwtException e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Token is invalid");
            return jsonObject;
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
    public JsonObject fetchUser(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String passwd,
            HttpServletRequest request
    ) {
        JsonObject jsonObject = new JsonObject();
        JsonObject tokenJsonObject = jwtTokenUtil.retrieveUserFromToken(token);

        if (tokenJsonObject == null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Invalid token");
            return jsonObject;
        }
        if (tokenJsonObject.get("status").getAsString().equals("error")) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", tokenJsonObject.get("message").getAsString());
            return jsonObject;
        }

        try {
            JsonObject tokenPayloadJsonObject = tokenJsonObject.get("payload").getAsJsonObject();


            //System.out.println(tokenPayloadJsonObject);
            String issuerUsername = tokenPayloadJsonObject.get("username").getAsString();
            UserEntity issuer = userRepository.findByUsername(issuerUsername).get();

            //System.out.println();
            if (issuer.getRole().equals(Role.ADMIN)) {
                UserEntity updatedUser = new UserEntity(firstName, lastName, username, email, passwd, request.getRemoteAddr());
                if (userService.userUpdate(firstName, lastName, username, email, passwd, id) == DbError.Success) {
                    jsonObject.addProperty("status", "success");
                    jsonObject.addProperty("message", "User updated successfully");
                    return jsonObject;
                }
                if (userService.userUpdate(firstName, lastName, username, email, passwd, id) == DbError.InternalServerError) {
                    jsonObject.addProperty("status", "error");
                    jsonObject.addProperty("message", "an internal server error occured");
                    return jsonObject;
                }
            }else {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Unauthorized");
                return jsonObject;
            }
        }  catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Something went wrong, call an administator");
            return jsonObject;
        }
        jsonObject.addProperty("status", "error");
        jsonObject.addProperty("message", "Something went wrong, call an administator");
        return jsonObject;
    }
}
