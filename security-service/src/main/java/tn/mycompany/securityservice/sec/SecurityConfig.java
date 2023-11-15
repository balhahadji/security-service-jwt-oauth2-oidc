package tn.mycompany.securityservice.sec;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tn.mycompany.securityservice.services.UserDetailsServiceImpl;

import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private RsaKeysConfig rsaKeysConfig;
    private PasswordEncoder passwordEncoder;
    // private AccountService accountService;
     private UserDetailsServiceImpl userDetailsService;

    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("balha").password(passwordEncoder.encode("1234")).authorities("USER").build(),
                User.withUsername("balha1").password(passwordEncoder.encode("1234")).authorities("USER").build(),
                User.withUsername("balha2").password(passwordEncoder.encode("1234")).authorities("USER", "ADMIN").build()
        );
    }

    // Ancien technique non recommandé
    // @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(c -> c.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()))
                // on desactive les frames pour h2 console
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.disable()
                ))
                .authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/token/**")).permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        //.userDetailsService(userDetailsService);
        return httpSecurity.build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeysConfig.publicKey()).privateKey(rsaKeysConfig.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaKeysConfig.publicKey()).build();
    }

}
