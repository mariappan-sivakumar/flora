package com.mari.flora;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Imai Flora API",
                version = "1.0",
                description = "Backend for Imai Flora garland management"
        )
)
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO) // ✅ stable Page JSON
public class FloraApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloraApplication.class, args);
    }

}
