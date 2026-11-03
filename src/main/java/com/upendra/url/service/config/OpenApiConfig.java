package com.upendra.url.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3.0 Configuration for Distributed URL Shortener Service.
 *
 * Defines API metadata, server environments, and JWT Bearer security scheme
 * for Swagger UI interactive documentation.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Distributed URL Shortener Service API",
                version = "v1.0.0",
                description = """
                        High-scale, production-ready Distributed URL Shortener Service built using
                        Spring Boot 3, Apache ZooKeeper, Redis, and PostgreSQL.

                        ### Key Capabilities:
                        * **Distributed ID Generation**: Range-based unique ID allocation using Apache ZooKeeper & Curator (Zero Collision).
                        * **Base62 URL Encoding**: Compact, URL-safe alphanumeric short codes (`[0-9a-zA-Z]`).
                        * **High-Speed Redirection**: Redis in-memory caching for microsecond-latency redirection (`GET /{code}`).
                        * **Cache Management**: Automatic Redis cache eviction upon URL updates and deletions.
                        * **Security & Auth**: Stateless JWT authentication with Bearer tokens, RBAC, and token revocation on logout.
                        * **URL Analytics & Management**: Track clicks, expiration timestamps, and user URL ownership.

                        ### Available Endpoint Groups:
                        * **Public Endpoints**: Redirection (`GET /{code}`), Health check (`GET /health`).
                        * **Authentication Endpoints**: Register (`POST /v1/auth/register`), Login (`POST /v1/auth/login`), Logout (`POST /v1/auth/logout`).
                        * **Short URL Management**: CRUD operations on short URLs with pagination (`/v1/short-urls`).
                        """,
                contact = @Contact(
                        name = "URL Shortener Development Team",
                        email = "info@test.org",
                        url = "https://github.com/upendrarai"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development Server",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = """
                JWT Bearer Token Authentication.
                
                **Instructions:**
                1. Authenticate via `POST /v1/auth/login` using your credentials.
                2. Copy the `accessToken` from the JSON response.
                3. Click **Authorize** (top right) in Swagger UI.
                4. Paste the token into the Value field (Swagger automatically adds `Bearer ` prefix).
                5. Execute secured API endpoints.
                """,
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
