package zyc.work.databasework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DataBaseWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataBaseWorkApplication.class, args);
    }

}
