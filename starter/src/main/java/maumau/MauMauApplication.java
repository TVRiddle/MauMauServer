package maumau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Start des Servers
 */
@SpringBootApplication(scanBasePackages = {"maumau"})
@EnableJpaRepositories(basePackages = {"maumau"})
@EntityScan("maumau")
public class MauMauApplication {


    public static void main(String[] args) {
        SpringApplication.run(MauMauApplication.class, args);
    }

}
