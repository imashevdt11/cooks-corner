package kg.neobis.cookscorner.service.impl;

import kg.neobis.cookscorner.dto.SignUpRequest;
import kg.neobis.cookscorner.entity.User;
import kg.neobis.cookscorner.exception.InvalidRequestException;
import kg.neobis.cookscorner.exception.ResourceAlreadyExistsException;
import kg.neobis.cookscorner.repository.UserRepository;
import kg.neobis.cookscorner.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Override
    public ResponseEntity<?> signUp(SignUpRequest request) {

        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("username is occupied", HttpStatus.CONFLICT.value());
        } else if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("email is occupied", HttpStatus.CONFLICT.value());
        }

        if (!request.getPassword().equals(request.getConfirm_password())) {
            throw new InvalidRequestException("passwords don't match", HttpStatus.BAD_REQUEST.value());
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("successful registration!");
    }
}