package kg.neobis.cookscorner.service;

import kg.neobis.cookscorner.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<?> signUp(SignUpRequest request);
}