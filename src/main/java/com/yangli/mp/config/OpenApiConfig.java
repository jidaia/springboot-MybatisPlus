package com.yangli.mp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("用户管理接口文档")
                        .description("用户管理接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("虎哥")
                                .email("zhanghuyi@itcast.cn")
                                .url("https://www.itcast.cn")));
    }
}
