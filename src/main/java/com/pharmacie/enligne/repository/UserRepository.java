package com.pharmacie.enligne.repository;

import com.pharmacie.enligne.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data fera automatiquement le lien avec le champ 'email' de l'entité
    Optional<User> findByEmail(String email); 
}