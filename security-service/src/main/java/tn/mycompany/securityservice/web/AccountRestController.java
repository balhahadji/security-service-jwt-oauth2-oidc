package tn.mycompany.securityservice.web;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.mycompany.securityservice.entities.AppRole;
import tn.mycompany.securityservice.entities.AppUser;
import tn.mycompany.securityservice.services.AccountService;

import java.util.List;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/users")
    public List<AppUser> appUserList() {
        return accountService.listUsers();
    }

    @PostMapping("/users")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return accountService.addNewUser(appUser);
    }

    @PostMapping("/roles")
    public AppRole saveUser(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }

    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }
}

@Data
class RoleUserForm {
    private String username;
    private String roleName;
}
