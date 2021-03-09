package com.huhu.swagger1.config;

import com.huhu.swagger1.model.Problem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
@SuppressWarnings("deprecation")
public class Swagger2Config {
    @Bean
    public Docket api() {
        return changeGlobalResponses(new Docket(DocumentationType.SWAGGER_2))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com/huhu/swagger1/controller".replace("/",".")))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot REST API")
                .description("Employee Management REST API")
                .contact(new Contact("loda", "https://loda.me/", "loda.namnh@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }

    private Docket changeGlobalResponses(Docket docket) {
        RequestMethod[] methodsToCustomize = {
                RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT };

        docket = docket.useDefaultResponseMessages(false);
        for (RequestMethod methodToCustomize : methodsToCustomize) {
            docket = docket
                    .globalResponseMessage(methodToCustomize,
                            Arrays.asList(
                                    new ResponseMessageBuilder().code(
                                            HttpStatus.INTERNAL_SERVER_ERROR.value())
                                            .message("Server error Try again later")
                                            .responseModel(new ModelRef(Problem.class.getSimpleName()))
                                            .build(),
                                    new ResponseMessageBuilder().code(
                                            HttpStatus.BAD_REQUEST.value())
                                            .message("Bad Request")
                                            .responseModel(new ModelRef(Problem.class.getSimpleName()))
                                            .build()));
        }

        return docket;
    }
}
