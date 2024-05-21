package kg.neobis.cookscorner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.dto.UserProfileDto;
import kg.neobis.cookscorner.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "User Profile")
@RequestMapping(EndpointConstants.USER_ENDPOINT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService service;

    @Operation(summary = "getting user profile information")
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        UserProfileDto userProfile = service.getUserProfile(username);
        return ResponseEntity.ok(userProfile);
    }
}