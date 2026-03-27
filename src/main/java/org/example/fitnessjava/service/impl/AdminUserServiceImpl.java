package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.AdminUserRepository;
import org.example.fitnessjava.pojo.AdminUser;
import org.example.fitnessjava.service.AdminUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserRepository adminUserRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<AdminUser> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return adminUserRepository.existsByUsername(username);
    }

    @Override
    public AdminUser createUser(AdminUser adminUser) {
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        adminUser.setEnabled(true);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        return adminUserRepository.save(adminUser);
    }

    @Override
    public AdminUser authenticate(String username, String password) {
        Optional<AdminUser> userOpt = adminUserRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            AdminUser user = userOpt.get();
            if (user.getEnabled() && passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
