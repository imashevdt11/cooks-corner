package kg.neobis.cookscorner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Diyas",
                        email = "imashevdt@gmail.com"
                ),
                description = "Open API documentation for CooksCorner Project",
                title = "CooksCorner"
        )
)
public class OpenApiConfig {}