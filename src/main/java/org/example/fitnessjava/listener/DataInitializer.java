package org.example.fitnessjava.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.dao.CoachRepository;
import org.example.fitnessjava.dao.CourseOrderRepository;
import org.example.fitnessjava.dao.PackageProductRepository;
import org.example.fitnessjava.dao.ProductRepository;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.example.fitnessjava.pojo.PackageType;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.pojo.penddingEntity.PackageProduct;
import org.example.fitnessjava.pojo.penddingEntity.Product;
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

    @Resource
    private PackageProductRepository packageProductRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CourseOrderRepository courseOrderRepository;

    @PostConstruct
    public void init() {
        initAdminUser();
        initTestData();
        initCoachData();
        initBanners();
        initPackageData();
        initProductData();
        initCourseOrderData();
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


            Coach coach6 = new Coach();
            coach6.setOpenid("oCoach001");
            coach6.setNickname("王教练");
            coach6.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=coach");
            coach6.setPhone("13700137003");
            coachRepository.save(coach6);
            System.out.println("王教练");

            System.out.println("测试教练数据已创建：6 名教练");
        }
    }

    private void initPackageData() {
        long count = packageProductRepository.count();
        if (count == 0) {
            PackageProduct p1 = new PackageProduct();
            p1.setName("月度健身卡");
            p1.setType(PackageType.TIME_CARD);
            p1.setSessions(0);
            p1.setValidDays(30);
            p1.setPrice(599.0);
            p1.setOriginalPrice(699.0);
            p1.setPointsReward(60);
            p1.setDescription("适合有固定锻炼习惯的会员，畅享 30 天不限次数训练");
            p1.setSaleStatus(SaleStatus.ON_SALE);
            packageProductRepository.save(p1);

            PackageProduct p2 = new PackageProduct();
            p2.setName("20 次私教课");
            p2.setType(PackageType.SESSION_CARD);
            p2.setSessions(20);
            p2.setValidDays(90);
            p2.setPrice(3999.0);
            p2.setOriginalPrice(4999.0);
            p2.setPointsReward(400);
            p2.setDescription("专业私教一对一指导，定制化训练方案");
            p2.setSaleStatus(SaleStatus.ON_SALE);
            packageProductRepository.save(p2);

            PackageProduct p3 = new PackageProduct();
            p3.setName("体测评估服务");
            p3.setType(PackageType.ASSESSMENT);
            p3.setSessions(1);
            p3.setValidDays(7);
            p3.setPrice(199.0);
            p3.setPointsReward(20);
            p3.setDescription("专业体测设备，全方位了解身体状况");
            p3.setSaleStatus(SaleStatus.ON_SALE);
            packageProductRepository.save(p3);

            PackageProduct p4 = new PackageProduct();
            p4.setName("季度健身卡");
            p4.setType(PackageType.TIME_CARD);
            p4.setSessions(0);
            p4.setValidDays(90);
            p4.setPrice(1599.0);
            p4.setOriginalPrice(1999.0);
            p4.setPointsReward(160);
            p4.setDescription("更长周期，更优价格，适合长期健身的会员");
            p4.setSaleStatus(SaleStatus.ON_SALE);
            packageProductRepository.save(p4);

            PackageProduct p5 = new PackageProduct();
            p5.setName("体验课程");
            p5.setType(PackageType.EXPERIENCE);
            p5.setSessions(1);
            p5.setValidDays(7);
            p5.setPrice(9.9);
            p5.setPointsReward(1);
            p5.setDescription("新用户专享，超值体验价");
            p5.setSaleStatus(SaleStatus.OFF_SALE);
            packageProductRepository.save(p5);

            System.out.println("测试套餐数据已创建：5 个套餐");
        }
    }

    private void initProductData() {
        long count = productRepository.count();
        if (count == 0) {
            Product p1 = new Product();
            p1.setName("蛋白粉 - 巧克力味");
            p1.setCategory("营养补剂");
            p1.setPrice(299.0);
            p1.setImage("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400");
            p1.setPointsPrice(2990);
            p1.setPointsReward(30);
            p1.setDesc("高品质乳清蛋白，增肌必备，每份含 25g 蛋白质");
            p1.setStock(156);
            p1.setSaleStatus(SaleStatus.ON_SALE);
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setName("运动毛巾");
            p2.setCategory("运动配件");
            p2.setPrice(59.0);
            p2.setImage("https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400");
            p2.setPointsPrice(590);
            p2.setPointsReward(6);
            p2.setDesc("速干吸水，柔软舒适，运动必备");
            p2.setStock(89);
            p2.setSaleStatus(SaleStatus.ON_SALE);
            productRepository.save(p2);

            Product p3 = new Product();
            p3.setName("运动水杯");
            p3.setCategory("运动配件");
            p3.setPrice(89.0);
            p3.setImage("https://images.unsplash.com/photo-1602143407151-01114192003f?w=400");
            p3.setPointsPrice(890);
            p3.setPointsReward(9);
            p3.setDesc("便携大容量，食品级材质，安全健康");
            p3.setStock(234);
            p3.setSaleStatus(SaleStatus.ON_SALE);
            productRepository.save(p3);

            Product p4 = new Product();
            p4.setName("BCAA 氨基酸");
            p4.setCategory("营养补剂");
            p4.setPrice(199.0);
            p4.setImage("https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400");
            p4.setPointsPrice(1990);
            p4.setPointsReward(20);
            p4.setDesc("支链氨基酸，帮助肌肉恢复，减少疲劳");
            p4.setStock(67);
            p4.setSaleStatus(SaleStatus.ON_SALE);
            productRepository.save(p4);

            Product p5 = new Product();
            p5.setName("健身手套");
            p5.setCategory("运动配件");
            p5.setPrice(79.0);
            p5.setImage("https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400");
            p5.setPointsPrice(790);
            p5.setPointsReward(8);
            p5.setDesc("防滑耐磨，保护手掌，提升训练体验");
            p5.setStock(0);
            p5.setSaleStatus(SaleStatus.OFF_SALE);
            productRepository.save(p5);

            Product p6 = new Product();
            p6.setName("瑜伽垫");
            p6.setCategory("运动器材");
            p6.setPrice(199.0);
            p6.setImage("https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400");
            p6.setPointsPrice(1990);
            p6.setPointsReward(20);
            p6.setDesc("加厚防滑，环保材质，瑜伽练习必备");
            p6.setStock(45);
            p6.setSaleStatus(SaleStatus.ON_SALE);
            productRepository.save(p6);

            Product p7 = new Product();
            p7.setName("弹力带套装");
            p7.setCategory("运动器材");
            p7.setPrice(129.0);
            p7.setImage("https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400");
            p7.setPointsPrice(1290);
            p7.setPointsReward(13);
            p7.setDesc("多阻力级别，适合不同训练强度");
            p7.setStock(78);
            p7.setSaleStatus(SaleStatus.ON_SALE);
            productRepository.save(p7);

            Product p8 = new Product();
            p8.setName("左旋肉碱");
            p8.setCategory("营养补剂");
            p8.setPrice(259.0);
            p8.setImage("https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400");
            p8.setPointsPrice(2590);
            p8.setPointsReward(26);
            p8.setDesc("帮助脂肪代谢，配合运动效果更佳");
            p8.setStock(92);
            p8.setSaleStatus(SaleStatus.ON_SALE);
            productRepository.save(p8);

            System.out.println("测试商品数据已创建：8 个商品");
        }
    }

    private void initCourseOrderData() {
        long count = courseOrderRepository.count();
        if (count == 0) {
            CourseOrder o1 = new CourseOrder();
            o1.setUserId(1);
            o1.setPackageId(2);
            o1.setPackageName("20 次私教课");
            o1.setType(PackageType.SESSION_CARD);
            o1.setTotalSessions(20);
            o1.setUsedSessions(8);
            o1.setRemainingSessions(12);
            o1.setValidDays(90);
            o1.setStartDate("2026-03-01");
            o1.setEndDate("2026-05-30");
            o1.setPurchaseDate("2026-03-01");
            o1.setPrice(3999.0);
            o1.setPointsReward(400);
            o1.setStatus(CourseOrderStatus.ACTIVE);
            courseOrderRepository.save(o1);

            CourseOrder o2 = new CourseOrder();
            o2.setUserId(2);
            o2.setPackageId(1);
            o2.setPackageName("月度健身卡");
            o2.setType(PackageType.TIME_CARD);
            o2.setTotalSessions(0);
            o2.setUsedSessions(0);
            o2.setRemainingSessions(0);
            o2.setValidDays(30);
            o2.setStartDate("2026-03-15");
            o2.setEndDate("2026-04-14");
            o2.setPurchaseDate("2026-03-15");
            o2.setPrice(599.0);
            o2.setPointsReward(60);
            o2.setStatus(CourseOrderStatus.ACTIVE);
            courseOrderRepository.save(o2);

            CourseOrder o3 = new CourseOrder();
            o3.setUserId(1);
            o3.setPackageId(4);
            o3.setPackageName("季度健身卡");
            o3.setType(PackageType.TIME_CARD);
            o3.setTotalSessions(0);
            o3.setUsedSessions(0);
            o3.setRemainingSessions(0);
            o3.setValidDays(90);
            o3.setStartDate("2026-02-01");
            o3.setEndDate("2026-05-01");
            o3.setPurchaseDate("2026-02-01");
            o3.setPrice(1599.0);
            o3.setPointsReward(160);
            o3.setStatus(CourseOrderStatus.ACTIVE);
            courseOrderRepository.save(o3);

            CourseOrder o4 = new CourseOrder();
            o4.setUserId(3);
            o4.setPackageId(3);
            o4.setPackageName("体测评估服务");
            o4.setType(PackageType.ASSESSMENT);
            o4.setTotalSessions(1);
            o4.setUsedSessions(1);
            o4.setRemainingSessions(0);
            o4.setValidDays(7);
            o4.setStartDate("2026-03-20");
            o4.setEndDate("2026-03-27");
            o4.setPurchaseDate("2026-03-20");
            o4.setPrice(199.0);
            o4.setPointsReward(20);
            o4.setStatus(CourseOrderStatus.COMPLETED);
            courseOrderRepository.save(o4);

            CourseOrder o5 = new CourseOrder();
            o5.setUserId(4);
            o5.setPackageId(5);
            o5.setPackageName("体验课程");
            o5.setType(PackageType.EXPERIENCE);
            o5.setTotalSessions(1);
            o5.setUsedSessions(0);
            o5.setRemainingSessions(1);
            o5.setValidDays(7);
            o5.setStartDate("2026-03-25");
            o5.setEndDate("2026-04-01");
            o5.setPurchaseDate("2026-03-25");
            o5.setPrice(9.9);
            o5.setPointsReward(1);
            o5.setStatus(CourseOrderStatus.ACTIVE);
            courseOrderRepository.save(o5);

            CourseOrder o6 = new CourseOrder();
            o6.setUserId(5);
            o6.setPackageId(2);
            o6.setPackageName("20 次私教课");
            o6.setType(PackageType.SESSION_CARD);
            o6.setTotalSessions(20);
            o6.setUsedSessions(15);
            o6.setRemainingSessions(5);
            o6.setValidDays(90);
            o6.setStartDate("2026-01-15");
            o6.setEndDate("2026-04-15");
            o6.setPurchaseDate("2026-01-15");
            o6.setPrice(3999.0);
            o6.setPointsReward(400);
            o6.setStatus(CourseOrderStatus.REFUNDING);
            courseOrderRepository.save(o6);

            System.out.println("测试课程订单数据已创建：6 个订单");
        }
    }
}
