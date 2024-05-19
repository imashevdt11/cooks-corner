package kg.neobis.cookscorner.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import kg.neobis.cookscorner.config.JwtUtil;
import kg.neobis.cookscorner.dto.LogInRequest;
import kg.neobis.cookscorner.dto.LogInResponse;
import kg.neobis.cookscorner.dto.SignUpRequest;
import kg.neobis.cookscorner.entity.User;
import kg.neobis.cookscorner.exception.InvalidRequestException;
import kg.neobis.cookscorner.exception.ResourceAlreadyExistsException;
import kg.neobis.cookscorner.exception.ResourceNotFoundException;
import kg.neobis.cookscorner.repository.UserRepository;
import kg.neobis.cookscorner.service.AuthenticationService;
import kg.neobis.cookscorner.service.BlackListTokenService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    AuthenticationManager authenticationManager;
    BlackListTokenService blackListTokenService;
    JwtUtil jwtUtil;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Override
    public LogInResponse logIn(LogInRequest request) {

        if (request.getEmail() == null || request.getPassword() == null || request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
            throw new InvalidRequestException("email and password are required", HttpStatus.BAD_REQUEST.value());
        }

        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("invalid email or password", HttpStatus.NOT_FOUND.value()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("invalid email or password", HttpStatus.NOT_FOUND.value());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtUtil.generateToken(user);
        return LogInResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public ResponseEntity<?> logOut(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            blackListTokenService.addTokenToBlacklist(jwtToken);
            return ResponseEntity.ok("successful log out!");
        }
        return ResponseEntity.badRequest().body("invalid authorization header");
    }

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