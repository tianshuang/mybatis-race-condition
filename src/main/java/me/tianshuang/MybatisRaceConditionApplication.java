package me.tianshuang;

import me.tianshuang.mapper.Mapper1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MybatisRaceConditionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisRaceConditionApplication.class, args);
    }

    @Autowired
    private Mapper1 mapper1;

    @PostConstruct
    public void raceConditionTest() {
        new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                try {
                    mapper1.select1();
                } catch (Exception e) {
                    System.err.println("current i: " + i);
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

}
