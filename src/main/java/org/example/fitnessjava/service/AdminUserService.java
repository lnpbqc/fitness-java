package org.example.fitnessjava.service;

import org.example.fitnessjava.pojo.AdminUser;

import java.util.Optional;

public interface AdminUserService {
    Optional<AdminUser> findByUsername(String username);
    boolean existsByUsername(String username);
    AdminUser createUser(AdminUser adminUser);
    AdminUser authenticate(String username, String password);
}
