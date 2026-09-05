package com.jobportal.smartjobportal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Job Portal API")
                        .version("1.0")
                        .description(
                                "REST API for managing jobs, searching jobs, " +
                                "filtering jobs, pagination, and sorting."
                        )
                )
                .addServersItem(
                        new Server()
                                .url("https://smart-job-portal-production.up.railway.app")
                                .description("Railway Production Server")
                );
    }
}