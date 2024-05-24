package kg.neobis.cookscorner.service;

import kg.neobis.cookscorner.dto.UserProfileDto;
import kg.neobis.cookscorner.dto.UserSearchPageDto;

import java.util.List;

public interface UserService {

    UserProfileDto getUserProfile(String username);

    List<UserSearchPageDto> searchUsersByName(String username);

    // FOLLOW

    void followChef(String authenticatedUsername, String chefName);

    boolean isChefFollowedByUser(String authenticatedUsername, String chefName);
}