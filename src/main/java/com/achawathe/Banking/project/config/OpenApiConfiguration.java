package com.achawathe.Banking.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Akhil Chawathe");
        myContact.setEmail("akhil.chawathe@gmail.com");

        Info information = new Info()
                .title("Banking Application API")
                .version("1.0")
                .description("This API exposes endpoints to manage users, accounts and transactions.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }

}
