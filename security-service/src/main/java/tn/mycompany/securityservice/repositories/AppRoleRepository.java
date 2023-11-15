package tn.mycompany.securityservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.mycompany.securityservice.entities.AppRole;
import tn.mycompany.securityservice.entities.AppUser;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String roleName);
}
