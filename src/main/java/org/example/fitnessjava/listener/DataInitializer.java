package org.example.fitnessjava.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.service.AdminUserService;
import org.example.fitnessjava.service.BannerService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataInitializer {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private CoachRepository coachRepository;

    @Resource
    private BannerService bannerService;

    @PostConstruct
    public void init() {
        initAdminUser();
        initTestData();
        initCoachData();
        initBanners();
    }

    ArrayList<String> banners = new ArrayList<>(Arrays.asList(
            "https://images.unsplash.com/photo-1659081442183-38e43365d911?w=800",
            "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800",
            "https://images.unsplash.com/photo-1761971975769-97e598bf526b?w=800"
    ));

    private void initBanners() {
        if (bannerService.getBanners().isEmpty()) {
            for (String b : banners) {
                Banner banner = new Banner();
                banner.setImage(b);
                bannerService.addBanner(banner);
            }
        }
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
        long count = clientRepository.count();
        if (count == 0) {
            Client client1 = new Client();
            client1.setOpenid("oTest001");
            client1.setNickname("张三");
            client1.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan");
            client1.setPhone("13800138001");
            client1.setMemberNumber("M2026001");
            client1.setMemberLevel("金卡会员");
            client1.setPoints(1200);
            client1.setCoupons(3);
            client1.setTotalTrainingCount(45);
            client1.setMembershipExpireAt("2026-12-31");
            clientRepository.save(client1);
            System.out.println("测试用户已创建：张三 (金卡会员)");

            Client client2 = new Client();
            client2.setOpenid("oTest002");
            client2.setNickname("李四");
            client2.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=lisi");
            client2.setPhone("13900139002");
            client2.setMemberNumber("M2026002");
            client2.setMemberLevel("银卡会员");
            client2.setPoints(650);
            client2.setCoupons(1);
            client2.setTotalTrainingCount(23);
            client2.setMembershipExpireAt("2026-06-30");
            clientRepository.save(client2);
            System.out.println("测试用户已创建：李四 (银卡会员)");

            Coach coach1 = new Coach();
            coach1.setOpenid("oCoach001");
            coach1.setNickname("王教练");
            coach1.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=coach");
            coach1.setPhone("13700137003");
            coachRepository.save(coach1);
            System.out.println("测试教练已创建：王教练");
        }
    }

    private void initCoachData() {
        long count = coachRepository.count();
        if (count == 0) {
            Coach coach1 = new Coach();
            coach1.setNickname("李教练");
            coach1.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=licoach");
            coach1.setIntro("专注力量训练 8 年，擅长制定个性化训练计划，帮助学员突破瓶颈期。");
            coach1.setSpecialty("力量训练");
            coach1.setDescription("国家一级健身教练，NSCA-CPT 认证，运动营养师");
            coach1.setRating(4.9);
            coach1.setLevel(5);
            coach1.setClassCount(234);
            coach1.setTags(Arrays.asList("力量训练", "增肌", "塑形"));
            coach1.setPhone("13800138123");
            coach1.setFeatured(true);
            coach1.setStatus(Coach.Status.ONLINE);
            coachRepository.save(coach1);

            Coach coach2 = new Coach();
            coach2.setNickname("王教练");
            coach2.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=wangcoach");
            coach2.setIntro("瑜伽与普拉提资深教练，注重身心平衡，帮助学员改善体态。");
            coach2.setSpecialty("瑜伽·普拉提");
            coach2.setDescription("RYT-500 瑜伽教练，普拉提认证教练");
            coach2.setRating(4.8);
            coach2.setLevel(4);
            coach2.setClassCount(189);
            coach2.setTags(Arrays.asList("瑜伽", "普拉提", "体态矫正"));
            coach2.setPhone("13900139456");
            coach2.setFeatured(true);
            coach2.setStatus(Coach.Status.ONLINE);
            coachRepository.save(coach2);

            Coach coach3 = new Coach();
            coach3.setNickname("张教练");
            coach3.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=zhangcoach");
            coach3.setIntro("专注减脂塑形领域，科学制定饮食与训练计划，效果显著。");
            coach3.setSpecialty("减脂塑形");
            coach3.setDescription("AASFP 私人教练，运动康复师");
            coach3.setRating(4.7);
            coach3.setLevel(4);
            coach3.setClassCount(156);
            coach3.setTags(Arrays.asList("减脂", "塑形", "营养指导"));
            coach3.setPhone("13600136789");
            coach3.setFeatured(false);
            coach3.setStatus(Coach.Status.BUSY);
            coachRepository.save(coach3);

            Coach coach4 = new Coach();
            coach4.setNickname("刘教练");
            coach4.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=liucoach");
            coach4.setIntro("功能训练专家，帮助学员提升运动表现和日常活动能力。");
            coach4.setSpecialty("功能训练");
            coach4.setDescription("FMS 功能性训练认证，NASM-CPT");
            coach4.setRating(4.6);
            coach4.setLevel(3);
            coach4.setClassCount(98);
            coach4.setTags(Arrays.asList("功能训练", "运动康复", "体能提升"));
            coach4.setPhone("13700137321");
            coach4.setFeatured(false);
            coach4.setStatus(Coach.Status.ONLINE);
            coachRepository.save(coach4);

            Coach coach5 = new Coach();
            coach5.setNickname("陈教练");
            coach5.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=chencoach");
            coach5.setIntro("拳击专业教练，结合有氧训练帮助学员快速燃脂和提升体能。");
            coach5.setSpecialty("拳击");
            coach5.setDescription("国家拳击二级运动员，Boxfit 认证教练");
            coach5.setRating(4.8);
            coach5.setLevel(4);
            coach5.setClassCount(145);
            coach5.setTags(Arrays.asList("拳击", "有氧", "燃脂"));
            coach5.setPhone("13500135654");
            coach5.setFeatured(true);
            coach5.setStatus(Coach.Status.OFFLINE);
            coachRepository.save(coach5);

            System.out.println("测试教练数据已创建：5 名教练");
        }
    }
}
