package org.example.fitnessjava.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.*;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.CourseOrder;
import org.example.fitnessjava.pojo.CourseOrderStatus;
import org.example.fitnessjava.pojo.PackageType;
import org.example.fitnessjava.pojo.ProductOrder;
import org.example.fitnessjava.pojo.ProductOrderItem;
import org.example.fitnessjava.pojo.ProductOrderStatus;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.pojo.Product;
import org.example.fitnessjava.service.AdminUserService;
import org.example.fitnessjava.service.BannerService;
import org.example.fitnessjava.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Resource
    private ProductOrderRepository productOrderRepository;

    @Resource
    private HealthSurveyRepository healthSurveyRepository;

    @Resource
    private CoachScheduleSlotRepository coachScheduleSlotRepository;

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private NotificationService notificationService;

    @PostConstruct
    public void init() {
        initAdminUser();
        initTestData();
        initCoachData();
        initBanners();
        initPackageData();
        initProductData();
        initCourseOrderData();
        initProductOrderData();
        initHealthSurveyData();
        initCoachScheduleSlotData();
        initBookingData();
        initNotifications();
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

            Client client3 = new Client();
            client3.setOpenid("oTest003");
            client3.setNickname("王五");
            client3.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=wangwu");
            client3.setPhone("13600136003");
            client3.setMemberNumber("M2026003");
            client3.setMemberLevel("普通会员");
            client3.setPoints(320);
            client3.setCoupons(0);
            client3.setTotalTrainingCount(8);
            client3.setMembershipExpireAt("2026-04-30");
            clientRepository.save(client3);
            System.out.println("测试用户已创建：王五 (普通会员)");

            Client client4 = new Client();
            client4.setOpenid("oTest004");
            client4.setNickname("赵六");
            client4.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=zhaoliu");
            client4.setPhone("13500135004");
            client4.setMemberNumber("M2026004");
            client4.setMemberLevel("银卡会员");
            client4.setPoints(780);
            client4.setCoupons(2);
            client4.setTotalTrainingCount(31);
            client4.setMembershipExpireAt("2026-08-31");
            clientRepository.save(client4);
            System.out.println("测试用户已创建：赵六 (银卡会员)");

            Client client5 = new Client();
            client5.setOpenid("oTest005");
            client5.setNickname("李明");
            client5.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=liming");
            client5.setPhone("13400134005");
            client5.setMemberNumber("M2026005");
            client5.setMemberLevel("金卡会员");
            client5.setPoints(1580);
            client5.setCoupons(4);
            client5.setTotalTrainingCount(56);
            client5.setMembershipExpireAt("2026-12-31");
            clientRepository.save(client5);
            System.out.println("测试用户已创建：李明 (金卡会员)");

            Client client6 = new Client();
            client6.setOpenid("oTest006");
            client6.setNickname("刘强");
            client6.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=liuqiang");
            client6.setPhone("13300133006");
            client6.setMemberNumber("M2026006");
            client6.setMemberLevel("普通会员");
            client6.setPoints(150);
            client6.setCoupons(0);
            client6.setTotalTrainingCount(3);
            client6.setMembershipExpireAt("2026-05-31");
            clientRepository.save(client6);
            System.out.println("测试用户已创建：刘强 (普通会员)");

            Client client7 = new Client();
            client7.setOpenid("oTest007");
            client7.setNickname("陈静");
            client7.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=chenjing");
            client7.setPhone("13200132007");
            client7.setMemberNumber("M2026007");
            client7.setMemberLevel("钻石会员");
            client7.setPoints(2580);
            client7.setCoupons(6);
            client7.setTotalTrainingCount(89);
            client7.setMembershipExpireAt("2027-03-31");
            clientRepository.save(client7);
            System.out.println("测试用户已创建：陈静 (钻石会员)");

            Client client8 = new Client();
            client8.setOpenid("oTest008");
            client8.setNickname("周杰");
            client8.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=zhoujie");
            client8.setPhone("13100131008");
            client8.setMemberNumber("M2026008");
            client8.setMemberLevel("银卡会员");
            client8.setPoints(520);
            client8.setCoupons(1);
            client8.setTotalTrainingCount(15);
            client8.setMembershipExpireAt("2026-07-31");
            clientRepository.save(client8);
            System.out.println("测试用户已创建：周杰 (银卡会员)");

            Client client9 = new Client();
            client9.setOpenid("oTest009");
            client9.setNickname("吴梅");
            client9.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=wumei");
            client9.setPhone("13000130009");
            client9.setMemberNumber("M2026009");
            client9.setMemberLevel("金卡会员");
            client9.setPoints(1350);
            client9.setCoupons(3);
            client9.setTotalTrainingCount(42);
            client9.setMembershipExpireAt("2026-11-30");
            clientRepository.save(client9);
            System.out.println("测试用户已创建：吴梅 (金卡会员)");

            Client client10 = new Client();
            client10.setOpenid("oTest010");
            client10.setNickname("郑浩");
            client10.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=zhenghao");
            client10.setPhone("13700137010");
            client10.setMemberNumber("M2026010");
            client10.setMemberLevel("普通会员");
            client10.setPoints(280);
            client10.setCoupons(0);
            client10.setTotalTrainingCount(6);
            client10.setMembershipExpireAt("2026-05-15");
            clientRepository.save(client10);
            System.out.println("测试用户已创建：郑浩 (普通会员)");

            Client client11 = new Client();
            client11.setOpenid("oTest011");
            client11.setNickname("孙丽");
            client11.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=sunli");
            client11.setPhone("13800138011");
            client11.setMemberNumber("M2026011");
            client11.setMemberLevel("银卡会员");
            client11.setPoints(890);
            client11.setCoupons(2);
            client11.setTotalTrainingCount(28);
            client11.setMembershipExpireAt("2026-09-30");
            clientRepository.save(client11);
            System.out.println("测试用户已创建：孙丽 (银卡会员)");

            Client client12 = new Client();
            client12.setOpenid("oTest012");
            client12.setNickname("王伟");
            client12.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=wangwei");
            client12.setPhone("13900139012");
            client12.setMemberNumber("M2026012");
            client12.setMemberLevel("金卡会员");
            client12.setPoints(1680);
            client12.setCoupons(5);
            client12.setTotalTrainingCount(67);
            client12.setMembershipExpireAt("2027-01-31");
            clientRepository.save(client12);
            System.out.println("测试用户已创建：王伟 (金卡会员)");

            Client client13 = new Client();
            client13.setOpenid("oTest013");
            client13.setNickname("冯敏");
            client13.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=fengmin");
            client13.setPhone("13600136013");
            client13.setMemberNumber("M2026013");
            client13.setMemberLevel("钻石会员");
            client13.setPoints(3200);
            client13.setCoupons(8);
            client13.setTotalTrainingCount(120);
            client13.setMembershipExpireAt("2027-06-30");
            clientRepository.save(client13);
            System.out.println("测试用户已创建：冯敏 (钻石会员)");

            Client client14 = new Client();
            client14.setOpenid("oTest014");
            client14.setNickname("陈晨");
            client14.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=chenchen");
            client14.setPhone("13500135014");
            client14.setMemberNumber("M2026014");
            client14.setMemberLevel("普通会员");
            client14.setPoints(95);
            client14.setCoupons(0);
            client14.setTotalTrainingCount(2);
            client14.setMembershipExpireAt("2026-04-15");
            clientRepository.save(client14);
            System.out.println("测试用户已创建：陈晨 (普通会员)");

            Client client15 = new Client();
            client15.setOpenid("oTest015");
            client15.setNickname("褚雪");
            client15.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=chuxue");
            client15.setPhone("13400134015");
            client15.setMemberNumber("M2026015");
            client15.setMemberLevel("银卡会员");
            client15.setPoints(720);
            client15.setCoupons(2);
            client15.setTotalTrainingCount(25);
            client15.setMembershipExpireAt("2026-08-15");
            clientRepository.save(client15);
            System.out.println("测试用户已创建：褚雪 (银卡会员)");

            System.out.println("测试用户数据已创建：15 名用户");
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
            coach6.setNickname("赵教练");
            coach6.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=zhaocoach");
            coach6.setIntro("有氧与核心训练教练，注重体能全面提升和心肺功能改善。");
            coach6.setSpecialty("有氧训练");
            coach6.setDescription("ACE-CPT 认证教练，心肺康复训练师");
            coach6.setRating(4.5);
            coach6.setLevel(3);
            coach6.setClassCount(72);
            coach6.setTags(Arrays.asList("有氧", "核心训练", "体能"));
            coach6.setPhone("13700137003");
            coach6.setFeatured(false);
            coach6.setStatus(Coach.Status.ONLINE);
            coachRepository.save(coach6);

            System.out.println("测试教练数据已创建：6 名教练");
        }
    }

    private void initPackageData() {
        long count = packageProductRepository.count();
        if (count == 0) {
            Package p1 = new Package();
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

            Package p2 = new Package();
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

            Package p3 = new Package();
            p3.setName("体测评估服务");
            p3.setType(PackageType.ASSESSMENT);
            p3.setSessions(1);
            p3.setValidDays(7);
            p3.setPrice(199.0);
            p3.setPointsReward(20);
            p3.setDescription("专业体测设备，全方位了解身体状况");
            p3.setSaleStatus(SaleStatus.ON_SALE);
            packageProductRepository.save(p3);

            Package p4 = new Package();
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

            Package p5 = new Package();
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

    private void initProductOrderData() {
        long count = productOrderRepository.count();
        if (count == 0) {
            ProductOrder o1 = new ProductOrder();
            o1.setUserId(1);
            o1.setItems(List.of(
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 2, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                createOrderItem(3, "运动水杯", "500ml 蓝色", 1, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400")
            ));
            o1.setTotalAmount(687.0);
            o1.setPointsUsed(0);
            o1.setActualPay(687.0);
            o1.setOrderDate("2026-03-26");
            o1.setStatus(ProductOrderStatus.PAID);
            o1.setStatusText("已付款");
            productOrderRepository.save(o1);

            ProductOrder o2 = new ProductOrder();
            o2.setUserId(2);
            o2.setItems(List.of(
                createOrderItem(2, "运动毛巾", "灰色 L 码", 3, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400")
            ));
            o2.setTotalAmount(177.0);
            o2.setPointsUsed(100);
            o2.setActualPay(167.0);
            o2.setOrderDate("2026-03-25");
            o2.setStatus(ProductOrderStatus.SHIPPED);
            o2.setStatusText("已发货");
            o2.setTrackingNumber("SF1234567890");
            o2.setEstimatedDelivery("2026-03-28");
            productOrderRepository.save(o2);

            ProductOrder o3 = new ProductOrder();
            o3.setUserId(3);
            o3.setItems(List.of(
                createOrderItem(6, "瑜伽垫", "紫色 10mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"),
                createOrderItem(7, "弹力带套装", "阻力组合装", 2, 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400")
            ));
            o3.setTotalAmount(457.0);
            o3.setPointsUsed(200);
            o3.setActualPay(437.0);
            o3.setOrderDate("2026-03-24");
            o3.setStatus(ProductOrderStatus.DELIVERED);
            o3.setStatusText("已送达");
            o3.setTrackingNumber("SF1234567891");
            o3.setEstimatedDelivery("2026-03-27");
            productOrderRepository.save(o3);

            ProductOrder o4 = new ProductOrder();
            o4.setUserId(4);
            o4.setItems(List.of(
                createOrderItem(4, "BCAA 氨基酸", "300g 柠檬味", 1, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400")
            ));
            o4.setTotalAmount(199.0);
            o4.setPointsUsed(0);
            o4.setActualPay(199.0);
            o4.setOrderDate("2026-03-23");
            o4.setStatus(ProductOrderStatus.CANCELLED);
            o4.setStatusText("已退款：用户申请退款");
            productOrderRepository.save(o4);

            ProductOrder o5 = new ProductOrder();
            o5.setUserId(5);
            o5.setItems(List.of(
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                createOrderItem(8, "左旋肉碱", "60 粒装", 1, 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400")
            ));
            o5.setTotalAmount(558.0);
            o5.setPointsUsed(0);
            o5.setActualPay(558.0);
            o5.setOrderDate("2026-03-22");
            o5.setStatus(ProductOrderStatus.PAID);
            o5.setStatusText("已付款");
            productOrderRepository.save(o5);

            ProductOrder o6 = new ProductOrder();
            o6.setUserId(6);
            o6.setItems(List.of(
                createOrderItem(3, "运动水杯", "750ml 黑色", 2, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400"),
                createOrderItem(2, "运动毛巾", "蓝色 M 码", 2, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400")
            ));
            o6.setTotalAmount(296.0);
            o6.setPointsUsed(150);
            o6.setActualPay(146.0);
            o6.setOrderDate("2026-03-21");
            o6.setStatus(ProductOrderStatus.SHIPPED);
            o6.setStatusText("已发货");
            o6.setTrackingNumber("SF1234567892");
            o6.setEstimatedDelivery("2026-03-24");
            productOrderRepository.save(o6);

            ProductOrder o7 = new ProductOrder();
            o7.setUserId(7);
            o7.setItems(List.of(
                createOrderItem(6, "瑜伽垫", "粉色 8mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"),
                createOrderItem(7, "弹力带套装", "轻量级", 1, 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400"),
                createOrderItem(2, "运动毛巾", "粉色 S 码", 2, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400")
            ));
            o7.setTotalAmount(446.0);
            o7.setPointsUsed(0);
            o7.setActualPay(446.0);
            o7.setOrderDate("2026-03-20");
            o7.setStatus(ProductOrderStatus.DELIVERED);
            o7.setStatusText("已送达");
            o7.setTrackingNumber("SF1234567893");
            o7.setEstimatedDelivery("2026-03-23");
            productOrderRepository.save(o7);

            ProductOrder o8 = new ProductOrder();
            o8.setUserId(8);
            o8.setItems(List.of(
                createOrderItem(4, "BCAA 氨基酸", "300g 葡萄味", 2, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400")
            ));
            o8.setTotalAmount(398.0);
            o8.setPointsUsed(0);
            o8.setActualPay(398.0);
            o8.setOrderDate("2026-03-19");
            o8.setStatus(ProductOrderStatus.PENDING);
            o8.setStatusText("待付款");
            productOrderRepository.save(o8);

            ProductOrder o9 = new ProductOrder();
            o9.setUserId(9);
            o9.setItems(List.of(
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                createOrderItem(6, "瑜伽垫", "绿色 10mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400")
            ));
            o9.setTotalAmount(498.0);
            o9.setPointsUsed(300);
            o9.setActualPay(468.0);
            o9.setOrderDate("2026-03-18");
            o9.setStatus(ProductOrderStatus.DELIVERED);
            o9.setStatusText("已送达");
            o9.setTrackingNumber("SF1234567894");
            o9.setEstimatedDelivery("2026-03-21");
            productOrderRepository.save(o9);

            ProductOrder o10 = new ProductOrder();
            o10.setUserId(10);
            o10.setItems(List.of(
                createOrderItem(3, "运动水杯", "500ml 白色", 3, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400")
            ));
            o10.setTotalAmount(267.0);
            o10.setPointsUsed(0);
            o10.setActualPay(267.0);
            o10.setOrderDate("2026-03-17");
            o10.setStatus(ProductOrderStatus.PAID);
            o10.setStatusText("已付款");
            productOrderRepository.save(o10);

            ProductOrder o11 = new ProductOrder();
            o11.setUserId(11);
            o11.setItems(List.of(
                createOrderItem(7, "弹力带套装", "阻力组合装", 1, 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400"),
                createOrderItem(2, "运动毛巾", "紫色 L 码", 2, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400")
            ));
            o11.setTotalAmount(247.0);
            o11.setPointsUsed(50);
            o11.setActualPay(242.0);
            o11.setOrderDate("2026-03-16");
            o11.setStatus(ProductOrderStatus.SHIPPED);
            o11.setStatusText("已发货");
            o11.setTrackingNumber("SF1234567895");
            o11.setEstimatedDelivery("2026-03-19");
            productOrderRepository.save(o11);

            ProductOrder o12 = new ProductOrder();
            o12.setUserId(12);
            o12.setItems(List.of(
                createOrderItem(8, "左旋肉碱", "60 粒装", 2, 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"),
                createOrderItem(4, "BCAA 氨基酸", "300g 柠檬味", 1, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400")
            ));
            o12.setTotalAmount(717.0);
            o12.setPointsUsed(0);
            o12.setActualPay(717.0);
            o12.setOrderDate("2026-03-15");
            o12.setStatus(ProductOrderStatus.DELIVERED);
            o12.setStatusText("已送达");
            o12.setTrackingNumber("SF1234567896");
            o12.setEstimatedDelivery("2026-03-18");
            productOrderRepository.save(o12);

            ProductOrder o13 = new ProductOrder();
            o13.setUserId(13);
            o13.setItems(List.of(
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 3, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                createOrderItem(8, "左旋肉碱", "60 粒装", 2, 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"),
                createOrderItem(4, "BCAA 氨基酸", "300g 柠檬味", 2, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400")
            ));
            o13.setTotalAmount(1813.0);
            o13.setPointsUsed(500);
            o13.setActualPay(1763.0);
            o13.setOrderDate("2026-03-14");
            o13.setStatus(ProductOrderStatus.DELIVERED);
            o13.setStatusText("已送达");
            o13.setTrackingNumber("SF1234567897");
            o13.setEstimatedDelivery("2026-03-17");
            productOrderRepository.save(o13);

            ProductOrder o14 = new ProductOrder();
            o14.setUserId(14);
            o14.setItems(List.of(
                createOrderItem(2, "运动毛巾", "灰色 M 码", 1, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400")
            ));
            o14.setTotalAmount(59.0);
            o14.setPointsUsed(0);
            o14.setActualPay(59.0);
            o14.setOrderDate("2026-03-13");
            o14.setStatus(ProductOrderStatus.PENDING);
            o14.setStatusText("待付款");
            productOrderRepository.save(o14);

            ProductOrder o15 = new ProductOrder();
            o15.setUserId(15);
            o15.setItems(List.of(
                createOrderItem(6, "瑜伽垫", "蓝色 8mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"),
                createOrderItem(3, "运动水杯", "750ml 粉色", 1, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400")
            ));
            o15.setTotalAmount(288.0);
            o15.setPointsUsed(100);
            o15.setActualPay(188.0);
            o15.setOrderDate("2026-03-12");
            o15.setStatus(ProductOrderStatus.PAID);
            o15.setStatusText("已付款");
            productOrderRepository.save(o15);

            System.out.println("测试商品订单数据已创建：15 个订单");
        }
    }

    private void initHealthSurveyData() {
        long count = healthSurveyRepository.count();
        if (count == 0) {
            // 张三 - 金卡会员，以增肌为目标
            HealthSurvey s1 = new HealthSurvey();
            s1.setUserId(1);
            s1.setName("张三");
            s1.setGender("男");
            s1.setAge(28);
            s1.setHeight(178.0);
            s1.setWeight(75.0);
            s1.setGoal("增肌塑形");
            s1.setFrequency("每周 4-5 次");
            s1.setHealthIssues(List.of());
            s1.setNotes("有 2 年健身基础，希望增加肌肉量");
            healthSurveyRepository.save(s1);

            // 李四 - 银卡会员，减脂为主
            HealthSurvey s2 = new HealthSurvey();
            s2.setUserId(2);
            s2.setName("李四");
            s2.setGender("女");
            s2.setAge(25);
            s2.setHeight(163.0);
            s2.setWeight(60.0);
            s2.setGoal("减脂瘦身");
            s2.setFrequency("每周 3 次");
            s2.setHealthIssues(List.of("膝盖旧伤"));
            s2.setNotes("膝盖有旧伤，需要避免高强度跳跃动作");
            healthSurveyRepository.save(s2);

            // 王五 - 普通会员，初学者
            HealthSurvey s3 = new HealthSurvey();
            s3.setUserId(3);
            s3.setName("王五");
            s3.setGender("男");
            s3.setAge(32);
            s3.setHeight(172.0);
            s3.setWeight(80.0);
            s3.setGoal("减脂增肌");
            s3.setFrequency("每周 2 次");
            s3.setHealthIssues(List.of("轻度脂肪肝"));
            s3.setNotes("刚开始健身，需要从基础开始");
            healthSurveyRepository.save(s3);

            // 陈静 - 钻石会员，以瑜伽为主
            HealthSurvey s4 = new HealthSurvey();
            s4.setUserId(7);
            s4.setName("陈静");
            s4.setGender("女");
            s4.setAge(30);
            s4.setHeight(165.0);
            s4.setWeight(55.0);
            s4.setGoal("体态矫正");
            s4.setFrequency("每周 5 次");
            s4.setHealthIssues(List.of());
            s4.setNotes("长期伏案工作，肩颈酸痛，希望改善体态");
            healthSurveyRepository.save(s4);

            // 冯敏 - 钻石会员，功能训练
            HealthSurvey s5 = new HealthSurvey();
            s5.setUserId(13);
            s5.setName("冯敏");
            s5.setGender("男");
            s5.setAge(35);
            s5.setHeight(180.0);
            s5.setWeight(85.0);
            s5.setGoal("体能提升");
            s5.setFrequency("每周 5-6 次");
            s5.setHealthIssues(List.of());
            s5.setNotes("运动爱好者，希望全面提升综合体能");
            healthSurveyRepository.save(s5);

            // 郑浩 - 普通会员
            HealthSurvey s6 = new HealthSurvey();
            s6.setUserId(10);
            s6.setName("郑浩");
            s6.setGender("男");
            s6.setAge(22);
            s6.setHeight(175.0);
            s6.setWeight(68.0);
            s6.setGoal("增肌");
            s6.setFrequency("每周 3 次");
            s6.setHealthIssues(List.of());
            s6.setNotes("大学生，预算有限，希望性价比高的训练方案");
            healthSurveyRepository.save(s6);

            System.out.println("健康问卷数据已创建：6 份");
        }
    }

    private void initCoachScheduleSlotData() {
        long count = coachScheduleSlotRepository.count();
        if (count == 0) {
            // 教练1（李教练）的排班 - 2026-04-01 ~ 04-07
            saveSlot(1, "2026-04-01", "09:00", "10:00", true, "A1 训练室", null);
            saveSlot(1, "2026-04-01", "10:30", "11:30", true, "A1 训练室", null);
            saveSlot(1, "2026-04-01", "14:00", "15:00", false, "A2 训练室", null);
            saveSlot(1, "2026-04-01", "16:00", "17:00", true, "A1 训练室", null);
            saveSlot(1, "2026-04-02", "09:00", "10:00", true, "A1 训练室", null);
            saveSlot(1, "2026-04-02", "10:30", "11:30", true, "A1 训练室", null);
            saveSlot(1, "2026-04-02", "15:00", "16:00", true, "A2 训练室", null);
            saveSlot(1, "2026-04-03", "09:00", "10:00", false, "A1 训练室", null);
            saveSlot(1, "2026-04-03", "14:00", "15:00", true, "A1 训练室", null);
            saveSlot(1, "2026-04-04", "09:00", "10:00", true, "A1 训练室", null);
            saveSlot(1, "2026-04-04", "10:30", "11:30", true, "A1 训练室", null);
            saveSlot(1, "2026-04-05", "09:00", "10:00", true, "A1 训练室", null);
            saveSlot(1, "2026-04-07", "10:00", "11:00", true, "A1 训练室", null);

            // 教练2（王教练）的排班 - 瑜伽普拉提
            saveSlot(2, "2026-04-01", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-01", "10:00", "11:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-01", "15:00", "16:00", false, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-02", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-02", "10:00", "11:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-03", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-03", "15:00", "16:00", true, "B2 普拉提室", null);
            saveSlot(2, "2026-04-04", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-04", "10:00", "11:00", false, "B1 瑜伽室", null);

            // 教练3（张教练）的排班 - 减脂塑形
            saveSlot(3, "2026-04-01", "09:00", "10:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-01", "11:00", "12:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-02", "09:00", "10:00", false, "C1 有氧区", null);
            saveSlot(3, "2026-04-02", "14:00", "15:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-03", "09:00", "10:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-05", "09:00", "10:00", true, "C1 有氧区", null);

            // 教练4（刘教练）的排班 - 功能训练
            saveSlot(4, "2026-04-01", "10:00", "11:00", true, "D1 功能区", null);
            saveSlot(4, "2026-04-01", "14:00", "15:00", true, "D1 功能区", null);
            saveSlot(4, "2026-04-02", "10:00", "11:00", true, "D1 功能区", null);
            saveSlot(4, "2026-04-03", "10:00", "11:00", false, "D1 功能区", null);
            saveSlot(4, "2026-04-04", "10:00", "11:00", true, "D1 功能区", null);

            // 教练6（赵教练）的排班 - 有氧训练
            saveSlot(6, "2026-04-01", "07:00", "08:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-01", "18:00", "19:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-02", "07:00", "08:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-03", "07:00", "08:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-03", "18:00", "19:00", false, "E1 有氧教室", null);

            System.out.println("排班数据已创建：37 个排班时段");
        }
    }

    private void saveSlot(int coachId, String date, String startTime, String endTime, boolean available, String roomName, Integer bookingId) {
        CoachScheduleSlot slot = new CoachScheduleSlot();
        slot.setCoachId(coachId);
        slot.setDate(date);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setAvailable(available);
        slot.setRoomName(roomName);
        slot.setBookingId(bookingId);
        coachScheduleSlotRepository.save(slot);
    }

    private void initBookingData() {
        long count = bookingRepository.count();
        if (count == 0) {
            // 预约1：张三预约李教练 - 已确认
            Booking b1 = new Booking();
            b1.setUserId(1);
            b1.setCoachId(1);
            b1.setBookingDate("2026-04-01");
            b1.setStartTime("14:00");
            b1.setEndTime("15:00");
            b1.setLocation("A2 训练室");
            b1.setStatus(BookingStatus.CONFIRMED);
            b1.setStatusText("已确认");
            b1.setSource(BookingSource.CLIENT);
            b1.setPackageOrderId("2");
            bookingRepository.save(b1);

            // 预约2：李四预约王教练 - 已确认
            Booking b2 = new Booking();
            b2.setUserId(2);
            b2.setCoachId(2);
            b2.setBookingDate("2026-04-01");
            b2.setStartTime("15:00");
            b2.setEndTime("16:00");
            b2.setLocation("B1 瑜伽室");
            b2.setStatus(BookingStatus.CONFIRMED);
            b2.setStatusText("已确认");
            b2.setSource(BookingSource.CLIENT);
            b2.setPackageOrderId("2");
            bookingRepository.save(b2);

            // 预约3：王五预约张教练 - 待确认
            Booking b3 = new Booking();
            b3.setUserId(3);
            b3.setCoachId(3);
            b3.setBookingDate("2026-04-02");
            b3.setStartTime("09:00");
            b3.setEndTime("10:00");
            b3.setLocation("C1 有氧区");
            b3.setStatus(BookingStatus.PENDING);
            b3.setStatusText("待确认");
            b3.setSource(BookingSource.CLIENT);
            b3.setPackageOrderId("5");
            bookingRepository.save(b3);

            // 预约4：张三预约刘教练 - 已完成
            Booking b4 = new Booking();
            b4.setUserId(1);
            b4.setCoachId(4);
            b4.setBookingDate("2026-03-28");
            b4.setStartTime("10:00");
            b4.setEndTime("11:00");
            b4.setLocation("D1 功能区");
            b4.setStatus(BookingStatus.COMPLETED);
            b4.setStatusText("已完成");
            b4.setSource(BookingSource.CLIENT);
            b4.setPackageOrderId("2");
            bookingRepository.save(b4);

            // 预约5：赵六预约李教练 - 已确认
            Booking b5 = new Booking();
            b5.setUserId(4);
            b5.setCoachId(1);
            b5.setBookingDate("2026-04-03");
            b5.setStartTime("14:00");
            b5.setEndTime("15:00");
            b5.setLocation("A1 训练室");
            b5.setStatus(BookingStatus.CONFIRMED);
            b5.setStatusText("已确认");
            b5.setSource(BookingSource.CLIENT);
            b5.setPackageOrderId("2");
            bookingRepository.save(b5);

            // 预约6：李明预约王教练 - 已签到
            Booking b6 = new Booking();
            b6.setUserId(5);
            b6.setCoachId(2);
            b6.setBookingDate("2026-04-01");
            b6.setStartTime("08:00");
            b6.setEndTime("09:00");
            b6.setLocation("B1 瑜伽室");
            b6.setStatus(BookingStatus.CHECKED_IN);
            b6.setStatusText("已签到");
            b6.setSource(BookingSource.CLIENT);
            b6.setPackageOrderId("6");
            bookingRepository.save(b6);

            // 预约7：陈静预约赵教练 - 已确认
            Booking b7 = new Booking();
            b7.setUserId(7);
            b7.setCoachId(6);
            b7.setBookingDate("2026-04-01");
            b7.setStartTime("18:00");
            b7.setEndTime("19:00");
            b7.setLocation("E1 有氧教室");
            b7.setStatus(BookingStatus.CONFIRMED);
            b7.setStatusText("已确认");
            b7.setSource(BookingSource.CLIENT);
            b7.setPackageOrderId("2");
            bookingRepository.save(b7);

            // 预约8：冯敏预约李教练 - 已取消
            Booking b8 = new Booking();
            b8.setUserId(13);
            b8.setCoachId(1);
            b8.setBookingDate("2026-03-30");
            b8.setStartTime("09:00");
            b8.setEndTime("10:00");
            b8.setLocation("A1 训练室");
            b8.setStatus(BookingStatus.CANCELLED);
            b8.setStatusText("已取消");
            b8.setSource(BookingSource.CLIENT);
            b8.setPackageOrderId("2");
            bookingRepository.save(b8);

            // 预约9：孙丽预约张教练 - 教练代约
            Booking b9 = new Booking();
            b9.setUserId(11);
            b9.setCoachId(3);
            b9.setBookingDate("2026-04-05");
            b9.setStartTime("09:00");
            b9.setEndTime("10:00");
            b9.setLocation("C1 有氧区");
            b9.setStatus(BookingStatus.CONFIRMED);
            b9.setStatusText("已确认");
            b9.setSource(BookingSource.COACH_PROXY);
            b9.setPackageOrderId("2");
            bookingRepository.save(b9);

            // 预约10：王伟预约刘教练 - 已完成
            Booking b10 = new Booking();
            b10.setUserId(12);
            b10.setCoachId(4);
            b10.setBookingDate("2026-03-29");
            b10.setStartTime("14:00");
            b10.setEndTime("15:00");
            b10.setLocation("D1 功能区");
            b10.setStatus(BookingStatus.COMPLETED);
            b10.setStatusText("已完成");
            b10.setSource(BookingSource.CLIENT);
            b10.setPackageOrderId("2");
            bookingRepository.save(b10);

            // 更新对应的排班时段为已占用
            updateSlotBooking(1, "2026-04-01", "14:00", b1.getId());
            updateSlotBooking(2, "2026-04-01", "15:00", b2.getId());
            updateSlotBooking(3, "2026-04-02", "09:00", b3.getId());
            updateSlotBooking(4, "2026-03-28", "10:00", b4.getId());
            updateSlotBooking(1, "2026-04-03", "14:00", b5.getId());
            updateSlotBooking(2, "2026-04-01", "08:00", b6.getId());
            updateSlotBooking(6, "2026-04-01", "18:00", b7.getId());
            updateSlotBooking(6, "2026-04-05", "09:00", b9.getId());

            System.out.println("预约数据已创建：10 条预约记录");
        }
    }

    private void initNotifications() {
        try {
            List<NotificationItem> existing = notificationService.getAllNotifications();
            if (existing.isEmpty()) {
                // 系统通知
                NotificationItem n1 = new NotificationItem();
                n1.setReceiverUserId(null);
                n1.setType(NotificationType.SYSTEM);
                n1.setTitle("系统维护通知");
                n1.setContent("系统将于今晚 23:00 进行例行维护，预计持续 2 小时。");
                n1.setIsRead(false);
                n1.setActionLink("");
                notificationService.createNotification(n1);

                // 会员通知
                NotificationItem n2 = new NotificationItem();
                n2.setReceiverUserId(1);
                n2.setType(NotificationType.MEMBER);
                n2.setTitle("会员等级提升");
                n2.setContent("恭喜您升级为黄金会员，享受更多专属权益！");
                n2.setIsRead(false);
                notificationService.createNotification(n2);

                // 预约通知
                NotificationItem n3 = new NotificationItem();
                n3.setReceiverUserId(2);
                n3.setType(NotificationType.BOOKING);
                n3.setTitle("预约确认");
                n3.setContent("您的预约已成功确认，请按时到达。");
                n3.setIsRead(true);
                notificationService.createNotification(n3);

                System.out.println("通知数据已创建：3 条测试通知");
            }
        } catch (Exception e) {
            System.err.println("初始化通知数据失败：" + e.getMessage());
        }
    }

    private void updateSlotBooking(int coachId, String date, String startTime, int bookingId) {
        List<CoachScheduleSlot> slots = coachScheduleSlotRepository.findAllByCoachIdOrderByDateAscStartTimeAsc(coachId);
        for (CoachScheduleSlot slot : slots) {
            if (slot.getDate().equals(date) && slot.getStartTime().equals(startTime)) {
                slot.setAvailable(false);
                slot.setBookingId(bookingId);
                coachScheduleSlotRepository.save(slot);
                break;
            }
        }
    }

    private ProductOrderItem createOrderItem(Integer productId, String name, String specs, Integer quantity, Double price, String image) {
        ProductOrderItem item = new ProductOrderItem();
        item.setProductId(productId);
        item.setName(name);
        item.setSpecs(specs);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setImage(image);
        return item;
    }
}
