package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.dto.UserProfileDto;
import kg.neobis.cookscorner.dto.UserSearchPageDto;
import kg.neobis.cookscorner.entity.Following;
import kg.neobis.cookscorner.entity.User;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.mapper.UserMapper;
import kg.neobis.cookscorner.repository.FollowingRepository;
import kg.neobis.cookscorner.repository.RecipeRepository;
import kg.neobis.cookscorner.repository.UserRepository;
import kg.neobis.cookscorner.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: '" + username + "'", HttpStatus.NOT_FOUND.value()));

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

    @Override
    public List<UserSearchPageDto> searchUsersByName(String username) {
        List<User> users = userRepository.findByUsernameStartingWithIgnoreCase(username);

        if (users.isEmpty())
            throw new ResourceNotFoundException("No recipes found with name '" + username + "'", HttpStatus.NOT_FOUND.value());

        return users.stream().map(UserMapper::toUserSearchPageDto).toList();
    }

    // FOLLOW

    @Override
    public void followChef(String authenticatedUsername, String chefName) {

        User follower = findUserByUsername(authenticatedUsername);
        User chef = findUserByUsername(chefName);

        Optional<Following> followingOptional = followingRepository.findByFollowerAndChef(follower, chef);
        Following following;
        if (followingOptional.isPresent()) {
            following = followingOptional.get();
            following.setIsFollowed(!following.getIsFollowed());
        } else {
            following = Following.builder()
                    .follower(follower)
                    .chef(chef)
                    .isFollowed(true)
                    .build();
        }
        followingRepository.save(following);
    }

    @Override
    public boolean isChefFollowedByUser(String authenticatedUsername, String chefName) {

        User follower = findUserByUsername(authenticatedUsername);
        User chef = findUserByUsername(chefName);

        return followingRepository.findByFollowerAndChef(follower, chef)
                .orElseThrow(() -> new ResourceNotFoundException("Following not found for follower and chef", HttpStatus.NOT_FOUND.value()))
                .getIsFollowed();
    }

    private User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with name: '" + username + "'", HttpStatus.NOT_FOUND.value()));
    }
}