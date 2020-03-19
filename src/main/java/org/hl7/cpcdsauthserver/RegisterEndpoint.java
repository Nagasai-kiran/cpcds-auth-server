package org.hl7.cpcdsauthserver;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/register")
public class RegisterEndpoint {

    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = { "application/json" })
    public ResponseEntity<String> RegisterUser(HttpServletRequest request, HttpEntity<String> entity) {
        System.out.println("RegisterEndpoint::Register: /register/user");
        System.out.println(entity.getBody());

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            User user = gson.fromJson(entity.getBody(), User.class);
            System.out.println(user.toString());

            String r = PasswordUtils.generateSalt(10);
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword(), r);
            User newUser = new User(user.getUsername(), hashedPassword, user.getPatientId(), r);

            if (App.getDB().write(newUser))
                return new ResponseEntity<String>("Success", HttpStatus.CREATED);
            else
                return new ResponseEntity<String>("ERROR", HttpStatus.BAD_REQUEST);
        } catch (JsonSyntaxException e) {
            System.out.println("RegisterEndpoint::RegisterUser:Unable to parse body");
            System.out.println(e);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String RegisterUserPage() {
        return "registerClient";
    }

    @RequestMapping(value = "/client", method = RequestMethod.POST)
    public ResponseEntity<String> RegisterClient(HttpServletRequest request) {
        System.out.println("RegisterEndpoint::Register: /register/client");

        HashMap<String, String> response = new HashMap<String, String>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String clientId = UUID.randomUUID().toString();
        String clientSecret = RandomStringUtils.randomAlphanumeric(256);
        response.put("id", clientId);
        response.put("secret", clientSecret);

        if (App.getDB().write(clientId, clientSecret))
            return new ResponseEntity<String>(gson.toJson(response), HttpStatus.CREATED);
        else
            return new ResponseEntity<String>("ERROR", HttpStatus.BAD_REQUEST);

    }

    @RequestMapping(value = "client", method = RequestMethod.GET)
    public String RegisterClientPage() {
        return "registerClient";
    }

}