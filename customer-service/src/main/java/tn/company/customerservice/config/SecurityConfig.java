package tn.company.customerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private RsaKeysConfig rsaKeysConfig;

    public SecurityConfig(RsaKeysConfig rsaKeysConfig) {
        this.rsaKeysConfig = rsaKeysConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .authorizeHttpRequests(aut->aut.anyRequest().authenticated())
                .build();
    }
    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaKeysConfig.publicKey()).build();

    }
}
