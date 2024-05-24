package kg.neobis.cookscorner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.dto.UserDto;
import kg.neobis.cookscorner.dto.UserProfileDto;
import kg.neobis.cookscorner.dto.UserSearchPageDto;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchPageDto>> searchUsers(@RequestParam String username) {
        List<UserSearchPageDto> users = service.searchUsersByName(username);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUserInfo(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("username") String username,
                                                 @RequestParam("bio") String bio,
                                                 BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + result.getAllErrors());
        }
        UserDto user = service.updateUserInfo(file, username, bio);
        return ResponseEntity.ok(user);
    }

    // FOLLOW

    @PostMapping("/follow")
    public ResponseEntity<?> followChef(@RequestParam String authenticatedUsername, @RequestParam String chefName) {
        try {
            service.followChef(authenticatedUsername, chefName);
            return ResponseEntity.ok("Chef followed/unfollowed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to follow/unfollow chef: " + e.getMessage());
        }
    }

    @GetMapping("/like/status")
    public ResponseEntity<Boolean> isChefFollowedByUser(@RequestParam String authenticatedUsername, @RequestParam String chefName) {
        Boolean isFollowed = service.isChefFollowedByUser(authenticatedUsername, chefName);
        return ResponseEntity.ok(isFollowed);
    }

}