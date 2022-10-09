package me.tianshuang;

import me.tianshuang.mapper.Mapper1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MybatisRaceConditionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisRaceConditionApplication.class, args);
    }

    @Autowired
    private Mapper1 mapper1;

    private volatile boolean applicationReady;

    @PostConstruct
    public void raceConditionTest() {
        new Thread(() -> {
            int successfulCalls = 0;
            while (!applicationReady) {
                try {
                    mapper1.select1();
                    successfulCalls++;
                } catch (Exception e) {
                    System.err.println("Number of successful calls before application ready: " + successfulCalls);
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        applicationReady = true;
        System.out.println("Context Ready Event received.");
    }

}
