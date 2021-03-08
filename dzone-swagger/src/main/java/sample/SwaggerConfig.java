package sample;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	ApiInfo apiInfo() {
		return new ApiInfo("A sample spring swagger example",
				"This project demonstrates some spring " 
						+ "and swagger concepts. It also demonstrates "
						+ "some enhancements in spring swagger using " 
						+ "spring-swagger-simplified",
				"1.0.6", 
				"Use as you like- some url here",
				new Contact("Contact Name", 
						"https://bitbucket.org/tek-nik/simplified-swagger-examples/", null),
				"The Apache Software License, Version 2.0", 
				"http://www.apache.org/licenses/LICENSE-2.0.txt",
				Collections.EMPTY_LIST);
	}

	@Bean
	public Docket api() {
		return changeGlobalResponses(new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(Predicates.not(RequestHandlerSelectors.
						basePackage("org.springframework.boot")))
				.paths(PathSelectors.any())
				.build()).apiInfo(apiInfo())

		;
	}

	private String resolveName(Class clazz) {
		return clazz.getName();
	}

	private Docket changeGlobalResponses(Docket docket) {
		RequestMethod[] methodsToCustomize = {
				RequestMethod.GET, RequestMethod.POST, };

		docket = docket.useDefaultResponseMessages(false);
		for (RequestMethod methodToCustomize : methodsToCustomize) {
			docket = docket
				.globalResponseMessage(methodToCustomize,
					Lists.newArrayList(
						new ResponseMessageBuilder().code(
								HttpStatus.INTERNAL_SERVER_ERROR.value())
							.message("Server error Try again later")
							.responseModel(new ModelRef(resolveName(Problem.class)))
							.build(),
						new ResponseMessageBuilder().code(
								HttpStatus.BAD_REQUEST.value())
							.message("Bad Request")
							.responseModel(new ModelRef(resolveName(ErrorMessage.class)))
							.build()));
		}

		return docket;
	}

}
