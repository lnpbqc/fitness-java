package org.example.fitnessjava.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.fitnessjava.pojo.AdminRole;
import org.example.fitnessjava.pojo.AdminUser;
import org.example.fitnessjava.service.AdminUserService;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Resource
    private AdminUserService adminUserService;

    @PostConstruct
    public void init() {
        if (!adminUserService.existsByUsername("admin")) {
            AdminUser superAdmin = new AdminUser();
            superAdmin.setUsername("admin");
            superAdmin.setPassword("admin123");
            superAdmin.setNickname("超级管理员");
            superAdmin.setRole(AdminRole.SUPER_ADMIN);
            superAdmin.setEnabled(true);
            adminUserService.createUser(superAdmin);
            System.out.println("默认超级管理员账号已创建：admin / admin123");
        }
    }
}
