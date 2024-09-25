package fr.twitmund.keaver.controllers;

import com.google.gson.JsonObject;
import fr.twitmund.keaver.db.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/auth")
public class AuthController {
    @Autowired
    private final UserService userService;


    @RequestMapping(value = "/login" , method = RequestMethod.POST, produces = "application/json")
    public JsonObject loginUser(@RequestParam(value = "email") String email,
                                @RequestParam(value = "password") String passwd){
        return userService.loginUser(email, passwd);
    }


}