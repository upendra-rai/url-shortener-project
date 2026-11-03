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

/**
 * OpenAPI Configuration for DAV School Management Portal
 *
 * This configuration defines the API documentation metadata,
 * security scheme, and server environments for the School Portal.
 *
 * It enables Swagger UI with JWT authentication support.
 */

@OpenAPIDefinition(
        info = @Info(
                title = "DAV School Management Portal API",
                version = "v1.0.0",
                description = """
                        DAV School Management Portal is a production-grade backend system
                        built using Spring Boot to manage academic and administrative
                        operations of the school.
                        
                        The system provides secure REST APIs for managing various
                        school modules including:
                        
                        • User Authentication and Authorization (JWT-based)
                        • Student Management
                        • Teacher and Staff Management
                        • Class and Section Management
                        • Attendance Management
                        • Fee Management
                        • Examination and Result Management
                        • Timetable Management
                        • Library Management
                        • Holiday and Leave Management
                        • Notification and Communication System
                        • Media Upload and Document Management
                        • School Configuration and Settings
                        
                        Key Features:
                        • Secure JWT Authentication
                        • Role-Based Access Control (RBAC)
                        • RESTful API Architecture
                        • Modular and Scalable Design
                        • Swagger/OpenAPI Documentation
                        • Production-Level Logging
                        • Global Exception Handling
                        • Database Integration (MySQL/PostgreSQL)
                        • Optional Redis Caching
                        
                        This documentation provides complete details of
                        available endpoints, request/response structures,
                        and authentication requirements.
                        """,
                contact = @Contact(
                        name = "DAV School Development Team",
                        email = "support@davschool.com",
                        url = "https://www.davschool.com"
                ),
                license = @License(
                        name = "DAV School Private License",
                        url = "https://www.davschool.com/license"
                ),
                termsOfService = "https://www.davschool.com/terms"
        ),

        servers = {

                @Server(
                        description = "Local Development Environment",
                        url = "http://localhost:8082"
                ),

                @Server(
                        description = "Development Server",
                        url = "http://76.13.245.49:8082"
                ),

                @Server(
                        description = "Production Server",
                        url = "https://api.davschool.com"
                )
        },

        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = """
                JWT Authentication using Bearer Token.
                
                Steps to authorize:
                
                1. Login using the authentication endpoint.
                2. Copy the JWT token from the login response.
                3. Click the 'Authorize' button in Swagger UI.
                4. Enter the token in the format:
                
                   Bearer your-jwt-token
                
                5. After authorization you can access all secured APIs.
                """,
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {

}