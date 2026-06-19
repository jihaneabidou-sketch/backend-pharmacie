package com.pharmacie.enligne.controller;

import com.pharmacie.enligne.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<User> obtenirUtilisateurParId(@PathVariable Long id) {
        User user = new User();
        user.setId(id);
        return ResponseEntity.ok(user);
    }
}