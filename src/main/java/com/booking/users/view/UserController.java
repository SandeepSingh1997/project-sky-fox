package com.booking.users.view;

import com.booking.config.featureTogglz.FeatureAssociation;
import com.booking.config.featureTogglz.FeatureOptions;
import com.booking.exceptions.UserIdDoesNotMatchesWithRequestedUserId;
import com.booking.users.UserPrincipalService;
import com.booking.users.repository.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        User user = userPrincipalService.findUserByUsername(username);
        userDetails.put("id", user.getId());
        userDetails.put("username", user.getUsername());
        userDetails.put("role", user.getRole().getName());
        return userDetails;
    }

    @FeatureAssociation(value = FeatureOptions.CHANGE_PASSWORD_FOR_ADMIN_FEATURE)
    @PutMapping("/password")
    public ResponseEntity<Object> changePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws Exception {
        userPrincipalService.changePassword(principal.getName(), changePasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public UserDetailsResponse getUserDetailsById(Principal principal, @PathVariable Long id) throws Exception {
        String username = principal.getName();
        User user = userPrincipalService.findUserByUsername(username);
        if (!isIdsMatches(id, user.getId()))
            throw new UserIdDoesNotMatchesWithRequestedUserId("User id does not matches with requested user id");
        return userPrincipalService.getUserDetailsById(id);
    }

    private boolean isIdsMatches(Long requestedId, Long userId) {
        return Objects.equals(requestedId, userId);
    }
}
