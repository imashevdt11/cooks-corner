package kg.neobis.cookscorner.service;

import jakarta.servlet.http.HttpServletRequest;
import kg.neobis.cookscorner.dto.LogInRequest;
import kg.neobis.cookscorner.dto.LogInResponse;
import kg.neobis.cookscorner.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    LogInResponse logIn(LogInRequest request);

    ResponseEntity<?> logOut(HttpServletRequest request);

    ResponseEntity<?> signUp(SignUpRequest request);
}