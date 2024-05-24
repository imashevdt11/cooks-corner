package kg.neobis.cookscorner.mapper;

import kg.neobis.cookscorner.dto.ImageDto;
import kg.neobis.cookscorner.dto.UserSearchPageDto;
import kg.neobis.cookscorner.entity.Image;
import kg.neobis.cookscorner.entity.User;

public class UserMapper {

    public static UserSearchPageDto toUserSearchPageDto(User user) {
        Image image = user.getImage();
        ImageDto imageDto = new ImageDto(image.getId(), image.getUrl());
        return new UserSearchPageDto(user.getId(), user.getUsername(), imageDto);
    }
}
