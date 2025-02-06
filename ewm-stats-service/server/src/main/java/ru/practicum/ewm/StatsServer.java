package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.practicum.ewm.exception.GlobalExceptionHandler;

@SpringBootApplication
@Import(GlobalExceptionHandler.class)
public class StatsServer {
    public static void main(String[] args) {
        SpringApplication.run(StatsServer.class, args);
    }
}
