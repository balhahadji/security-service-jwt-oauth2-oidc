package tn.mycompany.securityservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.mycompany.securityservice.entities.AppRole;
import tn.mycompany.securityservice.entities.AppUser;
import tn.mycompany.securityservice.repositories.AppRoleRepository;
import tn.mycompany.securityservice.repositories.AppUserRepository;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
  //  private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
     //   this.passwordEncoder = passwordEncoder;
    }

    @Override

    public AppUser addNewUser(AppUser appUser) {
        String password = appUser.getPassword();
     //   appUser.setPassword(passwordEncoder.encode(password));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {

        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {

        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {

        return appUserRepository.findAll();
    }
}
