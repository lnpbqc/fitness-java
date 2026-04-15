package org.example.fitnessjava.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.*;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.Package;
import org.example.fitnessjava.service.AdminUserService;
import org.example.fitnessjava.service.BannerService;
import org.example.fitnessjava.service.NotificationService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataInitializer {

    private static final String DEMO_CLIENT_OPENID = "0";
    private static final String DEMO_COACH_OPENID = "0";

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
    private CoachWithUserRepository coachWithUserRepository;
    @Resource
    private BookingRepository bookingRepository;
    @Resource
    private BookingCoachScheduleSlotRepository bookingCoachScheduleSlotRepository;
    @Resource
    private CheckinTicketRepository checkinTicketRepository;
    @Resource
    private NotificationService notificationService;
    @Resource
    private BodyAssessmentRecordRepository bodyAssessmentRecordRepository;
    @Resource
    private TrainingRecordRepository trainingRecordRepository;

    @PostConstruct
    public void init() {
        initAdminUser();

        Map<String, Client> clients = initClients();
        Map<String, Coach> coaches = initCoaches();

        initBanners();
        Map<String, Package> packages = initPackages();
        Map<String, Product> products = initProducts();

        initCoachWithUser(clients, coaches);
        Map<String, PackageOrder> packageOrders = initPackageOrders(clients, packages);
        initProductOrders(clients, products);
        initHealthSurveys(clients);

        Map<String, CoachScheduleSlot> slotIndex = initCoachScheduleSlots(coaches);
        List<Booking> bookings = initBookings(clients, coaches, packageOrders);
        initBookingSlotRelations(bookings, slotIndex);
        syncSlotUsage(slotIndex);

        initCheckinTickets(bookings, clients, packageOrders);
        initBodyAssessments(clients, coaches);
        initTrainingRecords(clients, coaches);
        initNotifications(clients.get("demo"));
    }

    private void initAdminUser() {
        if (adminUserService.existsByUsername("admin")) {
            return;
        }
        AdminUser superAdmin = new AdminUser();
        superAdmin.setUsername("admin");
        superAdmin.setPassword("admin123");
        superAdmin.setNickname("SuperAdmin");
        superAdmin.setRole(AdminRole.SUPER_ADMIN);
        superAdmin.setEnabled(true);
        adminUserService.createUser(superAdmin);
    }

    private Map<String, Client> initClients() {
        Map<String, Client> clients = new HashMap<>();
        clients.put("demo", ensureClient(
                DEMO_CLIENT_OPENID, "lnpbqc", "zhangsan", "13800138001",
                "M2026001", "GOLD", 1280, 3, 52,
                LocalDate.now().plusMonths(8).toString(), "MALE", 28,
                "2025-06-15", Arrays.asList("muscle-gain", "strength", "active")));
        clients.put("alice", ensureClient(
                "oTestAlice", "Alice", "alice", "13900010001",
                "M2026002", "SILVER", 720, 1, 24,
                LocalDate.now().plusMonths(4).toString(), "FEMALE", 26,
                "2025-09-20", Arrays.asList("fat-loss", "pilates")));
        clients.put("bob", ensureClient(
                "oTestBob", "Bob", "bob", "13900010002",
                "M2026003", "NORMAL", 260, 0, 8,
                LocalDate.now().plusMonths(2).toString(), "MALE", 31,
                "2026-01-10", Collections.singletonList("fitness")));
        clients.put("cathy", ensureClient(
                "oTestCathy", "Cathy", "cathy", "13900010003",
                "M2026004", "DIAMOND", 2320, 5, 86,
                LocalDate.now().plusMonths(12).toString(), "FEMALE", 30,
                "2024-11-01", Arrays.asList("yoga", "posture")));
        return clients;
    }

    private Map<String, Coach> initCoaches() {
        Map<String, Coach> coaches = new HashMap<>();
        coaches.put("demoCoach", ensureCoach(
                DEMO_COACH_OPENID, "Coach Li", "licoach", "13800138123",
                "Strength and hypertrophy coach", "Strength", "NSCA-CPT, 8 years experience",
                4.9, 5, 260, Arrays.asList("strength", "hypertrophy", "shape"),
                true, true, Coach.Status.ONLINE));
        coaches.put("yogaCoach", ensureCoach(
                null, "Coach Wang", "wangcoach", "13900139456",
                "Yoga and posture specialist", "Yoga & Pilates", "RYT-500 and rehab background",
                4.8, 4, 188, Arrays.asList("yoga", "pilates", "posture"),
                true, true, Coach.Status.ONLINE));
        coaches.put("fatLossCoach", ensureCoach(
                null, "Coach Zhang", "zhangcoach", "13600136789",
                "Fat-loss and nutrition planning", "Fat-loss", "AASFP certification",
                4.7, 4, 156, Arrays.asList("fat-loss", "nutrition"),
                false, true, Coach.Status.BUSY));
        return coaches;
    }

    private void initBanners() {
        if (!bannerService.getBanners().isEmpty()) {
            return;
        }
        List<String> banners = Arrays.asList(
                "https://images.unsplash.com/photo-1659081442183-38e43365d911?w=800",
                "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800",
                "https://images.unsplash.com/photo-1605296867304-46d5465a13f1?w=800"
        );
        for (String image : banners) {
            Banner banner = new Banner();
            banner.setImage(image);
            bannerService.addBanner(banner);
        }
    }

    private Map<String, Package> initPackages() {
        Map<String, Package> packages = new HashMap<>();
        packages.put("monthlyCard", ensurePackage("Monthly Pass", PackageType.TIME_CARD, 0, 30, 599.0, 699.0, 60,
                "30 days unlimited", SaleStatus.ON_SALE));
        packages.put("session20", ensurePackage("20 PT Sessions", PackageType.SESSION_CARD, 20, 90, 3999.0, 4999.0, 400,
                "One-on-one PT package", SaleStatus.ON_SALE));
        packages.put("assessment", ensurePackage("Body Assessment", PackageType.ASSESSMENT, 1, 7, 199.0, null, 20,
                "Initial assessment", SaleStatus.ON_SALE));
        packages.put("seasonCard", ensurePackage("Quarter Pass", PackageType.TIME_CARD, 0, 90, 1599.0, 1999.0, 160,
                "90 days unlimited", SaleStatus.ON_SALE));
        return packages;
    }

    private Map<String, Product> initProducts() {
        Map<String, Product> products = new HashMap<>();
        products.put("whey", ensureProduct("Whey Protein Chocolate", "Supplement", 299.0,
                "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400",
                2990, 30, "25g protein per serving", 180, SaleStatus.ON_SALE));
        products.put("towel", ensureProduct("Sport Towel", "Accessory", 59.0,
                "https://images.unsplash.com/photo-1516762689617-e1cffcef479d?w=400",
                590, 6, "Quick dry towel", 120, SaleStatus.ON_SALE));
        products.put("mat", ensureProduct("Yoga Mat", "Equipment", 199.0,
                "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400",
                1990, 20, "Anti-slip and thick", 68, SaleStatus.ON_SALE));
        return products;
    }

    private void initCoachWithUser(Map<String, Client> clients, Map<String, Coach> coaches) {
        ensureCoachBinding(coaches.get("demoCoach"), clients.get("demo"));
        ensureCoachBinding(coaches.get("demoCoach"), clients.get("alice"));
        ensureCoachBinding(coaches.get("yogaCoach"), clients.get("cathy"));
        ensureCoachBinding(coaches.get("fatLossCoach"), clients.get("bob"));
    }

    private Map<String, PackageOrder> initPackageOrders(Map<String, Client> clients, Map<String, Package> packages) {
        Map<String, PackageOrder> orders = new HashMap<>();
        LocalDate today = LocalDate.now();

        Client demo = clients.get("demo");
        Package session20 = packages.get("session20");
        orders.put("demoSession", ensurePackageOrder(
                demo.getId(), session20, today.minusDays(15), today.plusDays(75), today.minusDays(15),
                20, 6, 120, PackageOrderStatus.ACTIVE));

        Client alice = clients.get("alice");
        Package monthly = packages.get("monthlyCard");
        orders.put("aliceTime", ensurePackageOrder(
                alice.getId(), monthly, today.minusDays(10), today.plusDays(20), today.minusDays(10),
                0, 0, 80, PackageOrderStatus.ACTIVE));

        Client cathy = clients.get("cathy");
        Package assessment = packages.get("assessment");
        orders.put("cathyAssessment", ensurePackageOrder(
                cathy.getId(), assessment, today.minusDays(20), today.minusDays(13), today.minusDays(20),
                1, 1, 30, PackageOrderStatus.COMPLETED));

        return orders;
    }

    private void initProductOrders(Map<String, Client> clients, Map<String, Product> products) {
        LocalDate today = LocalDate.now();
        Client demo = clients.get("demo");
        ensureProductOrder(
                demo.getId(), today.minusDays(2).toString(), ProductOrderStatus.PAID, "PAID", null, null, 100,
                Arrays.asList(
                        buildOrderItem(products.get("whey"), "2kg", 1),
                        buildOrderItem(products.get("towel"), "gray", 2)
                )
        );
        ensureProductOrder(
                clients.get("alice").getId(), today.minusDays(5).toString(), ProductOrderStatus.DELIVERED, "DELIVERED",
                "SF1234567001", today.minusDays(2).toString(), 0,
                Collections.singletonList(buildOrderItem(products.get("mat"), "purple 10mm", 1))
        );
    }

    private void initHealthSurveys(Map<String, Client> clients) {
        ensureHealthSurvey(clients.get("demo"), "MALE", 28, 178.0, 75.0,
                "muscle gain", "4-5 times/week", Collections.emptyList(), "Has one year basic training");
        ensureHealthSurvey(clients.get("alice"), "FEMALE", 26, 164.0, 58.0,
                "fat loss", "3 times/week", Collections.singletonList("mild knee issue"), "Avoid high impact jumps");
        ensureHealthSurvey(clients.get("cathy"), "FEMALE", 30, 166.0, 54.0,
                "posture improvement", "5 times/week", Collections.emptyList(), "Long desk-work schedule");
    }

    private Map<String, CoachScheduleSlot> initCoachScheduleSlots(Map<String, Coach> coaches) {
        LocalDate today = LocalDate.now();
        String todayStr = today.toString();
        String tomorrowStr = today.plusDays(1).toString();
        String twoDaysLaterStr = today.plusDays(2).toString();
        String yesterdayStr = today.minusDays(1).toString();

        Coach demoCoach = coaches.get("demoCoach");
        Coach yogaCoach = coaches.get("yogaCoach");
        Coach fatLossCoach = coaches.get("fatLossCoach");

        ensureSlot(demoCoach.getId(), tomorrowStr, "09:00", "10:00", "A1", CoachScheduleSlot.ScheduleType.PRIVATE, 1);
        ensureSlot(demoCoach.getId(), tomorrowStr, "14:00", "15:00", "A1", CoachScheduleSlot.ScheduleType.PRIVATE, 1);
        ensureSlot(demoCoach.getId(), yesterdayStr, "10:00", "11:00", "A2", CoachScheduleSlot.ScheduleType.PRIVATE, 1);

        ensureSlot(yogaCoach.getId(), todayStr, "08:00", "09:00", "B1", CoachScheduleSlot.ScheduleType.TEAM, 8);
        ensureSlot(yogaCoach.getId(), tomorrowStr, "10:00", "11:00", "B1", CoachScheduleSlot.ScheduleType.TEAM, 10);
        ensureSlot(yogaCoach.getId(), twoDaysLaterStr, "15:00", "16:00", "B2", CoachScheduleSlot.ScheduleType.TEAM, 6);

        ensureSlot(fatLossCoach.getId(), tomorrowStr, "18:00", "19:00", "C1", CoachScheduleSlot.ScheduleType.TEAM, 12);

        return buildSlotIndex();
    }

    private List<Booking> initBookings(Map<String, Client> clients,
                                       Map<String, Coach> coaches,
                                       Map<String, PackageOrder> packageOrders) {
        LocalDate today = LocalDate.now();

        Client demo = clients.get("demo");
        Coach demoCoach = coaches.get("demoCoach");
        Coach yogaCoach = coaches.get("yogaCoach");

        List<Booking> created = new ArrayList<>();
        created.add(ensureBooking(
                demo.getId(), demoCoach.getId(),
                today.plusDays(1).toString(), "14:00", "15:00", "A1",
                BookingStatus.CONFIRMED, BookingSource.CLIENT, packageOrders.get("demoSession")));

        created.add(ensureBooking(
                demo.getId(), demoCoach.getId(),
                today.minusDays(1).toString(), "10:00", "11:00", "A2",
                BookingStatus.CHECKED_IN, BookingSource.CLIENT, packageOrders.get("demoSession")));

        created.add(ensureBooking(
                clients.get("alice").getId(), yogaCoach.getId(),
                today.plusDays(1).toString(), "10:00", "11:00", "B1",
                BookingStatus.PENDING, BookingSource.CLIENT, packageOrders.get("aliceTime")));

        return created;
    }

    private void initBookingSlotRelations(List<Booking> bookings, Map<String, CoachScheduleSlot> slotIndex) {
        for (Booking booking : bookings) {
            String key = slotKey(booking.getCoachId(), booking.getBookingDate(), booking.getStartTime());
            CoachScheduleSlot slot = slotIndex.get(key);
            if (slot == null) {
                continue;
            }
            BookingCoachScheduleSlot mapping = bookingCoachScheduleSlotRepository.findByBookingId(booking.getId());
            if (mapping == null) {
                mapping = new BookingCoachScheduleSlot();
                mapping.setBookingId(booking.getId());
            }
            mapping.setCoachScheduleSlotId(slot.getId());
            bookingCoachScheduleSlotRepository.save(mapping);
        }
    }

    private void syncSlotUsage(Map<String, CoachScheduleSlot> slotIndex) {
        List<Booking> allBookings = bookingRepository.findAll();

        for (CoachScheduleSlot slot : slotIndex.values()) {
            slot.setActual(0);
            int expected = slot.getExpected() == null ? 0 : slot.getExpected();
            slot.setAvailable(expected > 0);
        }

        for (Booking booking : allBookings) {
            if (booking.getStatus() == BookingStatus.CANCELLED) {
                continue;
            }
            String key = slotKey(booking.getCoachId(), booking.getBookingDate(), booking.getStartTime());
            CoachScheduleSlot slot = slotIndex.get(key);
            if (slot == null) {
                continue;
            }
            if (slot.getType() == CoachScheduleSlot.ScheduleType.PRIVATE) {
                slot.setAvailable(false);
                continue;
            }
            int actual = slot.getActual() == null ? 0 : slot.getActual();
            int expected = slot.getExpected() == null ? 0 : slot.getExpected();
            actual++;
            slot.setActual(actual);
            slot.setAvailable(actual < expected);
        }

        coachScheduleSlotRepository.saveAll(slotIndex.values());
    }

    private void initCheckinTickets(List<Booking> bookings,
                                    Map<String, Client> clients,
                                    Map<String, PackageOrder> packageOrders) {
        Map<Integer, Client> clientById = clients.values().stream()
                .collect(Collectors.toMap(Client::getId, c -> c));
        Map<Integer, PackageOrder> packageOrderById = packageOrders.values().stream()
                .collect(Collectors.toMap(PackageOrder::getId, o -> o, (a, b) -> a));

        for (Booking booking : bookings) {
            if (checkinTicketRepository.findById(booking.getId()).isPresent()) {
                continue;
            }

            Client client = clientById.get(booking.getUserId());
            if (client == null) {
                continue;
            }

            PackageOrder order = parseOrderId(booking.getPackageOrderId())
                    .map(packageOrderById::get)
                    .orElse(null);

            CheckinTicket ticket = new CheckinTicket();
            ticket.setBookingId(booking.getId());
            ticket.setQrCode("MEMBER_QR:" + client.getId());
            ticket.setMemberId(client.getId());
            ticket.setMemberName(client.getNickname());
            ticket.setMemberAvatar(client.getAvatar());
            ticket.setClassType("session");
            ticket.setScheduledTime(booking.getBookingDate() + " " + booking.getStartTime());
            ticket.setSessionsLeft(order == null ? 0 : nonNull(order.getRemainingSessions()));
            ticket.setStatus((booking.getStatus() == BookingStatus.CHECKED_IN || booking.getStatus() == BookingStatus.COMPLETED)
                    ? TicketStatus.USED : TicketStatus.UNUSED);
            checkinTicketRepository.save(ticket);
        }
    }

    private void initBodyAssessments(Map<String, Client> clients, Map<String, Coach> coaches) {
        Client demo = clients.get("demo");
        Coach demoCoach = coaches.get("demoCoach");
        LocalDate today = LocalDate.now();

        ensureBodyAssessment(demo.getId(), demoCoach.getId(), today.minusDays(30).toString(),
                178.0, 76.0, 22.5, 44.8, 24.0, 8.0, 95.0, 84.0, 97.0,
                35.2, 35.6, 55.0, 55.4, 38.0, 38.2, "month-1 baseline");
        ensureBodyAssessment(demo.getId(), demoCoach.getId(), today.minusDays(15).toString(),
                178.0, 74.8, 21.4, 45.4, 23.6, 7.4, 95.8, 82.8, 96.4,
                35.8, 36.1, 55.5, 55.8, 38.4, 38.7, "mid check");
        ensureBodyAssessment(demo.getId(), demoCoach.getId(), today.minusDays(3).toString(),
                178.0, 73.9, 20.8, 46.0, 23.3, 7.0, 96.2, 81.9, 95.9,
                36.1, 36.4, 56.0, 56.2, 38.8, 39.0, "latest check");
    }

    private void initTrainingRecords(Map<String, Client> clients, Map<String, Coach> coaches) {
        Client demo = clients.get("demo");
        Coach demoCoach = coaches.get("demoCoach");
        LocalDate today = LocalDate.now();

        ensureTrainingRecord(demo.getId(), demoCoach.getId(), today.minusDays(7).toString(),
                "Upper Strength", 60, "Bench + Row + Press", "Stable form");
        ensureTrainingRecord(demo.getId(), demoCoach.getId(), today.minusDays(4).toString(),
                "Lower Strength", 70, "Squat + Deadlift + Lunge", "Good core control");
        ensureTrainingRecord(demo.getId(), demoCoach.getId(), today.minusDays(1).toString(),
                "Core and Cardio", 50, "Circuit + Core", "Need more sleep recovery");
    }

    private void initNotifications(Client demoClient) {
        if (demoClient == null) {
            return;
        }
        List<NotificationItem> existing = notificationService.getAllNotifications();

        ensureNotification(existing, null, NotificationItem.ReceiverType.ALL, NotificationType.SYSTEM,
                "System Notice", "Maintenance this Saturday at 22:30 for 30 minutes.", false, "");
        ensureNotification(existing, demoClient.getId(), NotificationItem.ReceiverType.CLIENT, NotificationType.BOOKING,
                "Booking Confirmed", "Your 14:00 session tomorrow has been confirmed.", false, null);
        ensureNotification(existing, demoClient.getId(), NotificationItem.ReceiverType.CLIENT, NotificationType.MEMBER,
                "Progress Notice", "Your body fat is lower than last month. Keep going.", true, null);
    }

    private Client ensureClient(String openid, String nickname, String avatarSeed, String phone,
                                String memberNumber, String memberLevel, int points, int coupons,
                                int trainingCount, String expireDate, String gender, int age,
                                String joinDate, List<String> tags) {
        Client existing = clientRepository.findByOpenid(openid);
        if (existing != null) {
            return existing;
        }

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
        return clientRepository.save(client);
    }

    private Coach ensureCoach(String openid, String nickname, String avatarSeed, String phone,
                              String intro, String specialty, String description, double rating,
                              int level, int classCount, List<String> tags, boolean featured,
                              boolean verified, Coach.Status status) {
        Coach existing = null;
        if (openid != null && !openid.isBlank()) {
            existing = coachRepository.findByOpenid(openid).orElse(null);
        }
        if (existing == null) {
            existing = coachRepository.findAll().stream()
                    .filter(c -> Objects.equals(c.getNickname(), nickname))
                    .findFirst()
                    .orElse(null);
        }
        if (existing != null) {
            return existing;
        }

        Coach coach = new Coach();
        coach.setOpenid(openid);
        coach.setNickname(nickname);
        coach.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + avatarSeed);
        coach.setPhone(phone);
        coach.setIntro(intro);
        coach.setSpecialty(specialty);
        coach.setDescription(description);
        coach.setRating(rating);
        coach.setLevel(level);
        coach.setClassCount(classCount);
        coach.setTags(tags);
        coach.setFeatured(featured);
        coach.setVerified(verified);
        coach.setStatus(status);
        return coachRepository.save(coach);
    }

    private Package ensurePackage(String name, PackageType type, int sessions, int validDays,
                                  double price, Double originalPrice, int pointsReward,
                                  String description, SaleStatus saleStatus) {
        Package existing = packageProductRepository.findAll().stream()
                .filter(p -> Objects.equals(p.getName(), name))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            return existing;
        }

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
        return packageProductRepository.save(p);
    }

    private Product ensureProduct(String name, String category, double price, String image,
                                  int pointsPrice, int pointsReward, String desc, int stock,
                                  SaleStatus saleStatus) {
        Product existing = productRepository.findAll().stream()
                .filter(p -> Objects.equals(p.getName(), name))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            return existing;
        }

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
        return productRepository.save(p);
    }

    private void ensureCoachBinding(Coach coach, Client client) {
        if (coach == null || client == null) {
            return;
        }
        if (coachWithUserRepository.existsByCoachIdAndClientId(coach.getId(), client.getId())) {
            return;
        }
        CoachWithUser relation = new CoachWithUser();
        relation.setCoachId(coach.getId());
        relation.setClientId(client.getId());
        coachWithUserRepository.save(relation);
    }

    private PackageOrder ensurePackageOrder(Integer userId, Package pkg,
                                            LocalDate startDate, LocalDate endDate,
                                            LocalDate purchaseDate, int totalSessions, int usedSessions,
                                            int pointsUsed, PackageOrderStatus status) {
        if (userId == null || pkg == null) {
            return null;
        }

        PackageOrder existing = packageOrderRepository.findByUserId(userId).stream()
                .filter(o -> Objects.equals(o.getPackageId(), pkg.getId()))
                .filter(o -> Objects.equals(o.getPurchaseDate(), purchaseDate.toString()))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            return existing;
        }

        int remaining = Math.max(0, totalSessions - usedSessions);
        double actualPay = Math.max(0, pkg.getPrice() - pointsUsed / 10.0);

        PackageOrder order = new PackageOrder();
        order.setUserId(userId);
        order.setPackageId(pkg.getId());
        order.setPackageName(pkg.getName());
        order.setType(pkg.getType());
        order.setTotalSessions(totalSessions);
        order.setUsedSessions(usedSessions);
        order.setRemainingSessions(remaining);
        order.setValidDays(pkg.getValidDays());
        order.setStartDate(startDate.toString());
        order.setEndDate(endDate.toString());
        order.setPurchaseDate(purchaseDate.toString());
        order.setPrice(pkg.getPrice());
        order.setPointsUsed(pointsUsed);
        order.setActualPay(actualPay);
        order.setPointsReward(pkg.getPointsReward());
        order.setStatus(status);
        return packageOrderRepository.save(order);
    }

    private ProductOrderItem buildOrderItem(Product product, String specs, int quantity) {
        ProductOrderItem item = new ProductOrderItem();
        item.setProductId(product.getId());
        item.setName(product.getName());
        item.setSpecs(specs);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());
        item.setImage(product.getImage());
        return item;
    }

    private ProductOrder ensureProductOrder(Integer userId, String orderDate, ProductOrderStatus status,
                                            String statusText, String trackingNumber, String estimatedDelivery,
                                            int pointsUsed, List<ProductOrderItem> items) {
        if (userId == null || items == null || items.isEmpty()) {
            return null;
        }

        double totalAmount = items.stream()
                .mapToDouble(i -> (i.getPrice() == null ? 0 : i.getPrice()) * (i.getQuantity() == null ? 0 : i.getQuantity()))
                .sum();
        int pointsReward = items.stream()
                .mapToInt(i -> {
                    Product p = productRepository.findById(Long.valueOf(i.getProductId())).orElse(null);
                    int reward = p == null || p.getPointsReward() == null ? 0 : p.getPointsReward();
                    return reward * (i.getQuantity() == null ? 0 : i.getQuantity());
                })
                .sum();
        double actualPay = Math.max(0, totalAmount - pointsUsed / 10.0);

        ProductOrder existing = productOrderRepository.findByUserId(userId).stream()
                .filter(o -> Objects.equals(o.getOrderDate(), orderDate))
                .filter(o -> o.getStatus() == status)
                .filter(o -> Math.abs((o.getTotalAmount() == null ? 0 : o.getTotalAmount()) - totalAmount) < 0.01)
                .findFirst()
                .orElse(null);
        if (existing != null) {
            return existing;
        }

        ProductOrder order = new ProductOrder();
        order.setUserId(userId);
        order.setItems(items);
        order.setTotalAmount(totalAmount);
        order.setPointsUsed(pointsUsed);
        order.setPointsReward(pointsReward);
        order.setActualPay(actualPay);
        order.setOrderDate(orderDate);
        order.setStatus(status);
        order.setStatusText(statusText);
        order.setTrackingNumber(trackingNumber);
        order.setEstimatedDelivery(estimatedDelivery);
        return productOrderRepository.save(order);
    }

    private void ensureHealthSurvey(Client client, String gender, int age, double height, double weight,
                                    String goal, String frequency, List<String> healthIssues, String notes) {
        if (client == null) {
            return;
        }
        HealthSurvey existing = healthSurveyRepository.findByUserId(client.getId());
        if (existing != null) {
            return;
        }

        HealthSurvey survey = new HealthSurvey();
        survey.setUserId(client.getId());
        survey.setName(client.getNickname());
        survey.setGender(gender);
        survey.setAge(age);
        survey.setHeight(height);
        survey.setWeight(weight);
        survey.setGoal(goal);
        survey.setFrequency(frequency);
        survey.setHealthIssues(healthIssues);
        survey.setNotes(notes);
        healthSurveyRepository.save(survey);
    }

    private CoachScheduleSlot ensureSlot(Integer coachId, String date, String startTime, String endTime,
                                         String roomName, CoachScheduleSlot.ScheduleType type, int expected) {
        List<CoachScheduleSlot> coachSlots = coachScheduleSlotRepository.findAllByCoachIdOrderByDateAscStartTimeAsc(coachId);
        for (CoachScheduleSlot slot : coachSlots) {
            if (Objects.equals(slot.getDate(), date) && Objects.equals(slot.getStartTime(), startTime)) {
                return slot;
            }
        }

        CoachScheduleSlot slot = new CoachScheduleSlot();
        slot.setCoachId(coachId);
        slot.setDate(date);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setRoomName(roomName);
        slot.setType(type);
        slot.setExpected(expected);
        slot.setActual(0);
        slot.setAvailable(expected > 0);
        return coachScheduleSlotRepository.save(slot);
    }

    private Map<String, CoachScheduleSlot> buildSlotIndex() {
        Map<String, CoachScheduleSlot> map = new HashMap<>();
        for (CoachScheduleSlot slot : coachScheduleSlotRepository.findAll()) {
            map.put(slotKey(slot.getCoachId(), slot.getDate(), slot.getStartTime()), slot);
        }
        return map;
    }

    private String slotKey(Integer coachId, String date, String startTime) {
        return coachId + "|" + date + "|" + startTime;
    }

    private Booking ensureBooking(Integer userId, Integer coachId, String bookingDate, String startTime, String endTime,
                                  String location, BookingStatus status, BookingSource source, PackageOrder packageOrder) {
        List<Booking> userBookings = bookingRepository.findAllByUserId(userId);
        for (Booking b : userBookings) {
            if (Objects.equals(b.getCoachId(), coachId)
                    && Objects.equals(b.getBookingDate(), bookingDate)
                    && Objects.equals(b.getStartTime(), startTime)) {
                return b;
            }
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCoachId(coachId);
        booking.setBookingDate(bookingDate);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setLocation(location);
        booking.setStatus(status);
        booking.setStatusText(defaultStatusText(status));
        booking.setSource(source);
        booking.setPackageOrderId(packageOrder == null ? null : String.valueOf(packageOrder.getId()));
        return bookingRepository.save(booking);
    }

    private String defaultStatusText(BookingStatus status) {
        if (status == null) {
            return "UNKNOWN";
        }
        switch (status) {
            case PENDING:
                return "PENDING";
            case CONFIRMED:
                return "CONFIRMED";
            case CHECKED_IN:
                return "CHECKED_IN";
            case COMPLETED:
                return "COMPLETED";
            case CANCELLED:
                return "CANCELLED";
            default:
                return "UNKNOWN";
        }
    }

    private Optional<Integer> parseOrderId(String packageOrderId) {
        if (packageOrderId == null || packageOrderId.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(packageOrderId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private int nonNull(Integer value) {
        return value == null ? 0 : value;
    }

    private void ensureBodyAssessment(Integer userId, Integer coachId, String date, double height, double weight,
                                      double bodyFat, double muscleMass, double bmi, double visceralFat,
                                      Double chest, Double waist, Double hips, Double leftArm, Double rightArm,
                                      Double leftThigh, Double rightThigh, Double leftCalf, Double rightCalf,
                                      String notes) {
        List<BodyAssessmentRecord> existing = bodyAssessmentRecordRepository.findByUserIdAndCoachId(userId, coachId);
        boolean duplicated = existing.stream().anyMatch(r -> Objects.equals(r.getDate(), date));
        if (duplicated) {
            return;
        }

        BodyAssessmentRecord record = new BodyAssessmentRecord();
        record.setUserId(userId);
        record.setCoachId(coachId);
        record.setDate(date);
        record.setHeight(height);
        record.setWeight(weight);
        record.setBodyFat(bodyFat);
        record.setMuscleMass(muscleMass);
        record.setBmi(bmi);
        record.setVisceralFat(visceralFat);
        record.setChest(chest);
        record.setWaist(waist);
        record.setHips(hips);
        record.setLeftArm(leftArm);
        record.setRightArm(rightArm);
        record.setLeftThigh(leftThigh);
        record.setRightThigh(rightThigh);
        record.setLeftCalf(leftCalf);
        record.setRightCalf(rightCalf);
        record.setNotes(notes);
        bodyAssessmentRecordRepository.save(record);
    }

    private void ensureTrainingRecord(Integer clientId, Integer coachId, String date, String title,
                                      Integer duration, String content, String comment) {
        List<TrainingRecord> existing = trainingRecordRepository.findByClientIdAndCoachId(clientId, coachId);
        boolean duplicated = existing.stream()
                .anyMatch(r -> Objects.equals(r.getDate(), date) && Objects.equals(r.getTitle(), title));
        if (duplicated) {
            return;
        }

        TrainingRecord record = new TrainingRecord();
        record.setClientId(clientId);
        record.setCoachId(coachId);
        record.setDate(date);
        record.setTitle(title);
        record.setDuration(duration);
        record.setContent(content);
        record.setComment(comment);
        trainingRecordRepository.save(record);
    }

    private void ensureNotification(List<NotificationItem> existing,
                                    Integer receiverUserId,
                                    NotificationItem.ReceiverType receiverType,
                                    NotificationType type,
                                    String title, String content, boolean isRead, String actionLink) {
        boolean duplicated = existing.stream()
                .anyMatch(n -> Objects.equals(n.getReceiverId(), receiverUserId)
                        && normalizeReceiverType(n) == receiverType
                        && n.getType() == type
                        && Objects.equals(n.getTitle(), title));
        if (duplicated) {
            return;
        }

        NotificationItem notification = new NotificationItem();
        notification.setReceiverId(receiverUserId);
        notification.setReceiverType(receiverType);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(isRead);
        notification.setActionLink(actionLink);
        notificationService.createNotification(notification);
    }

    private NotificationItem.ReceiverType normalizeReceiverType(NotificationItem item) {
        if (item.getReceiverType() != null) {
            return item.getReceiverType();
        }
        return item.getReceiverId() == null ? NotificationItem.ReceiverType.ALL : NotificationItem.ReceiverType.CLIENT;
    }
}
