package es.amplia.springamqp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;

@SpringBootApplication(exclude = {WebClientAutoConfiguration.class})
public class SpringAmqpApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringAmqpApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
    }
}