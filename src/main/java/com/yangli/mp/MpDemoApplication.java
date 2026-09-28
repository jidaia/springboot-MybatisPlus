package com.yangli.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yangli.mp.mapper")
@SpringBootApplication
public class MpDemoApplication {
    //访问这个页面 http://localhost:8080/swagger-ui/index.html#/
    public static void main(String[] args) {
        SpringApplication.run(MpDemoApplication.class, args);
    }

}

