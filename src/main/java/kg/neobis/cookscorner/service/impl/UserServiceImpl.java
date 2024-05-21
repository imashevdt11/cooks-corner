package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.dto.UserProfileDto;
import kg.neobis.cookscorner.entity.User;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.repository.FollowingRepository;
import kg.neobis.cookscorner.repository.RecipeRepository;
import kg.neobis.cookscorner.repository.UserRepository;
import kg.neobis.cookscorner.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    FollowingRepository followingRepository;
    RecipeRepository recipeRepository;
    UserRepository userRepository;

    @Override
    public UserProfileDto getUserProfile(String username) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username, HttpStatus.NOT_FOUND.value()));

        UserProfileDto userProfile = new UserProfileDto();
        userProfile.setId(user.getId());
        userProfile.setName(user.getUsername());
        userProfile.setBio(user.getBio());

        if (user.getImage() != null) {
            userProfile.setImageUrl(user.getImage().getUrl());
        }

        userProfile.setRecipeCount(recipeRepository.countByUserId(user.getId()));
        userProfile.setFollowerCount(followingRepository.countByChefId(user.getId()));
        userProfile.setFollowingCount(followingRepository.countByFollowerId(user.getId()));

        return userProfile;
    }
}