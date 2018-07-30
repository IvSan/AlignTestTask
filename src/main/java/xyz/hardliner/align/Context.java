package xyz.hardliner.align;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"xyz.hardliner.align"})
@PropertySource(value = "application.properties", encoding = "UTF-8")
public class Context {

}
