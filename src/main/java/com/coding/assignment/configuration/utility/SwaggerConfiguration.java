package com.coding.assignment.configuration.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * you will be able to visit swagger page using :
 * http://localhost:8080/swagger-ui/index.html
 */

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    public static final Contact DEFAULT_CONTACT = new Contact("Yousef Yagoub", "localhost",
            "mail@gmail.com");

    public static final ApiInfo API_INFO = new ApiInfo(
            "title","desc","1.0",
            "urn:tos", DEFAULT_CONTACT,
            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
            Arrays.asList()
    );
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(
            Arrays.asList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("title")
                .description("description")
                //.termsOfServiceUrl("")
                .contact(DEFAULT_CONTACT)
                //.license("Apache 2.0")
                //.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .version("1.0")
                .build();
    }
}
