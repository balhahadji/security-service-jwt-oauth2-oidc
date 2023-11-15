package tn.company.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tn.company.customerservice.config.RsaKeysConfig;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}
