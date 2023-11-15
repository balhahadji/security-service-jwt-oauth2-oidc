package tn.mycompany.securityservice.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.mycompany.securityservice.entities.AppUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser==null) throw new UsernameNotFoundException(String.format("User %s not found",username));
        String[] roles = appUser.getAppRoles().stream().map(appRole -> appRole.getRoleName()).toArray(String[]::new);
        UserDetails userDetails=
                User.withUsername(appUser.getUsername())
                        .password(appUser.getPassword())
                        .roles(roles)
                        .build();
        return userDetails;
    }
}
