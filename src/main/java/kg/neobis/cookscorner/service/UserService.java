package kg.neobis.cookscorner.service;

import kg.neobis.cookscorner.dto.UserProfileDto;

public interface UserService {

    UserProfileDto getUserProfile(String username);
}
