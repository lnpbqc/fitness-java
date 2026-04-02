package org.example.fitnessjava.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.*;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.PackageOrder;
import org.example.fitnessjava.pojo.PackageOrderStatus;
import org.example.fitnessjava.pojo.PackageType;
import org.example.fitnessjava.pojo.ProductOrder;
import org.example.fitnessjava.pojo.ProductOrderItem;
import org.example.fitnessjava.pojo.ProductOrderStatus;
import org.example.fitnessjava.pojo.SaleStatus;
import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.pojo.Product;
import org.example.fitnessjava.pojo.TicketStatus;
import org.example.fitnessjava.repository.CheckinTicketRepository;
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
    private PackageOrderRepository packageOrderRepository;

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

    @Resource
    private CheckinTicketRepository checkinTicketRepository;

    @PostConstruct
    public void init() {
        initAdminUser();
        initTestData();
        initCoachData();
        initBanners();
        initPackageData();
        initProductData();
        initPackageOrderData();
        initProductOrderData();
        initHealthSurveyData();
        initCoachScheduleSlotData();
        initBookingData();
        initCheckinTicketData();
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
            // 钻石会员 (3名)
            saveClient("oTest001", "张伟", "zhangwei", "13800138001", "M2026001", "钻石会员", 3500, 10, 156, "2027-06-30");
            saveClient("oTest002", "李娜", "lina", "13800138002", "M2026002", "钻石会员", 4200, 12, 203, "2027-09-30");
            saveClient("oTest003", "王强", "wangqiang", "13800138003", "M2026003", "钻石会员", 2800, 8, 98, "2027-03-31");

            // 金卡会员 (10名)
            saveClient("oTest004", "刘洋", "liuyang", "13900139004", "M2026004", "金卡会员", 1800, 6, 78, "2026-12-31");
            saveClient("oTest005", "陈静", "chenjing", "13900139005", "M2026005", "金卡会员", 2200, 7, 112, "2027-01-31");
            saveClient("oTest006", "赵敏", "zhaomin", "13900139006", "M2026006", "金卡会员", 1500, 5, 65, "2026-11-30");
            saveClient("oTest007", "孙浩", "sunhao", "13900139007", "M2026007", "金卡会员", 1950, 6, 89, "2026-12-15");
            saveClient("oTest008", "周琳", "zhoulin", "13900139008", "M2026008", "金卡会员", 1680, 4, 54, "2026-10-31");
            saveClient("oTest009", "吴超", "wuchao", "13900139009", "M2026009", "金卡会员", 2100, 8, 95, "2027-02-28");
            saveClient("oTest010", "郑刚", "zhenggang", "13900139010", "M2026010", "金卡会员", 1350, 3, 43, "2026-09-30");
            saveClient("oTest011", "冯丽", "fengli", "13900139011", "M2026011", "金卡会员", 1750, 5, 67, "2026-11-15");
            saveClient("oTest012", "褚勇", "chuyong", "13900139012", "M2026012", "金卡会员", 1580, 4, 52, "2026-10-15");
            saveClient("oTest013", "卫民", "weimin", "13900139013", "M2026013", "金卡会员", 1920, 6, 81, "2026-12-31");

            // 银卡会员 (12名)
            saveClient("oTest014", "蒋涛", "jiangtao", "13600136014", "M2026014", "银卡会员", 980, 3, 38, "2026-08-31");
            saveClient("oTest015", "沈华", "shenhua", "13600136015", "M2026015", "银卡会员", 750, 2, 25, "2026-07-31");
            saveClient("oTest016", "韩静", "hanjing", "13600136016", "M2026016", "银卡会员", 860, 2, 32, "2026-09-30");
            saveClient("oTest017", "杨帆", "yangfan", "13600136017", "M2026017", "银卡会员", 1100, 4, 45, "2026-10-31");
            saveClient("oTest018", "朱磊", "zhulei", "13600136018", "M2026018", "银卡会员", 680, 1, 18, "2026-06-30");
            saveClient("oTest019", "秦明", "qinming", "13600136019", "M2026019", "银卡会员", 920, 3, 36, "2026-08-15");
            saveClient("oTest020", "许刚", "xugang", "13600136020", "M2026020", "银卡会员", 1050, 3, 41, "2026-09-15");
            saveClient("oTest021", "何涛", "hetao", "13600136021", "M2026021", "银卡会员", 880, 2, 29, "2026-07-15");
            saveClient("oTest022", "吕娜", "lvna", "13600136022", "M2026022", "银卡会员", 790, 2, 24, "2026-08-31");
            saveClient("oTest023", "施磊", "shilei", "13600136023", "M2026023", "银卡会员", 650, 1, 15, "2026-06-15");
            saveClient("oTest024", "张敏", "zhangmin", "13600136024", "M2026024", "银卡会员", 820, 2, 28, "2026-07-31");
            saveClient("oTest025", "孔刚", "konggang", "13600136025", "M2026025", "银卡会员", 950, 3, 37, "2026-09-30");

            // 普通会员 (5名)
            saveClient("oTest026", "曹阳", "caoyang", "13700137026", "M2026026", "普通会员", 320, 1, 8, "2026-05-31");
            saveClient("oTest027", "严丽", "yanli", "13700137027", "M2026027", "普通会员", 180, 0, 3, "2026-04-30");
            saveClient("oTest028", "陆明", "luming", "13700137028", "M2026028", "普通会员", 450, 1, 12, "2026-06-30");
            saveClient("oTest029", "丁芳", "dingfang", "13700137029", "M2026029", "普通会员", 280, 0, 6, "2026-05-15");
            saveClient("oTest030", "尤伟", "youwei", "13700137030", "M2026030", "普通会员", 150, 0, 2, "2026-04-15");

            System.out.println("测试用户数据已创建：30 名用户");
        }
    }

    private void saveClient(String openid, String nickname, String seed, String phone, String memberNumber, String memberLevel, int points, int coupons, int trainingCount, String expireAt) {
        Client client = new Client();
        client.setOpenid(openid);
        client.setNickname(nickname);
        client.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + seed);
        client.setPhone(phone);
        client.setMemberNumber(memberNumber);
        client.setMemberLevel(memberLevel);
        client.setPoints(points);
        client.setCoupons(coupons);
        client.setTotalTrainingCount(trainingCount);
        client.setMembershipExpireAt(expireAt);
        clientRepository.save(client);
    }

    private void initCoachData() {
        long count = coachRepository.count();
        if (count == 0) {
            saveCoach("李教练", "licoach", "力量训练", "国家一级健身教练，NSCA-CPT 认证，运动营养师", 4.9, 5, 286, Arrays.asList("力量训练", "增肌", "塑形"), "13800138123", true, Coach.Status.ONLINE);
            saveCoach("王教练", "wangcoach", "瑜伽·普拉提", "RYT-500 瑜伽教练，普拉提认证教练", 4.8, 4, 234, Arrays.asList("瑜伽", "普拉提", "体态矫正"), "13900139456", true, Coach.Status.ONLINE);
            saveCoach("张教练", "zhangcoach", "减脂塑形", "AASFP 私人教练，运动康复师", 4.7, 4, 198, Arrays.asList("减脂", "塑形", "营养指导"), "13600136789", false, Coach.Status.BUSY);
            saveCoach("刘教练", "liucoach", "功能训练", "FMS 功能性训练认证，NASM-CPT", 4.6, 3, 156, Arrays.asList("功能训练", "运动康复", "体能提升"), "13700137321", false, Coach.Status.ONLINE);
            saveCoach("陈教练", "chencoach", "拳击", "国家拳击二级运动员，Boxfit 认证教练", 4.8, 4, 187, Arrays.asList("拳击", "有氧", "燃脂"), "13500135654", true, Coach.Status.OFFLINE);
            saveCoach("赵教练", "zhaocoach", "有氧训练", "ACE-CPT 认证教练，心肺康复训练师", 4.5, 3, 112, Arrays.asList("有氧", "核心训练", "体能"), "13700137003", false, Coach.Status.ONLINE);
            saveCoach("孙教练", "suncoach", "CROSSFIT", "CROSSFIT L2 认证教练，擅长高强度功能训练", 4.7, 4, 145, Arrays.asList("CROSSFIT", "HIIT", "综合体能"), "13700137004", false, Coach.Status.ONLINE);
            saveCoach("周教练", "zhoucoach", "游泳训练", "国家游泳运动员，擅长水中康复和游泳教学", 4.9, 5, 312, Arrays.asList("游泳", "水中康复", "心肺训练"), "13700137005", false, Coach.Status.BUSY);

            System.out.println("测试教练数据已创建：8 名教练");
        }
    }

    private void saveCoach(String nickname, String seed, String specialty, String description, double rating, int level, int classCount, List<String> tags, String phone, boolean featured, Coach.Status status) {
        Coach coach = new Coach();
        coach.setNickname(nickname);
        coach.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + seed);
        coach.setIntro(description.split("，")[0]);
        coach.setSpecialty(specialty);
        coach.setDescription(description);
        coach.setRating(rating);
        coach.setLevel(level);
        coach.setClassCount(classCount);
        coach.setTags(tags);
        coach.setPhone(phone);
        coach.setFeatured(featured);
        coach.setStatus(status);
        coachRepository.save(coach);
    }

    private void initPackageData() {
        long count = packageProductRepository.count();
        if (count == 0) {
            savePackage("月度健身卡", PackageType.TIME_CARD, 0, 30, 599.0, 699.0, 60, "适合有固定锻炼习惯的会员，畅享 30 天不限次数训练", SaleStatus.ON_SALE);
            savePackage("季度健身卡", PackageType.TIME_CARD, 0, 90, 1599.0, 1999.0, 160, "更长周期，更优价格，适合长期健身的会员", SaleStatus.ON_SALE);
            savePackage("年度健身卡", PackageType.TIME_CARD, 0, 365, 4999.0, 5999.0, 500, "全年畅练，性价比最高，适合健身爱好者", SaleStatus.ON_SALE);
            savePackage("10 次私教课", PackageType.SESSION_CARD, 10, 60, 2199.0, 2599.0, 220, "专业私教一对一指导，定制化训练方案", SaleStatus.ON_SALE);
            savePackage("20 次私教课", PackageType.SESSION_CARD, 20, 90, 3999.0, 4999.0, 400, "专业私教一对一指导，定制化训练方案", SaleStatus.ON_SALE);
            savePackage("50 次私教课", PackageType.SESSION_CARD, 50, 180, 8999.0, 10999.0, 900, "大量采购更优惠，适合高频训练学员", SaleStatus.ON_SALE);
            savePackage("体测评估服务", PackageType.ASSESSMENT, 1, 7, 199.0, null, 20, "专业体测设备，全方位了解身体状况", SaleStatus.ON_SALE);
            savePackage("体验课程", PackageType.EXPERIENCE, 1, 7, 9.9, null, 1, "新用户专享，超值体验价", SaleStatus.OFF_SALE);

            System.out.println("测试套餐数据已创建：8 个套餐");
        }
    }

    private void savePackage(String name, PackageType type, int sessions, int validDays, double price, Double originalPrice, int pointsReward, String description, SaleStatus saleStatus) {
        Package p = new Package();
        p.setName(name);
        p.setType(type);
        p.setSessions(sessions);
        p.setValidDays(validDays);
        p.setPrice(price);
        p.setOriginalPrice(originalPrice);
        p.setPointsReward(pointsReward);
        p.setDescription(description);
        p.setSaleStatus(saleStatus);
        packageProductRepository.save(p);
    }

    private void initProductData() {
        long count = productRepository.count();
        if (count == 0) {
            saveProduct("蛋白粉 - 巧克力味", "营养补剂", 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400", 2990, 30, "高品质乳清蛋白，增肌必备，每份含 25g 蛋白质", 156, SaleStatus.ON_SALE);
            saveProduct("蛋白粉 - 香草味", "营养补剂", 299.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400", 2990, 30, "高品质乳清蛋白，口感清新，每份含 25g 蛋白质", 89, SaleStatus.ON_SALE);
            saveProduct("BCAA 氨基酸", "营养补剂", 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400", 1990, 20, "支链氨基酸，帮助肌肉恢复，减少疲劳", 134, SaleStatus.ON_SALE);
            saveProduct("左旋肉碱", "营养补剂", 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400", 2590, 26, "帮助脂肪代谢，配合运动效果更佳", 78, SaleStatus.ON_SALE);
            saveProduct("运动毛巾", "运动配件", 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400", 590, 6, "速干吸水，柔软舒适，运动必备", 234, SaleStatus.ON_SALE);
            saveProduct("运动水杯", "运动配件", 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400", 890, 9, "便携大容量，食品级材质，安全健康", 312, SaleStatus.ON_SALE);
            saveProduct("健身手套", "运动配件", 79.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400", 790, 8, "防滑耐磨，保护手掌，提升训练体验", 145, SaleStatus.ON_SALE);
            saveProduct("瑜伽垫", "运动器材", 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400", 1990, 20, "加厚防滑，环保材质，瑜伽练习必备", 87, SaleStatus.ON_SALE);
            saveProduct("弹力带套装", "运动器材", 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400", 1290, 13, "多阻力级别，适合不同训练强度", 156, SaleStatus.ON_SALE);
            saveProduct("拳击手套", "运动器材", 189.0, "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 1890, 19, "专业拳击手套，防滑耐用，保护手部", 65, SaleStatus.ON_SALE);

            System.out.println("测试商品数据已创建：10 个商品");
        }
    }

    private void saveProduct(String name, String category, double price, String image, int pointsPrice, int pointsReward, String desc, int stock, SaleStatus saleStatus) {
        Product p = new Product();
        p.setName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setImage(image);
        p.setPointsPrice(pointsPrice);
        p.setPointsReward(pointsReward);
        p.setDesc(desc);
        p.setStock(stock);
        p.setSaleStatus(saleStatus);
        productRepository.save(p);
    }

    private void initPackageOrderData() {
        long count = packageOrderRepository.count();
        if (count == 0) {
            // ACTIVE 状态订单 - 进行中
            savePackageOrder(1, 5, "20 次私教课", PackageType.SESSION_CARD, 20, 8, 12, 90, "2026-03-01", "2026-05-30", "2026-03-01", 3999.0, 400, PackageOrderStatus.ACTIVE);
            savePackageOrder(2, 2, "季度健身卡", PackageType.TIME_CARD, 0, 0, 0, 90, "2026-03-15", "2026-06-13", "2026-03-15", 1599.0, 160, PackageOrderStatus.ACTIVE);
            savePackageOrder(4, 5, "20 次私教课", PackageType.SESSION_CARD, 20, 5, 15, 90, "2026-02-20", "2026-05-21", "2026-02-20", 3999.0, 400, PackageOrderStatus.ACTIVE);
            savePackageOrder(5, 6, "10 次私教课", PackageType.SESSION_CARD, 10, 3, 7, 60, "2026-03-10", "2026-05-09", "2026-03-10", 2199.0, 220, PackageOrderStatus.ACTIVE);
            savePackageOrder(7, 1, "月度健身卡", PackageType.TIME_CARD, 0, 0, 0, 30, "2026-03-20", "2026-04-19", "2026-03-20", 599.0, 60, PackageOrderStatus.ACTIVE);
            savePackageOrder(8, 5, "20 次私教课", PackageType.SESSION_CARD, 20, 12, 8, 90, "2026-01-10", "2026-04-10", "2026-01-10", 3999.0, 400, PackageOrderStatus.ACTIVE);
            savePackageOrder(9, 4, "10 次私教课", PackageType.SESSION_CARD, 10, 2, 8, 60, "2026-03-05", "2026-06-03", "2026-03-05", 2199.0, 220, PackageOrderStatus.ACTIVE);
            savePackageOrder(10, 3, "月度健身卡", PackageType.TIME_CARD, 0, 0, 0, 30, "2026-03-25", "2026-04-24", "2026-03-25", 599.0, 60, PackageOrderStatus.ACTIVE);
            savePackageOrder(11, 5, "20 次私教课", PackageType.SESSION_CARD, 20, 18, 2, 90, "2026-01-05", "2026-04-05", "2026-01-05", 3999.0, 400, PackageOrderStatus.ACTIVE);
            savePackageOrder(13, 7, "50 次私教课", PackageType.SESSION_CARD, 50, 15, 35, 180, "2026-02-01", "2026-07-31", "2026-02-01", 8999.0, 900, PackageOrderStatus.ACTIVE);
            savePackageOrder(14, 4, "10 次私教课", PackageType.SESSION_CARD, 10, 4, 6, 60, "2026-03-01", "2026-05-30", "2026-03-01", 2199.0, 220, PackageOrderStatus.ACTIVE);
            savePackageOrder(15, 1, "月度健身卡", PackageType.TIME_CARD, 0, 0, 0, 30, "2026-03-18", "2026-04-17", "2026-03-18", 599.0, 60, PackageOrderStatus.ACTIVE);
            savePackageOrder(16, 5, "20 次私教课", PackageType.SESSION_CARD, 20, 7, 13, 90, "2026-02-28", "2026-05-29", "2026-02-28", 3999.0, 400, PackageOrderStatus.ACTIVE);
            savePackageOrder(17, 2, "季度健身卡", PackageType.TIME_CARD, 0, 0, 0, 90, "2026-03-10", "2026-06-08", "2026-03-10", 1599.0, 160, PackageOrderStatus.ACTIVE);
            savePackageOrder(18, 6, "10 次私教课", PackageType.SESSION_CARD, 10, 1, 9, 60, "2026-03-22", "2026-05-21", "2026-03-22", 2199.0, 220, PackageOrderStatus.ACTIVE);
            savePackageOrder(19, 3, "月度健身卡", PackageType.TIME_CARD, 0, 0, 0, 30, "2026-03-15", "2026-04-14", "2026-03-15", 599.0, 60, PackageOrderStatus.ACTIVE);
            savePackageOrder(21, 8, "年度健身卡", PackageType.TIME_CARD, 0, 0, 0, 365, "2026-03-01", "2027-03-01", "2026-03-01", 4999.0, 500, PackageOrderStatus.ACTIVE);
            savePackageOrder(22, 5, "20 次私教课", PackageType.SESSION_CARD, 20, 6, 14, 90, "2026-03-08", "2026-06-06", "2026-03-08", 3999.0, 400, PackageOrderStatus.ACTIVE);
            savePackageOrder(24, 4, "10 次私教课", PackageType.SESSION_CARD, 10, 5, 5, 60, "2026-02-15", "2026-05-16", "2026-02-15", 2199.0, 220, PackageOrderStatus.ACTIVE);
            savePackageOrder(25, 2, "季度健身卡", PackageType.TIME_CARD, 0, 0, 0, 90, "2026-03-20", "2026-06-18", "2026-03-20", 1599.0, 160, PackageOrderStatus.ACTIVE);

            // COMPLETED 状态订单 - 已完成
            savePackageOrder(3, 8, "体测评估服务", PackageType.ASSESSMENT, 1, 1, 0, 7, "2026-03-10", "2026-03-17", "2026-03-10", 199.0, 20, PackageOrderStatus.COMPLETED);
            savePackageOrder(6, 8, "体测评估服务", PackageType.ASSESSMENT, 1, 1, 0, 7, "2026-03-05", "2026-03-12", "2026-03-05", 199.0, 20, PackageOrderStatus.COMPLETED);
            savePackageOrder(20, 8, "体测评估服务", PackageType.ASSESSMENT, 1, 1, 0, 7, "2026-02-28", "2026-03-07", "2026-02-28", 199.0, 20, PackageOrderStatus.COMPLETED);
            savePackageOrder(26, 8, "体测评估服务", PackageType.ASSESSMENT, 1, 1, 0, 7, "2026-01-15", "2026-01-22", "2026-01-15", 199.0, 20, PackageOrderStatus.COMPLETED);
            savePackageOrder(28, 8, "体测评估服务", PackageType.ASSESSMENT, 1, 1, 0, 7, "2026-02-01", "2026-02-08", "2026-02-01", 199.0, 20, PackageOrderStatus.COMPLETED);

            // EXPIRED 状态订单 - 已过期
            savePackageOrder(12, 1, "月度健身卡", PackageType.TIME_CARD, 0, 0, 0, 30, "2026-01-01", "2026-01-31", "2026-01-01", 599.0, 60, PackageOrderStatus.EXPIRED);
            savePackageOrder(23, 1, "月度健身卡", PackageType.TIME_CARD, 0, 0, 0, 30, "2025-12-01", "2025-12-31", "2025-12-01", 599.0, 60, PackageOrderStatus.EXPIRED);
            savePackageOrder(27, 3, "月度健身卡", PackageType.TIME_CARD, 0, 0, 0, 30, "2025-11-15", "2025-12-15", "2025-11-15", 599.0, 60, PackageOrderStatus.EXPIRED);

            // REFUNDING 状态订单 - 退款中 (用于测试首页待办事项)
            savePackageOrder(29, 5, "20 次私教课", PackageType.SESSION_CARD, 20, 10, 10, 90, "2026-02-10", "2026-05-11", "2026-02-10", 3999.0, 400, PackageOrderStatus.REFUNDING);
            savePackageOrder(30, 4, "10 次私教课", PackageType.SESSION_CARD, 10, 5, 5, 60, "2026-02-20", "2026-05-21", "2026-02-20", 2199.0, 220, PackageOrderStatus.REFUNDING);

            System.out.println("测试套餐订单数据已创建：30 个订单");
        }
    }

    private void savePackageOrder(int userId, int packageId, String packageName, PackageType type, int totalSessions, int usedSessions, int remainingSessions, int validDays, String startDate, String endDate, String purchaseDate, double price, int pointsReward, PackageOrderStatus status) {
        PackageOrder order = new PackageOrder();
        order.setUserId(userId);
        order.setPackageId(packageId);
        order.setPackageName(packageName);
        order.setType(type);
        order.setTotalSessions(totalSessions);
        order.setUsedSessions(usedSessions);
        order.setRemainingSessions(remainingSessions);
        order.setValidDays(validDays);
        order.setStartDate(startDate);
        order.setEndDate(endDate);
        order.setPurchaseDate(purchaseDate);
        order.setPrice(price);
        order.setPointsReward(pointsReward);
        order.setStatus(status);
        packageOrderRepository.save(order);
    }

    private void initProductOrderData() {
        long count = productOrderRepository.count();
        if (count == 0) {
            // PENDING (待付款) - 6条
            saveProductOrder(1, "2026-04-02", ProductOrderStatus.PENDING, "待付款", null, null, 0,
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0));
            saveProductOrder(5, "2026-04-02", ProductOrderStatus.PENDING, "待付款", null, null, 100,
                createOrderItem(3, "运动毛巾", "灰色 L 码", 2, 59.0));
            saveProductOrder(8, "2026-04-01", ProductOrderStatus.PENDING, "待付款", null, null, 0,
                createOrderItem(6, "瑜伽垫", "紫色 10mm", 1, 199.0));
            saveProductOrder(12, "2026-04-01", ProductOrderStatus.PENDING, "待付款", null, null, 200,
                createOrderItem(7, "弹力带套装", "阻力组合装", 1, 129.0));
            saveProductOrder(16, "2026-03-31", ProductOrderStatus.PENDING, "待付款", null, null, 0,
                createOrderItem(9, "拳击手套", "M 码 红色", 1, 189.0));
            saveProductOrder(22, "2026-03-31", ProductOrderStatus.PENDING, "待付款", null, null, 50,
                createOrderItem(2, "运动毛巾", "蓝色 M 码", 3, 59.0));

            // PAID (已付款待发货) - 12条 (用于首页待发货统计)
            saveProductOrder(2, "2026-04-01", ProductOrderStatus.PAID, "已付款", null, null, 0,
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 2, 299.0),
                createOrderItem(5, "运动水杯", "500ml 蓝色", 1, 89.0));
            saveProductOrder(6, "2026-04-01", ProductOrderStatus.PAID, "已付款", null, null, 200,
                createOrderItem(4, "BCAA 氨基酸", "300g 柠檬味", 2, 199.0));
            saveProductOrder(9, "2026-03-31", ProductOrderStatus.PAID, "已付款", null, null, 100,
                createOrderItem(8, "左旋肉碱", "60 粒装", 1, 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"),
                createOrderItem(3, "运动毛巾", "灰色 L 码", 2, 59.0));
            saveProductOrder(11, "2026-03-31", ProductOrderStatus.PAID, "已付款", null, null, 0,
                createOrderItem(6, "瑜伽垫", "粉色 8mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"));
            saveProductOrder(14, "2026-03-30", ProductOrderStatus.PAID, "已付款", null, null, 150,
                createOrderItem(7, "弹力带套装", "轻量级", 1, 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400"),
                createOrderItem(2, "运动毛巾", "粉色 S 码", 1, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400"));
            saveProductOrder(17, "2026-03-30", ProductOrderStatus.PAID, "已付款", null, null, 0,
                createOrderItem(1, "蛋白粉 - 香草味", "2kg 装", 1, 299.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"));
            saveProductOrder(19, "2026-03-29", ProductOrderStatus.PAID, "已付款", null, null, 300,
                createOrderItem(9, "拳击手套", "L 码 黑色", 2, 189.0));
            saveProductOrder(23, "2026-03-29", ProductOrderStatus.PAID, "已付款", null, null, 100,
                createOrderItem(5, "运动水杯", "750ml 黑色", 1, 89.0),
                createOrderItem(4, "BCAA 氨基酸", "300g 葡萄味", 1, 199.0));
            saveProductOrder(25, "2026-03-28", ProductOrderStatus.PAID, "已付款", null, null, 0,
                createOrderItem(6, "瑜伽垫", "绿色 10mm", 1, 199.0));
            saveProductOrder(27, "2026-03-28", ProductOrderStatus.PAID, "已付款", null, null, 200,
                createOrderItem(8, "左旋肉碱", "60 粒装", 2, 259.0));
            saveProductOrder(30, "2026-03-27", ProductOrderStatus.PAID, "已付款", null, null, 0,
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0),
                createOrderItem(7, "弹力带套装", "中量级", 1, 129.0));

            // SHIPPED (已发货) - 10条
            saveProductOrder(3, "2026-03-26", ProductOrderStatus.SHIPPED, "已发货", "SF1234567800", "2026-03-29", 100,
                createOrderItem(2, "运动毛巾", "灰色 L 码", 3, 59.0));
            saveProductOrder(7, "2026-03-25", ProductOrderStatus.SHIPPED, "已发货", "SF1234567801", "2026-03-28", 0,
                createOrderItem(6, "瑜伽垫", "蓝色 8mm", 1, 199.0),
                createOrderItem(7, "弹力带套装", "阻力组合装", 2, 129.0));
            saveProductOrder(10, "2026-03-24", ProductOrderStatus.SHIPPED, "已发货", "SF1234567802", "2026-03-27", 200,
                createOrderItem(4, "BCAA 氨基酸", "300g 柠檬味", 2, 199.0));
            saveProductOrder(13, "2026-03-23", ProductOrderStatus.SHIPPED, "已发货", "SF1234567803", "2026-03-26", 0,
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0),
                createOrderItem(8, "左旋肉碱", "60 粒装", 1, 259.0));
            saveProductOrder(15, "2026-03-22", ProductOrderStatus.SHIPPED, "已发货", "SF1234567804", "2026-03-25", 150,
                createOrderItem(5, "运动水杯", "500ml 白色", 2, 89.0),
                createOrderItem(2, "运动毛巾", "紫色 L 码", 2, 59.0));
            saveProductOrder(18, "2026-03-21", ProductOrderStatus.SHIPPED, "已发货", "SF1234567805", "2026-03-24", 100,
                createOrderItem(7, "弹力带套装", "轻量级", 1, 129.0),
                createOrderItem(3, "运动毛巾", "蓝色 M 码", 1, 59.0));
            saveProductOrder(20, "2026-03-20", ProductOrderStatus.SHIPPED, "已发货", "SF1234567806", "2026-03-23", 0,
                createOrderItem(9, "拳击手套", "M 码 红色", 1, 189.0));
            saveProductOrder(24, "2026-03-19", ProductOrderStatus.SHIPPED, "已发货", "SF1234567807", "2026-03-22", 300,
                createOrderItem(1, "蛋白粉 - 香草味", "2kg 装", 2, 299.0));
            saveProductOrder(26, "2026-03-18", ProductOrderStatus.SHIPPED, "已发货", "SF1234567808", "2026-03-21", 200,
                createOrderItem(6, "瑜伽垫", "粉色 8mm", 2, 199.0));
            saveProductOrder(28, "2026-03-17", ProductOrderStatus.SHIPPED, "已发货", "SF1234567809", "2026-03-20", 0,
                createOrderItem(4, "BCAA 氨基酸", "300g 葡萄味", 3, 199.0));

            // DELIVERED (已送达) - 10条
            saveProductOrder(4, "2026-03-16", ProductOrderStatus.DELIVERED, "已送达", "SF1234567810", "2026-03-19", 100,
                createOrderItem(8, "左旋肉碱", "60 粒装", 1, 259.0));
            saveProductOrder(29, "2026-03-15", ProductOrderStatus.DELIVERED, "已送达", "SF1234567811", "2026-03-18", 0,
                createOrderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0),
                createOrderItem(5, "运动水杯", "750ml 粉色", 1, 89.0));
            saveProductOrder(21, "2026-03-14", ProductOrderStatus.DELIVERED, "已送达", "SF1234567812", "2026-03-17", 200,
                createOrderItem(6, "瑜伽垫", "绿色 10mm", 1, 199.0),
                createOrderItem(7, "弹力带套装", "中量级", 1, 129.0));
            saveProductOrder(1, "2026-03-10", ProductOrderStatus.DELIVERED, "已送达", "SF1234567813", "2026-03-13", 0,
                createOrderItem(2, "运动毛巾", "灰色 L 码", 2, 59.0));
            saveProductOrder(8, "2026-03-08", ProductOrderStatus.DELIVERED, "已送达", "SF1234567814", "2026-03-11", 300,
                createOrderItem(4, "BCAA 氨基酸", "300g 柠檬味", 2, 199.0));
            saveProductOrder(14, "2026-03-05", ProductOrderStatus.DELIVERED, "已送达", "SF1234567815", "2026-03-08", 100,
                createOrderItem(7, "弹力带套装", "阻力组合装", 1, 129.0),
                createOrderItem(3, "运动毛巾", "蓝色 M 码", 2, 59.0));
            saveProductOrder(17, "2026-03-02", ProductOrderStatus.DELIVERED, "已送达", "SF1234567816", "2026-03-05", 0,
                createOrderItem(9, "拳击手套", "L 码 黑色", 1, 189.0));
            saveProductOrder(19, "2026-02-28", ProductOrderStatus.DELIVERED, "已送达", "SF1234567817", "2026-03-03", 200,
                createOrderItem(1, "蛋白粉 - 香草味", "2kg 装", 1, 299.0));
            saveProductOrder(23, "2026-02-25", ProductOrderStatus.DELIVERED, "已送达", "SF1234567818", "2026-02-28", 0,
                createOrderItem(6, "瑜伽垫", "蓝色 8mm", 1, 199.0));
            saveProductOrder(25, "2026-02-22", ProductOrderStatus.DELIVERED, "已送达", "SF1234567819", "2026-02-25", 150,
                createOrderItem(8, "左旋肉碱", "60 粒装", 2, 259.0));

            // CANCELLED (已退款) - 2条
            saveProductOrder(27, "2026-03-12", ProductOrderStatus.CANCELLED, "已退款：用户申请取消", null, null, 0,
                createOrderItem(5, "运动水杯", "500ml 白色", 1, 89.0));
            saveProductOrder(30, "2026-03-08", ProductOrderStatus.CANCELLED, "已退款：商品缺货", null, null, 100,
                createOrderItem(2, "运动毛巾", "灰色 L 码", 3, 59.0));

            System.out.println("测试商品订单数据已创建：40 个订单");
        }
    }

    private void saveProductOrder(int userId, String orderDate, ProductOrderStatus status, String statusText, String trackingNumber, String estimatedDelivery, int pointsUsed, ProductOrderItem... items) {
        ProductOrder order = new ProductOrder();
        order.setUserId(userId);
        order.setOrderDate(orderDate);
        order.setStatus(status);
        order.setStatusText(statusText);
        order.setTrackingNumber(trackingNumber);
        order.setEstimatedDelivery(estimatedDelivery);
        order.setPointsUsed(pointsUsed);
        order.setItems(Arrays.asList(items));

        double totalAmount = Arrays.stream(items).mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        order.setTotalAmount(totalAmount);
        order.setActualPay(totalAmount - pointsUsed / 10.0);

        productOrderRepository.save(order);
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
            // 教练1（李教练）的排班 - 2026-04-01 ~ 04-06
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(1, date, "09:00", "10:00", true, "A1 训练室", null);
                saveSlot(1, date, "10:30", "11:30", true, "A1 训练室", null);
                saveSlot(1, date, "14:00", "15:00", true, "A2 训练室", null);
                saveSlot(1, date, "15:30", "16:30", true, "A1 训练室", null);
            }

            // 教练2（王教练）的排班 - 瑜伽普拉提
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(2, date, "08:00", "09:00", true, "B1 瑜伽室", null);
                saveSlot(2, date, "10:00", "11:00", true, "B1 瑜伽室", null);
                saveSlot(2, date, "14:00", "15:00", true, "B1 瑜伽室", null);
                saveSlot(2, date, "15:30", "16:30", true, "B2 普拉提室", null);
            }

            // 教练3（张教练）的排班 - 减脂塑形
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(3, date, "09:00", "10:00", true, "C1 有氧区", null);
                saveSlot(3, date, "11:00", "12:00", true, "C1 有氧区", null);
                saveSlot(3, date, "14:00", "15:00", true, "C1 有氧区", null);
            }

            // 教练4（刘教练）的排班 - 功能训练
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(4, date, "10:00", "11:00", true, "D1 功能区", null);
                saveSlot(4, date, "14:00", "15:00", true, "D1 功能区", null);
                saveSlot(4, date, "16:00", "17:00", true, "D1 功能区", null);
            }

            // 教练5（陈教练）的排班 - 拳击
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(5, date, "10:00", "11:00", true, "E1 拳击室", null);
                saveSlot(5, date, "15:00", "16:00", true, "E1 拳击室", null);
                saveSlot(5, date, "19:00", "20:00", true, "E1 拳击室", null);
            }

            // 教练6（赵教练）的排班 - 有氧训练
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(6, date, "07:00", "08:00", true, "F1 有氧教室", null);
                saveSlot(6, date, "18:00", "19:00", true, "F1 有氧教室", null);
            }

            // 教练7（孙教练）的排班 - CROSSFIT
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(7, date, "09:00", "10:00", true, "F1 CROSSFIT 区", null);
                saveSlot(7, date, "14:00", "15:00", true, "F1 CROSSFIT 区", null);
                saveSlot(7, date, "19:00", "20:00", true, "F1 CROSSFIT 区", null);
            }

            // 教练8（周教练）的排班 - 游泳训练
            for (int day = 1; day <= 6; day++) {
                String date = "2026-04-0" + day;
                saveSlot(8, date, "06:00", "07:00", true, "G1 游泳池", null);
                saveSlot(8, date, "08:00", "09:00", true, "G1 游泳池", null);
                saveSlot(8, date, "16:00", "17:00", true, "G1 游泳池", null);
            }

            System.out.println("排班数据已创建：约 150 个排班时段");
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
            // PENDING (待确认) - 6条 (用于首页待确认预约统计)
            saveBooking(1, 1, "2026-04-02", "09:00", "10:00", "A1 训练室", BookingStatus.PENDING, "待确认", BookingSource.CLIENT, "1");
            saveBooking(4, 3, "2026-04-02", "10:30", "11:30", "C1 有氧区", BookingStatus.PENDING, "待确认", BookingSource.CLIENT, "4");
            saveBooking(6, 2, "2026-04-02", "14:00", "15:00", "B1 瑜伽室", BookingStatus.PENDING, "待确认", BookingSource.CLIENT, "5");
            saveBooking(9, 4, "2026-04-03", "09:00", "10:00", "D1 功能区", BookingStatus.PENDING, "待确认", BookingSource.CLIENT, "9");
            saveBooking(11, 5, "2026-04-03", "11:00", "12:00", "A2 训练室", BookingStatus.PENDING, "待确认", BookingSource.COACH_PROXY, "11");
            saveBooking(14, 7, "2026-04-03", "14:00", "15:00", "F1 CROSSFIT 区", BookingStatus.PENDING, "待确认", BookingSource.CLIENT, "14");

            // CONFIRMED (已确认) - 12条
            saveBooking(2, 1, "2026-04-01", "14:00", "15:00", "A2 训练室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "2");
            saveBooking(5, 2, "2026-04-01", "15:00", "16:00", "B1 瑜伽室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "5");
            saveBooking(7, 6, "2026-04-01", "18:00", "19:00", "E1 有氧教室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "7");
            saveBooking(8, 1, "2026-04-04", "09:00", "10:00", "A1 训练室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "8");
            saveBooking(10, 3, "2026-04-04", "10:30", "11:30", "C1 有氧区", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "10");
            saveBooking(13, 4, "2026-04-04", "14:00", "15:00", "D1 功能区", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "13");
            saveBooking(15, 8, "2026-04-05", "08:00", "09:00", "G1 游泳池", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "13");
            saveBooking(16, 2, "2026-04-05", "10:00", "11:00", "B2 普拉提室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "16");
            saveBooking(18, 5, "2026-04-05", "14:00", "15:00", "A1 训练室", BookingStatus.CONFIRMED, "已确认", BookingSource.COACH_PROXY, "18");
            saveBooking(19, 1, "2026-04-06", "09:00", "10:00", "A1 训练室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "1");
            saveBooking(21, 7, "2026-04-06", "14:00", "15:00", "F1 CROSSFIT 区", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "21");
            saveBooking(22, 3, "2026-04-06", "16:00", "17:00", "C1 有氧区", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "9");

            // CHECKED_IN (已签到) - 4条
            saveBooking(3, 2, "2026-04-01", "08:00", "09:00", "B1 瑜伽室", BookingStatus.CHECKED_IN, "已签到", BookingSource.CLIENT, "5");
            saveBooking(17, 4, "2026-04-01", "10:00", "11:00", "D1 功能区", BookingStatus.CHECKED_IN, "已签到", BookingSource.CLIENT, "16");
            saveBooking(20, 6, "2026-04-01", "07:00", "08:00", "E1 有氧教室", BookingStatus.CHECKED_IN, "已签到", BookingSource.CLIENT, "13");
            saveBooking(24, 8, "2026-04-01", "06:00", "07:00", "G1 游泳池", BookingStatus.CHECKED_IN, "已签到", BookingSource.CLIENT, "21");

            // COMPLETED (已完成) - 5条
            saveBooking(25, 1, "2026-03-31", "14:00", "15:00", "A1 训练室", BookingStatus.COMPLETED, "已完成", BookingSource.CLIENT, "1");
            saveBooking(12, 5, "2026-03-30", "10:00", "11:00", "A2 训练室", BookingStatus.COMPLETED, "已完成", BookingSource.CLIENT, "11");
            saveBooking(23, 3, "2026-03-29", "09:00", "10:00", "C1 有氧区", BookingStatus.COMPLETED, "已完成", BookingSource.CLIENT, "9");
            saveBooking(26, 7, "2026-03-28", "14:00", "15:00", "F1 CROSSFIT 区", BookingStatus.COMPLETED, "已完成", BookingSource.CLIENT, "13");
            saveBooking(28, 2, "2026-03-27", "15:00", "16:00", "B1 瑜伽室", BookingStatus.COMPLETED, "已完成", BookingSource.CLIENT, "5");

            // CANCELLED (已取消) - 3条
            saveBooking(29, 1, "2026-03-25", "10:00", "11:00", "A1 训练室", BookingStatus.CANCELLED, "已取消", BookingSource.CLIENT, "1");
            saveBooking(30, 4, "2026-03-20", "14:00", "15:00", "D1 功能区", BookingStatus.CANCELLED, "已取消", BookingSource.CLIENT, "16");
            saveBooking(27, 6, "2026-03-15", "18:00", "19:00", "E1 有氧教室", BookingStatus.CANCELLED, "已取消", BookingSource.COACH_PROXY, "7");

            System.out.println("预约数据已创建：30 条预约记录");
        }
    }

    private void saveBooking(int userId, int coachId, String bookingDate, String startTime, String endTime, String location, BookingStatus status, String statusText, BookingSource source, String packageOrderId) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCoachId(coachId);
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setLocation(location);
        booking.setStatus(status);
        booking.setStatusText(statusText);
        booking.setSource(source);
        booking.setPackageOrderId(packageOrderId);
        bookingRepository.save(booking);
    }

    private void initCheckinTicketData() {
        long count = checkinTicketRepository.count();
        if (count == 0) {
            // UNUSED (未使用) - 5条
            saveCheckinTicket("CHECKIN-UNUSED-1", 1, "张伟", "钻石会员", "力量训练", "2026-04-02 09:00", 8, TicketStatus.UNUSED);
            saveCheckinTicket("CHECKIN-UNUSED-2", 5, "陈静", "金卡会员", "瑜伽·普拉提", "2026-04-02 10:00", 5, TicketStatus.UNUSED);
            saveCheckinTicket("CHECKIN-UNUSED-3", 9, "吴超", "金卡会员", "减脂塑形", "2026-04-02 14:00", 6, TicketStatus.UNUSED);
            saveCheckinTicket("CHECKIN-UNUSED-4", 13, "卫民", "金卡会员", "CROSSFIT", "2026-04-03 09:00", 30, TicketStatus.UNUSED);
            saveCheckinTicket("CHECKIN-UNUSED-5", 17, "杨帆", "银卡会员", "功能训练", "2026-04-03 14:00", 4, TicketStatus.UNUSED);

            // USED (已使用) - 12条
            saveCheckinTicket("CHECKIN-USED-1", 1, "张伟", "钻石会员", "力量训练", "2026-03-28 14:00", 10, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-2", 3, "王强", "钻石会员", "减脂塑形", "2026-03-27 10:00", 5, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-3", 5, "陈静", "金卡会员", "瑜伽·普拉提", "2026-03-26 15:00", 3, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-4", 7, "孙浩", "金卡会员", "有氧训练", "2026-03-25 18:00", 8, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-5", 10, "郑刚", "金卡会员", "减脂塑形", "2026-03-24 09:00", 4, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-6", 13, "卫民", "金卡会员", "CROSSFIT", "2026-03-23 14:00", 25, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-7", 15, "沈华", "银卡会员", "游泳训练", "2026-03-22 08:00", 6, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-8", 18, "朱磊", "银卡会员", "拳击", "2026-03-21 19:00", 3, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-9", 20, "许刚", "银卡会员", "功能训练", "2026-03-20 10:00", 7, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-10", 22, "吕娜", "银卡会员", "瑜伽·普拉提", "2026-03-19 15:00", 5, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-11", 25, "尤伟", "普通会员", "游泳训练", "2026-03-18 06:00", 2, TicketStatus.USED);
            saveCheckinTicket("CHECKIN-USED-12", 28, "丁芳", "普通会员", "力量训练", "2026-03-17 14:00", 4, TicketStatus.USED);

            // EXPIRED (已过期) - 3条
            saveCheckinTicket("CHECKIN-EXPIRED-1", 2, "李娜", "钻石会员", "瑜伽·普拉提", "2026-03-10 10:00", 3, TicketStatus.EXPIRED);
            saveCheckinTicket("CHECKIN-EXPIRED-2", 6, "赵敏", "金卡会员", "力量训练", "2026-03-05 14:00", 5, TicketStatus.EXPIRED);
            saveCheckinTicket("CHECKIN-EXPIRED-3", 11, "冯丽", "金卡会员", "CROSSFIT", "2026-03-01 09:00", 10, TicketStatus.EXPIRED);

            System.out.println("核销记录数据已创建：20 条");
        }
    }

    private void saveCheckinTicket(String qrCode, int memberId, String memberName, String memberLevel, String classType, String scheduledTime, int sessionsLeft, TicketStatus status) {
        CheckinTicket ticket = new CheckinTicket();
        ticket.setQrCode(qrCode);
        ticket.setMemberId(memberId);
        ticket.setMemberName(memberName);
        ticket.setMemberAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + memberName);
        ticket.setClassType(classType);
        ticket.setScheduledTime(scheduledTime);
        ticket.setSessionsLeft(sessionsLeft);
        ticket.setStatus(status);
        checkinTicketRepository.save(ticket);
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
        item.setImage(image != null ? image : "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400");
        return item;
    }

    private ProductOrderItem createOrderItem(Integer productId, String name, String specs, Integer quantity, Double price) {
        return createOrderItem(productId, name, specs, quantity, price, null);
    }
}
