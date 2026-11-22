package io.github.parkwoong.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.crypto.KeyGenerator;
import java.time.Instant;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
@SpringBootApplication
public class DemoApplication {
 public static void main(String[] args) { SpringApplication.run(DemoApplication.class, args); }
 @Bean CommandLineRunner demo() { return args -> { var generator = KeyGenerator.getInstance("HmacSHA256"); generator.init(256); var key = generator.generateKey(); System.out.println(TokenVerifier.hmac(key).decode(SampleToken.sign(key, Instant.now().plusSeconds(60))).getSubject()); }; }
}
