package me.tianshuang;

import me.tianshuang.mapper.Mapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtherConfiguration {

    @Autowired
    private Mapper2 mapper2;

}
