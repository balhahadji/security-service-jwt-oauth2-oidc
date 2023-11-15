package tn.mycompany.securityservice.services;

import tn.mycompany.securityservice.entities.AppRole;
import tn.mycompany.securityservice.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole role);
    void addRoleToUser(String username,String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();

}
