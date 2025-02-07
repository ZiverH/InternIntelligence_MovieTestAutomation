package org.app.movie;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieApplication {

    public static void main(String[] args) {
        System.out.println("Ziver");
        SpringApplication.run(MovieApplication.class, args);
    }

}
