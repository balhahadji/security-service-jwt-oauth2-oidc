package tn.mycompany.securityservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.mycompany.securityservice.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
