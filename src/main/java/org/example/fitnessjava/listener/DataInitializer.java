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

    @Resource
    private BodyAssessmentRecordRepository bodyAssessmentRecordRepository;

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
        initBodyAssessmentData();
    }

    private final List<String> banners = Arrays.asList(
            "https://images.unsplash.com/photo-1659081442183-38e43365d911?w=800",
            "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800",
            "https://images.unsplash.com/photo-1761971975769-97e598bf526b?w=800"
    );

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
            createClient("o9Nev67lfstCOQ0ZN63B_LcrAngA", "lnpbqc", "zhangsan", "13800138001", "M2026001", "金卡会员", 1200, 3, 45, "2026-12-31", "男", 28, "2025-06-15", Arrays.asList("增肌", "力量训练", "高活跃"));
            createClient("oTest002", "李四", "lisi", "13900139002", "M2026002", "银卡会员", 650, 1, 23, "2026-06-30", "女", 25, "2025-09-20", Arrays.asList("减脂", "瑜伽"));
            createClient("oTest003", "王五", "wangwu", "13600136003", "M2026003", "普通会员", 320, 0, 8, "2026-04-30", "男", 32, "2026-01-10", Arrays.asList("减脂"));
            createClient("oTest004", "赵六", "zhaoliu", "13500135004", "M2026004", "银卡会员", 780, 2, 31, "2026-08-31", "男", 29, "2025-08-05", Arrays.asList("综合训练", "体能"));
            createClient("oTest005", "李明", "liming", "13400134005", "M2026005", "金卡会员", 1580, 4, 56, "2026-12-31", "男", 26, "2025-04-12", Arrays.asList("力量训练", "增肌", "高活跃"));
            createClient("oTest006", "刘强", "liuqiang", "13300133006", "M2026006", "普通会员", 150, 0, 3, "2026-05-31", "男", 24, "2026-02-20", List.of());
            createClient("oTest007", "陈静", "chenjing", "13200132007", "M2026007", "钻石会员", 2580, 6, 89, "2027-03-31", "女", 30, "2024-11-01", Arrays.asList("瑜伽", "普拉提", "体态矫正", "高活跃"));
            createClient("oTest008", "周杰", "zhoujie", "13100131008", "M2026008", "银卡会员", 520, 1, 15, "2026-07-31", "男", 27, "2025-10-15", Arrays.asList("有氧", "减脂"));
            createClient("oTest009", "吴梅", "wumei", "13000130009", "M2026009", "金卡会员", 1350, 3, 42, "2026-11-30", "女", 31, "2025-05-18", Arrays.asList("塑形", "营养指导"));
            createClient("oTest010", "郑浩", "zhenghao", "13700137010", "M2026010", "普通会员", 280, 0, 6, "2026-05-15", "男", 22, "2026-01-25", Arrays.asList("增肌"));
            createClient("oTest011", "孙丽", "sunli", "13800138011", "M2026011", "银卡会员", 890, 2, 28, "2026-09-30", "女", 26, "2025-07-22", Arrays.asList("瑜伽", "拉伸"));
            createClient("oTest012", "王伟", "wangwei", "13900139012", "M2026012", "金卡会员", 1680, 5, 67, "2027-01-31", "男", 33, "2025-03-08", Arrays.asList("力量训练", "体能提升"));
            createClient("oTest013", "冯敏", "fengmin", "13600136013", "M2026013", "钻石会员", 3200, 8, 120, "2027-06-30", "男", 35, "2024-06-20", Arrays.asList("功能训练", "体能提升", "高活跃"));
            createClient("oTest014", "陈晨", "chenchen", "13500135014", "M2026014", "普通会员", 95, 0, 2, "2026-04-15", "男", 20, "2026-03-01", List.of());
            createClient("oTest015", "褚雪", "chuxue", "13400134015", "M2026015", "银卡会员", 720, 2, 25, "2026-08-15", "女", 28, "2025-08-12", Arrays.asList("减脂", "有氧"));
            System.out.println("测试用户数据已创建：15 名用户");
        }
    }

    private void initCoachData() {
        long count = coachRepository.count();
        if (count == 0) {
            createCoach("李教练", "licoach", "专注力量训练 8 年，擅长制定个性化训练计划，帮助学员突破瓶颈期。", "力量训练", "国家一级健身教练，NSCA-CPT 认证，运动营养师", 4.9, 5, 234, Arrays.asList("力量训练", "增肌", "塑形"), "13800138123", true, Coach.Status.ONLINE, null);
            createCoach("王教练", "wangcoach", "瑜伽与普拉提资深教练，注重身心平衡，帮助学员改善体态。", "瑜伽·普拉提", "RYT-500 瑜伽教练，普拉提认证教练", 4.8, 4, 189, Arrays.asList("瑜伽", "普拉提", "体态矫正"), "13900139456", true, Coach.Status.ONLINE, null);
            createCoach("张教练", "zhangcoach", "专注减脂塑形领域，科学制定饮食与训练计划，效果显著。", "减脂塑形", "AASFP 私人教练，运动康复师", 4.7, 4, 156, Arrays.asList("减脂", "塑形", "营养指导"), "13600136789", false, Coach.Status.BUSY, null);
            createCoach("刘教练", "liucoach", "功能训练专家，帮助学员提升运动表现和日常活动能力。", "功能训练", "FMS 功能性训练认证，NASM-CPT", 4.6, 3, 98, Arrays.asList("功能训练", "运动康复", "体能提升"), "13700137321", false, Coach.Status.ONLINE, null);
            createCoach("陈教练", "chencoach", "拳击专业教练，结合有氧训练帮助学员快速燃脂和提升体能。", "拳击", "国家拳击二级运动员，Boxfit 认证教练", 4.8, 4, 145, Arrays.asList("拳击", "有氧", "燃脂"), "13500135654", true, Coach.Status.OFFLINE, null);
            createCoach("赵教练", "zhaocoach", "有氧与核心训练教练，注重体能全面提升和心肺功能改善。", "有氧训练", "ACE-CPT 认证教练，心肺康复训练师", 4.5, 3, 72, Arrays.asList("有氧", "核心训练", "体能"), "13700137003", false, Coach.Status.ONLINE, "oCoach001");
            System.out.println("测试教练数据已创建：6 名教练");
        }
    }

    private void initPackageData() {
        long count = packageProductRepository.count();
        if (count == 0) {
            createPackage("月度健身卡", PackageType.TIME_CARD, 0, 30, 599.0, 699.0, 60, "适合有固定锻炼习惯的会员，畅享 30 天不限次数训练", SaleStatus.ON_SALE);
            createPackage("20 次私教课", PackageType.SESSION_CARD, 20, 90, 3999.0, 4999.0, 400, "专业私教一对一指导，定制化训练方案", SaleStatus.ON_SALE);
            createPackage("体测评估服务", PackageType.ASSESSMENT, 1, 7, 199.0, null, 20, "专业体测设备，全方位了解身体状况", SaleStatus.ON_SALE);
            createPackage("季度健身卡", PackageType.TIME_CARD, 0, 90, 1599.0, 1999.0, 160, "更长周期，更优价格，适合长期健身的会员", SaleStatus.ON_SALE);
            createPackage("体验课程", PackageType.EXPERIENCE, 1, 7, 9.9, null, 1, "新用户专享，超值体验价", SaleStatus.OFF_SALE);
            System.out.println("测试套餐数据已创建：5 个套餐");
        }
    }

    private void initProductData() {
        long count = productRepository.count();
        if (count == 0) {
            createProduct("蛋白粉 - 巧克力味", "营养补剂", 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400", 2990, 30, "高品质乳清蛋白，增肌必备，每份含 25g 蛋白质", 156, SaleStatus.ON_SALE);
            createProduct("运动毛巾", "运动配件", 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400", 590, 6, "速干吸水，柔软舒适，运动必备", 89, SaleStatus.ON_SALE);
            createProduct("运动水杯", "运动配件", 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400", 890, 9, "便携大容量，食品级材质，安全健康", 234, SaleStatus.ON_SALE);
            createProduct("BCAA 氨基酸", "营养补剂", 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400", 1990, 20, "支链氨基酸，帮助肌肉恢复，减少疲劳", 67, SaleStatus.ON_SALE);
            createProduct("健身手套", "运动配件", 79.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400", 790, 8, "防滑耐磨，保护手掌，提升训练体验", 0, SaleStatus.OFF_SALE);
            createProduct("瑜伽垫", "运动器材", 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400", 1990, 20, "加厚防滑，环保材质，瑜伽练习必备", 45, SaleStatus.ON_SALE);
            createProduct("弹力带套装", "运动器材", 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400", 1290, 13, "多阻力级别，适合不同训练强度", 78, SaleStatus.ON_SALE);
            createProduct("左旋肉碱", "营养补剂", 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400", 2590, 26, "帮助脂肪代谢，配合运动效果更佳", 92, SaleStatus.ON_SALE);
            System.out.println("测试商品数据已创建：8 个商品");
        }
    }

    private void initCourseOrderData() {
        long count = courseOrderRepository.count();
        if (count == 0) {
            createCourseOrder(1, 2, "20 次私教课", PackageType.SESSION_CARD, 20, 8, 90, "2026-03-01", "2026-05-30", "2026-03-01", 3999.0, 0, 3999.0, 400, CourseOrderStatus.ACTIVE);
            createCourseOrder(2, 1, "月度健身卡", PackageType.TIME_CARD, 0, 0, 30, "2026-03-15", "2026-04-14", "2026-03-15", 599.0, 100, 499.0, 60, CourseOrderStatus.ACTIVE);
            createCourseOrder(1, 4, "季度健身卡", PackageType.TIME_CARD, 0, 0, 90, "2026-02-01", "2026-05-01", "2026-02-01", 1599.0, 0, 1599.0, 160, CourseOrderStatus.ACTIVE);
            createCourseOrder(3, 3, "体测评估服务", PackageType.ASSESSMENT, 1, 1, 7, "2026-03-20", "2026-03-27", "2026-03-20", 199.0, 50, 149.0, 20, CourseOrderStatus.COMPLETED);
            createCourseOrder(4, 5, "体验课程", PackageType.EXPERIENCE, 1, 0, 7, "2026-03-25", "2026-04-01", "2026-03-25", 9.9, 0, 9.9, 1, CourseOrderStatus.ACTIVE);
            createCourseOrder(5, 2, "20 次私教课", PackageType.SESSION_CARD, 20, 15, 90, "2026-01-15", "2026-04-15", "2026-01-15", 3999.0, 200, 3799.0, 400, CourseOrderStatus.REFUNDING);
            System.out.println("测试课程订单数据已创建：6 个订单");
        }
    }

    private void initProductOrderData() {
        long count = productOrderRepository.count();
        if (count == 0) {
            createProductOrder(1, 687.0, 0, 69, 687.0, "2026-03-26", ProductOrderStatus.PAID, "已付款", null, null,
                    orderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 2, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                    orderItem(3, "运动水杯", "500ml 蓝色", 1, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400"));

            createProductOrder(2, 177.0, 100, 18, 167.0, "2026-03-25", ProductOrderStatus.SHIPPED, "已发货", "SF1234567890", "2026-03-28",
                    orderItem(2, "运动毛巾", "灰色 L 码", 3, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400"));

            createProductOrder(3, 457.0, 200, 46, 437.0, "2026-03-24", ProductOrderStatus.DELIVERED, "已送达", "SF1234567891", "2026-03-27",
                    orderItem(6, "瑜伽垫", "紫色 10mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"),
                    orderItem(7, "弹力带套装", "阻力组合装", 2, 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400"));

            createProductOrder(4, 199.0, 0, 20, 199.0, "2026-03-23", ProductOrderStatus.CANCELLED, "已退款：用户申请退款", null, null,
                    orderItem(4, "BCAA 氨基酸", "300g 柠檬味", 1, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"));

            createProductOrder(5, 558.0, 0, 56, 558.0, "2026-03-22", ProductOrderStatus.PAID, "已付款", null, null,
                    orderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                    orderItem(8, "左旋肉碱", "60 粒装", 1, 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"));

            createProductOrder(6, 296.0, 150, 30, 146.0, "2026-03-21", ProductOrderStatus.SHIPPED, "已发货", "SF1234567892", "2026-03-24",
                    orderItem(3, "运动水杯", "750ml 黑色", 2, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400"),
                    orderItem(2, "运动毛巾", "蓝色 M 码", 2, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400"));

            createProductOrder(7, 446.0, 0, 45, 446.0, "2026-03-20", ProductOrderStatus.DELIVERED, "已送达", "SF1234567893", "2026-03-23",
                    orderItem(6, "瑜伽垫", "粉色 8mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"),
                    orderItem(7, "弹力带套装", "轻量级", 1, 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400"),
                    orderItem(2, "运动毛巾", "粉色 S 码", 2, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400"));

            createProductOrder(8, 398.0, 0, 40, 398.0, "2026-03-19", ProductOrderStatus.PENDING, "待付款", null, null,
                    orderItem(4, "BCAA 氨基酸", "300g 葡萄味", 2, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"));

            createProductOrder(9, 498.0, 300, 50, 468.0, "2026-03-18", ProductOrderStatus.DELIVERED, "已送达", "SF1234567894", "2026-03-21",
                    orderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 1, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                    orderItem(6, "瑜伽垫", "绿色 10mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"));

            createProductOrder(10, 267.0, 0, 27, 267.0, "2026-03-17", ProductOrderStatus.PAID, "已付款", null, null,
                    orderItem(3, "运动水杯", "500ml 白色", 3, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400"));

            createProductOrder(11, 247.0, 50, 25, 242.0, "2026-03-16", ProductOrderStatus.SHIPPED, "已发货", "SF1234567895", "2026-03-19",
                    orderItem(7, "弹力带套装", "阻力组合装", 1, 129.0, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400"),
                    orderItem(2, "运动毛巾", "紫色 L 码", 2, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400"));

            createProductOrder(12, 717.0, 0, 72, 717.0, "2026-03-15", ProductOrderStatus.DELIVERED, "已送达", "SF1234567896", "2026-03-18",
                    orderItem(8, "左旋肉碱", "60 粒装", 2, 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"),
                    orderItem(4, "BCAA 氨基酸", "300g 柠檬味", 1, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"));

            createProductOrder(13, 1813.0, 500, 182, 1763.0, "2026-03-14", ProductOrderStatus.DELIVERED, "已送达", "SF1234567897", "2026-03-17",
                    orderItem(1, "蛋白粉 - 巧克力味", "2kg 装", 3, 299.0, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
                    orderItem(8, "左旋肉碱", "60 粒装", 2, 259.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"),
                    orderItem(4, "BCAA 氨基酸", "300g 柠檬味", 2, 199.0, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"));

            createProductOrder(14, 59.0, 0, 6, 59.0, "2026-03-13", ProductOrderStatus.PENDING, "待付款", null, null,
                    orderItem(2, "运动毛巾", "灰色 M 码", 1, 59.0, "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400"));

            createProductOrder(15, 288.0, 100, 29, 188.0, "2026-03-12", ProductOrderStatus.PAID, "已付款", null, null,
                    orderItem(6, "瑜伽垫", "蓝色 8mm", 1, 199.0, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400"),
                    orderItem(3, "运动水杯", "750ml 粉色", 1, 89.0, "https://images.unsplash.com/photo-1602143407151-01114192003f?w=400"));

            System.out.println("测试商品订单数据已创建：15 个订单");
        }
    }

    private void initHealthSurveyData() {
        long count = healthSurveyRepository.count();
        if (count == 0) {
            createHealthSurvey(1, "张三", "男", 28, 178.0, 75.0, "增肌塑形", "每周 4-5 次", List.of(), "有 2 年健身基础，希望增加肌肉量");
            createHealthSurvey(2, "李四", "女", 25, 163.0, 60.0, "减脂瘦身", "每周 3 次", Arrays.asList("膝盖旧伤"), "膝盖有旧伤，需要避免高强度跳跃动作");
            createHealthSurvey(3, "王五", "男", 32, 172.0, 80.0, "减脂增肌", "每周 2 次", Arrays.asList("轻度脂肪肝"), "刚开始健身，需要从基础开始");
            createHealthSurvey(7, "陈静", "女", 30, 165.0, 55.0, "体态矫正", "每周 5 次", List.of(), "长期伏案工作，肩颈酸痛，希望改善体态");
            createHealthSurvey(13, "冯敏", "男", 35, 180.0, 85.0, "体能提升", "每周 5-6 次", List.of(), "运动爱好者，希望全面提升综合体能");
            createHealthSurvey(10, "郑浩", "男", 22, 175.0, 68.0, "增肌", "每周 3 次", List.of(), "大学生，预算有限，希望性价比高的训练方案");
            System.out.println("健康问卷数据已创建：6 份");
        }
    }

    private void initCoachScheduleSlotData() {
        long count = coachScheduleSlotRepository.count();
        if (count == 0) {
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

            saveSlot(2, "2026-04-01", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-01", "10:00", "11:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-01", "15:00", "16:00", false, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-02", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-02", "10:00", "11:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-03", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-03", "15:00", "16:00", true, "B2 普拉提室", null);
            saveSlot(2, "2026-04-04", "08:00", "09:00", true, "B1 瑜伽室", null);
            saveSlot(2, "2026-04-04", "10:00", "11:00", false, "B1 瑜伽室", null);

            saveSlot(3, "2026-04-01", "09:00", "10:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-01", "11:00", "12:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-02", "09:00", "10:00", false, "C1 有氧区", null);
            saveSlot(3, "2026-04-02", "14:00", "15:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-03", "09:00", "10:00", true, "C1 有氧区", null);
            saveSlot(3, "2026-04-05", "09:00", "10:00", true, "C1 有氧区", null);

            saveSlot(4, "2026-04-01", "10:00", "11:00", true, "D1 功能区", null);
            saveSlot(4, "2026-04-01", "14:00", "15:00", true, "D1 功能区", null);
            saveSlot(4, "2026-04-02", "10:00", "11:00", true, "D1 功能区", null);
            saveSlot(4, "2026-04-03", "10:00", "11:00", false, "D1 功能区", null);
            saveSlot(4, "2026-04-04", "10:00", "11:00", true, "D1 功能区", null);

            saveSlot(6, "2026-04-01", "07:00", "08:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-01", "18:00", "19:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-02", "07:00", "08:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-03", "07:00", "08:00", true, "E1 有氧教室", null);
            saveSlot(6, "2026-04-03", "18:00", "19:00", false, "E1 有氧教室", null);

            System.out.println("排班数据已创建：37 个排班时段");
        }
    }

    private void initBookingData() {
        long count = bookingRepository.count();
        if (count == 0) {
            createBooking(1, 1, "2026-04-01", "14:00", "15:00", "A2 训练室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "2");
            createBooking(2, 2, "2026-04-01", "15:00", "16:00", "B1 瑜伽室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "2");
            createBooking(3, 3, "2026-04-02", "09:00", "10:00", "C1 有氧区", BookingStatus.PENDING, "待确认", BookingSource.CLIENT, "5");
            createBooking(1, 4, "2026-03-28", "10:00", "11:00", "D1 功能区", BookingStatus.COMPLETED, "已完成", BookingSource.CLIENT, "2");
            createBooking(4, 1, "2026-04-03", "14:00", "15:00", "A1 训练室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "2");
            createBooking(5, 2, "2026-04-01", "08:00", "09:00", "B1 瑜伽室", BookingStatus.CHECKED_IN, "已签到", BookingSource.CLIENT, "6");
            createBooking(7, 6, "2026-04-01", "18:00", "19:00", "E1 有氧教室", BookingStatus.CONFIRMED, "已确认", BookingSource.CLIENT, "2");
            createBooking(13, 1, "2026-03-30", "09:00", "10:00", "A1 训练室", BookingStatus.CANCELLED, "已取消", BookingSource.CLIENT, "2");
            createBooking(11, 3, "2026-04-05", "09:00", "10:00", "C1 有氧区", BookingStatus.CONFIRMED, "已确认", BookingSource.COACH_PROXY, "2");
            createBooking(12, 4, "2026-03-29", "14:00", "15:00", "D1 功能区", BookingStatus.COMPLETED, "已完成", BookingSource.CLIENT, "2");

            updateSlotBooking(1, "2026-04-01", "14:00", 1);
            updateSlotBooking(2, "2026-04-01", "15:00", 2);
            updateSlotBooking(3, "2026-04-02", "09:00", 3);
            updateSlotBooking(4, "2026-03-28", "10:00", 4);
            updateSlotBooking(1, "2026-04-03", "14:00", 5);
            updateSlotBooking(2, "2026-04-01", "08:00", 6);
            updateSlotBooking(6, "2026-04-01", "18:00", 7);
            updateSlotBooking(6, "2026-04-05", "09:00", 9);

            System.out.println("预约数据已创建：10 条预约记录");
        }
    }

    private void initNotifications() {
        try {
            List<NotificationItem> existing = notificationService.getAllNotifications();
            if (existing.isEmpty()) {
                createNotification(null, NotificationType.SYSTEM, "系统维护通知", "系统将于今晚 23:00 进行例行维护，预计持续 2 小时。", false, "");
                createNotification(1, NotificationType.MEMBER, "会员等级提升", "恭喜您升级为黄金会员，享受更多专属权益！", false, null);
                createNotification(2, NotificationType.BOOKING, "预约确认", "您的预约已成功确认，请按时到达。", true, null);
                System.out.println("通知数据已创建：3 条测试通知");
            }
        } catch (Exception e) {
            System.err.println("初始化通知数据失败：" + e.getMessage());
        }
    }

    private void initBodyAssessmentData() {
        long count = bodyAssessmentRecordRepository.count();
        if (count == 0) {
            createBodyAssessment(1, 1, "2026-03-01", 178.0, 75.0, 22.0, 45.0, 23.7, 8.0, 95.0, 85.0, 98.0, 35.0, 35.5, 55.0, 55.5, 38.0, 38.5, "初始体测，目标减脂增肌");
            createBodyAssessment(1, 1, "2026-03-15", 178.0, 74.0, 21.0, 45.5, 23.4, 7.5, 96.0, 83.0, 97.5, 35.5, 36.0, 55.5, 56.0, 38.5, 39.0, "两周后复测，体脂下降明显");
            createBodyAssessment(1, 1, "2026-03-28", 178.0, 73.2, 20.0, 46.0, 23.1, 7.0, 97.0, 81.0, 97.0, 36.0, 36.5, 56.0, 56.5, 39.0, 39.5, "效果持续，继续保持");
            createBodyAssessment(2, 2, "2026-03-05", 163.0, 60.0, 28.0, 35.0, 22.6, 6.0, null, 70.0, 90.0, null, null, null, null, null, null, "瑜伽减脂计划开始");
            createBodyAssessment(2, 2, "2026-03-25", 163.0, 58.5, 26.5, 35.5, 22.0, 5.5, null, 68.0, 88.0, null, null, null, null, null, null, "体重下降，体态改善");
            createBodyAssessment(7, 2, "2026-03-10", 165.0, 55.0, 20.0, 38.0, 20.2, 4.0, null, 65.0, 88.0, null, null, null, null, null, null, "体态矫正评估");
            System.out.println("体测数据已创建：6 条记录");
        }
    }

    // ==================== Helper Methods ====================

    private void createClient(String openid, String nickname, String avatarSeed, String phone, String memberNumber, String memberLevel, int points, int coupons, int trainingCount, String expireDate, String gender, int age, String joinDate, List<String> tags) {
        Client client = new Client();
        client.setOpenid(openid);
        client.setNickname(nickname);
        client.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + avatarSeed);
        client.setPhone(phone);
        client.setMemberNumber(memberNumber);
        client.setMemberLevel(memberLevel);
        client.setPoints(points);
        client.setCoupons(coupons);
        client.setTotalTrainingCount(trainingCount);
        client.setMembershipExpireAt(expireDate);
        client.setGender(gender);
        client.setAge(age);
        client.setJoinDate(joinDate);
        client.setTags(tags);
        clientRepository.save(client);
        System.out.println("测试用户已创建：" + nickname + " (" + memberLevel + ")");
    }

    private void createCoach(String nickname, String avatarSeed, String intro, String specialty, String description, double rating, int level, int classCount, List<String> tags, String phone, boolean featured, Coach.Status status, String openid) {
        Coach coach = new Coach();
        coach.setOpenid(openid);
        coach.setNickname(nickname);
        coach.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + avatarSeed);
        coach.setIntro(intro);
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

    private void createPackage(String name, PackageType type, int sessions, int validDays, double price, Double originalPrice, int pointsReward, String description, SaleStatus saleStatus) {
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

    private void createProduct(String name, String category, double price, String image, int pointsPrice, int pointsReward, String desc, int stock, SaleStatus saleStatus) {
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

    private void createCourseOrder(int userId, int packageId, String packageName, PackageType type, int totalSessions, int usedSessions, int validDays, String startDate, String endDate, String purchaseDate, double price, int pointsUsed, double actualPay, int pointsReward, CourseOrderStatus status) {
        CourseOrder o = new CourseOrder();
        o.setUserId(userId);
        o.setPackageId(packageId);
        o.setPackageName(packageName);
        o.setType(type);
        o.setTotalSessions(totalSessions);
        o.setUsedSessions(usedSessions);
        o.setRemainingSessions(totalSessions - usedSessions);
        o.setValidDays(validDays);
        o.setStartDate(startDate);
        o.setEndDate(endDate);
        o.setPurchaseDate(purchaseDate);
        o.setPrice(price);
        o.setPointsUsed(pointsUsed);
        o.setActualPay(actualPay);
        o.setPointsReward(pointsReward);
        o.setStatus(status);
        courseOrderRepository.save(o);
    }

    private void createProductOrder(int userId, double totalAmount, int pointsUsed, int pointsReward, double actualPay, String orderDate, ProductOrderStatus status, String statusText, String trackingNumber, String estimatedDelivery, ProductOrderItem... items) {
        ProductOrder o = new ProductOrder();
        o.setUserId(userId);
        o.setItems(List.of(items));
        o.setTotalAmount(totalAmount);
        o.setPointsUsed(pointsUsed);
        o.setPointsReward(pointsReward);
        o.setActualPay(actualPay);
        o.setOrderDate(orderDate);
        o.setStatus(status);
        o.setStatusText(statusText);
        o.setTrackingNumber(trackingNumber);
        o.setEstimatedDelivery(estimatedDelivery);
        productOrderRepository.save(o);
    }

    private void createHealthSurvey(int userId, String name, String gender, int age, double height, double weight, String goal, String frequency, List<String> healthIssues, String notes) {
        HealthSurvey s = new HealthSurvey();
        s.setUserId(userId);
        s.setName(name);
        s.setGender(gender);
        s.setAge(age);
        s.setHeight(height);
        s.setWeight(weight);
        s.setGoal(goal);
        s.setFrequency(frequency);
        s.setHealthIssues(healthIssues);
        s.setNotes(notes);
        healthSurveyRepository.save(s);
    }

    private void createBooking(int userId, int coachId, String bookingDate, String startTime, String endTime, String location, BookingStatus status, String statusText, BookingSource source, String packageOrderId) {
        Booking b = new Booking();
        b.setUserId(userId);
        b.setCoachId(coachId);
        b.setBookingDate(bookingDate);
        b.setStartTime(startTime);
        b.setEndTime(endTime);
        b.setLocation(location);
        b.setStatus(status);
        b.setStatusText(statusText);
        b.setSource(source);
        b.setPackageOrderId(packageOrderId);
        bookingRepository.save(b);
    }

    private void createNotification(Integer receiverUserId, NotificationType type, String title, String content, boolean isRead, String actionLink) {
        NotificationItem n = new NotificationItem();
        n.setReceiverUserId(receiverUserId);
        n.setType(type);
        n.setTitle(title);
        n.setContent(content);
        n.setIsRead(isRead);
        n.setActionLink(actionLink);
        notificationService.createNotification(n);
    }

    private void createBodyAssessment(int userId, int coachId, String date, double height, double weight, double bodyFat, double muscleMass, double bmi, double visceralFat, Double chest, Double waist, Double hips, Double leftArm, Double rightArm, Double leftThigh, Double rightThigh, Double leftCalf, Double rightCalf, String notes) {
        BodyAssessmentRecord r = new BodyAssessmentRecord();
        r.setUserId(userId);
        r.setCoachId(coachId);
        r.setDate(date);
        r.setHeight(height);
        r.setWeight(weight);
        r.setBodyFat(bodyFat);
        r.setMuscleMass(muscleMass);
        r.setBmi(bmi);
        r.setVisceralFat(visceralFat);
        r.setChest(chest);
        r.setWaist(waist);
        r.setHips(hips);
        r.setLeftArm(leftArm);
        r.setRightArm(rightArm);
        r.setLeftThigh(leftThigh);
        r.setRightThigh(rightThigh);
        r.setLeftCalf(leftCalf);
        r.setRightCalf(rightCalf);
        r.setNotes(notes);
        bodyAssessmentRecordRepository.save(r);
    }

    private ProductOrderItem orderItem(Integer productId, String name, String specs, Integer quantity, Double price, String image) {
        ProductOrderItem item = new ProductOrderItem();
        item.setProductId(productId);
        item.setName(name);
        item.setSpecs(specs);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setImage(image);
        return item;
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
}
