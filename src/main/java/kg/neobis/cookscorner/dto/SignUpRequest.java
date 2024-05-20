package kg.neobis.cookscorner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$", message = "Email must start with letter and contain only letters and digits before @")
    String email;

    @NotBlank(message = "username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username must contain only letters and digits")
    @Size(min = 4, max = 28)
    String username;

    @NotBlank(message = "password is required")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "password must contain at least one digit, one lowercase letter, one uppercase letter, one special character (@#$%^&+=), and length between 8 and 15"
    )
    String password;

    @NotBlank(message = "confirm password")
    String confirm_password;
}