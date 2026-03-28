package org.example.fitnessjava.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.UserProfileRepository;
import org.example.fitnessjava.pojo.AdminRole;
import org.example.fitnessjava.pojo.AdminUser;
import org.example.fitnessjava.pojo.ClientProfile;
import org.example.fitnessjava.pojo.UserProfile;
import org.example.fitnessjava.pojo.UserRole;
import org.example.fitnessjava.service.AdminUserService;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private UserProfileRepository userProfileRepository;

    @PostConstruct
    public void init() {
        initAdminUser();
        initTestData();
    }

    private void initAdminUser() {
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

    private void initTestData() {
        long count = userProfileRepository.count();
        if (count == 0) {
            ClientProfile client1 = new ClientProfile();
            client1.setOpenid("oTest001");
            client1.setNickname("张三");
            client1.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan");
            client1.setPhone("13800138001");
            client1.setRole(UserRole.CLIENT);
            client1.setMemberNumber("M2026001");
            client1.setMemberLevel("金卡会员");
            client1.setPoints(1200);
            client1.setCoupons(3);
            client1.setTotalTrainingCount(45);
            client1.setMembershipExpireAt("2026-12-31");
            userProfileRepository.save(client1);
            System.out.println("测试用户已创建：张三 (金卡会员)");

            ClientProfile client2 = new ClientProfile();
            client2.setOpenid("oTest002");
            client2.setNickname("李四");
            client2.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=lisi");
            client2.setPhone("13900139002");
            client2.setRole(UserRole.CLIENT);
            client2.setMemberNumber("M2026002");
            client2.setMemberLevel("银卡会员");
            client2.setPoints(650);
            client2.setCoupons(1);
            client2.setTotalTrainingCount(23);
            client2.setMembershipExpireAt("2026-06-30");
            userProfileRepository.save(client2);
            System.out.println("测试用户已创建：李四 (银卡会员)");

            UserProfile coach1 = new UserProfile();
            coach1.setOpenid("oCoach001");
            coach1.setNickname("王教练");
            coach1.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=coach");
            coach1.setPhone("13700137003");
            coach1.setRole(UserRole.COACH);
            userProfileRepository.save(coach1);
            System.out.println("测试教练已创建：王教练");
        }
    }
}
