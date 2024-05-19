package kg.neobis.cookscorner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.neobis.cookscorner.common.EndpointConstants;
import kg.neobis.cookscorner.dto.SignUpRequest;
import kg.neobis.cookscorner.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication")
@RequestMapping(EndpointConstants.AUTH_ENDPOINT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService service;

    @Operation(summary = "registration")
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("validation error: " + result.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.signUp(request));
    }
}