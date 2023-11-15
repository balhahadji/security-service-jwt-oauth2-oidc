package tn.mycompany.securityservice.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.mycompany.securityservice.dto.LoginRequest;
import tn.mycompany.securityservice.entities.AppUser;
import tn.mycompany.securityservice.services.AccountService;
import tn.mycompany.securityservice.services.TokenService;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AuthController {
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private TokenService tokenService;
    private AccountService accountService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> jwtToken(LoginRequest loginRequest) {
        Map<String, String> response;

        if (loginRequest.grantType().equals("password")) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
            response = tokenService.generateJwtToken(authentication.getName(), authentication.getAuthorities(), loginRequest.withRefreshToken());
            return ResponseEntity.ok(response);
        } else if (loginRequest.grantType().equals("refreshToken")) {
            String refreshToken = loginRequest.refreshToken();
            if (refreshToken == null) {
                return new ResponseEntity<>(Map.of("error", "RefreshToken Not Present"), HttpStatus.UNAUTHORIZED);
            }
            Jwt decodeJWT = jwtDecoder.decode(loginRequest.refreshToken());
            String username = decodeJWT.getSubject();
            AppUser appUser = accountService.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = appUser.getAppRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
            response = tokenService.generateJwtToken(appUser.getUsername(), authorities, loginRequest.withRefreshToken());
            return ResponseEntity.ok(response);
        }

        return new ResponseEntity(Map.of("error", String.format("grantType <<%s>> not supported ", loginRequest.grantType())), HttpStatus.UNAUTHORIZED);

    }
}
