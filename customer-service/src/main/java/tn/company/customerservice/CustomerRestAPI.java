package tn.company.customerservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CustomerRestAPI {
    @GetMapping("/customers")
    public Map<String, Object> customer() {
        return Map.of("name", "Balha",
                "email", "balhahadji@gmail.com");
    }
}
