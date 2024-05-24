package kg.neobis.cookscorner.service;

import kg.neobis.cookscorner.dto.UserDto;
import kg.neobis.cookscorner.dto.UserProfileDto;
import kg.neobis.cookscorner.dto.UserSearchPageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserProfileDto getUserProfile(String username);

    List<UserSearchPageDto> searchUsersByName(String username);

    UserDto updateUserInfo(MultipartFile file, String username, String bio) throws IOException;

    // FOLLOW

    void followChef(String authenticatedUsername, String chefName);

    boolean isChefFollowedByUser(String authenticatedUsername, String chefName);
}