package net.say2you;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"net.say2you"})
@MapperScan("net.say2you.dao")
public class WChatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WChatServerApplication.class, args);
	}

}

