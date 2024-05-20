package kg.neobis.cookscorner.service.impl;

import com.cloudinary.Cloudinary;
import jakarta.annotation.Resource;
import kg.neobis.cookscorner.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("folder", folderName);
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), options);
        return result.get("url").toString();
    }
}