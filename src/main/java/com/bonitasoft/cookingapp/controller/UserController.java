package com.bonitasoft.cookingapp.controller;


import com.bonitasoft.cookingapp.entity.AuthRequest;
import com.bonitasoft.cookingapp.entity.User;
import com.bonitasoft.cookingapp.service.UserService;
import com.bonitasoft.cookingapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/addUser")
    public  ResponseEntity<?>  welcome(@RequestBody User user){

        User userExist = userService.findUser(user.getUsername());
        if(userExist!=null) {
            return new ResponseEntity<>(("Unable to create User, already exist !"), HttpStatus.CONFLICT);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return ResponseEntity.ok().body(userService.save(user));

        }
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
            );
        } catch (Exception e){
            throw new Exception("invalid username or password");
        }
        return jwtUtil.generateToken(authRequest.getUsername());
    }


}
