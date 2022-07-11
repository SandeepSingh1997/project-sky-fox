package com.booking.users;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Users")
@RestController
public class UserController {

    private UserPrincipalService userPrincipalService;

    @Autowired
    public UserController(UserPrincipalService userPrincipalService) {
        this.userPrincipalService = userPrincipalService;
    }

    @GetMapping("/login")
    Map<String, Object> login(Principal principal) {
        String username = principal.getName();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", username);
        return userDetails;
    }

    @PutMapping("/password")
    public ResponseEntity<Object> changePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws Exception {
        userPrincipalService.changePassword(principal.getName(), changePasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
