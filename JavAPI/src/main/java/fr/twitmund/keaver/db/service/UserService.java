package fr.twitmund.keaver.db.service;

import com.google.gson.JsonObject;
import fr.twitmund.keaver.config.JwtTokenUtil;
import fr.twitmund.keaver.config.JwtUserDetailsService;
import fr.twitmund.keaver.db.entities.ProductEntity;
import fr.twitmund.keaver.db.entities.UserEntity;
import fr.twitmund.keaver.db.model.JwtUserDetails;
import fr.twitmund.keaver.db.repositories.ProductReposotiry;
import fr.twitmund.keaver.db.repositories.UserRepository;
import fr.twitmund.keaver.utils.DbError;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.query.Query;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    public List<UserEntity> userFetch() {
        return userRepository.findAll();
    }

    public UserEntity userFetchById(int id) {
        UserEntity user = userRepository.findById(String.valueOf(id)).get();
        return userRepository.findById(String.valueOf(id)).get();
    }

    public DbError userUpdate(String firstName , String lastName , String username, String email, String password, String id) {
        try{
            UserEntity user = userRepository.findById(id).get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return DbError.Success;
        } catch (Exception e) {
            return DbError.InternalServerError;
        }
    }

    public JsonObject delete(UserEntity userEntity) {
        JsonObject json = new JsonObject();
        try {
            userRepository.delete(userEntity);
            json.addProperty("status", "Success");
            json.addProperty("message", "User deleted successfully");
            return json;
        } catch (Exception e) {
            json.addProperty("status", "Error");
            json.addProperty("message", e.getMessage());
            return json;
        }
    }


    public DbError createUser(UserEntity userEntity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(userEntity.getEmail()));
        if (mongoTemplate.find(query, UserEntity.class).size() > 0) {
            return DbError.DuplicateKey;
        } else {
            String paswdB4Hash = userEntity.getPassword();


            userEntity.setPassword(passwordEncoder.encode(paswdB4Hash));
            userRepository.insert(userEntity);
            return DbError.Success;
        }
    }


    public void createIfNotExists(UserEntity userEntity) throws NoSuchAlgorithmException {
        if (createUser(userEntity) == DbError.Success) {
            System.out.println("Product created successfully");
        } else {
            System.out.println("Product Already exists");
        }
    }

    public JsonObject loginUser(String email, String password) {
        JsonObject jsonObject = new JsonObject();
        final UserEntity userEntity = userRepository.findByEmail(email).get();
        final String username = userEntity.getUsername();
        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(username);
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("email", email);
        body.put("firstName", userEntity.getFirstName());
        body.put("lastName", userEntity.getLastName());
        String token = jwtTokenUtil.generateToken(jwtUserDetails, body);


        try {
            if (!userRepository.findByEmail(email).isPresent()) {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Email does not exist");
                return jsonObject;
            }
            UserEntity entity1 = userRepository.findByEmail(email).get();

            if (entity1 != null) {
                String encodedPassword = entity1.getPassword();

                Boolean isPsswdRight = passwordEncoder.matches(password, encodedPassword);
                if (isPsswdRight) {
                    Optional<UserEntity> entity = userRepository.findOneByEmailAndPassword(email, encodedPassword);
                    if (entity.isPresent()) {
                        jsonObject.addProperty("status", "success");
                        jsonObject.addProperty("message", "Login successful");
                        jsonObject.addProperty("token", token);

                        return jsonObject;
                    } else {
                        jsonObject.addProperty("status", "error");
                        jsonObject.addProperty("message", "Login failed");
                        return jsonObject;
                    }
                } else {
                    jsonObject.addProperty("status", "error");
                    jsonObject.addProperty("message", "Password does not match");
                    return jsonObject;
                }
            } else {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Email does not exist");
                return jsonObject;
            }

        }catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "internal server error");
            jsonObject.addProperty("error", e.getMessage());
            return jsonObject;
        }
    }

}
