package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.fitnessjava.dao.*;
import org.example.fitnessjava.pojo.*;
import org.example.fitnessjava.pojo.vo.DashboardOverviewResponse;
import org.example.fitnessjava.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private CoachRepository coachRepository;

    @Resource
    private PackageProductRepository packageProductRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private PackageOrderRepository packageOrderRepository;

    @Resource
    private ProductOrderRepository productOrderRepository;

    @Override
    public DashboardOverviewResponse getOverview() {
        DashboardOverviewResponse response = new DashboardOverviewResponse();

        response.setTotalUsers(clientRepository.count());
        response.setTotalCoaches(coachRepository.count());
        response.setOnlineCoaches(coachRepository.findByStatus(Coach.Status.ONLINE).stream().count());
        response.setFeaturedCoaches(coachRepository.findByFeatured(true).stream().count());
        response.setTotalPackages(packageProductRepository.count());
        response.setTotalProducts(productRepository.count());

        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<Booking> todayBookings = bookingRepository.findByBookingDate(today);
        response.setTodayBookings((long) todayBookings.size());
        response.setPendingBookings(bookingRepository.findByStatus(BookingStatus.PENDING).stream().count());
        response.setConfirmedBookings(bookingRepository.findByStatus(BookingStatus.CONFIRMED).stream().count());

        List<PackageOrder> todayPackageOrders = packageOrderRepository.findByPurchaseDate(today);
        response.setTodayPackageOrders((long) todayPackageOrders.size());

        List<ProductOrder> todayProductOrders = productOrderRepository.findByOrderDate(today);
        response.setTodayProductOrders((long) todayProductOrders.size());

        response.setPendingRefunds(packageOrderRepository.findByStatus(PackageOrderStatus.REFUNDING).stream().count());
        response.setPendingShipments(productOrderRepository.findByStatus(ProductOrderStatus.PAID).stream().count());

        response.setRecentRevenue(generateRecentRevenue());
        response.setOrderDistribution(generateOrderDistribution());
        response.setTodoItems(generateTodoItems());

        return response;
    }

    private List<DashboardOverviewResponse.RevenueData> generateRecentRevenue() {
        List<DashboardOverviewResponse.RevenueData> revenue = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(formatter);

            List<PackageOrder> packageOrders = packageOrderRepository.findByPurchaseDate(date.toString());
            List<ProductOrder> productOrders = productOrderRepository.findByOrderDate(date.toString());

            double packageRevenue = packageOrders.stream()
                    .mapToDouble(order -> order.getPrice() != null ? order.getPrice() : 0)
                    .sum();
            double productRevenue = productOrders.stream()
                    .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0)
                    .sum();

            DashboardOverviewResponse.RevenueData data = new DashboardOverviewResponse.RevenueData();
            data.setDate(dateStr);
            data.setAmount(packageRevenue + productRevenue);
            revenue.add(data);
        }

        return revenue;
    }

    private List<DashboardOverviewResponse.OrderDistribution> generateOrderDistribution() {
        List<DashboardOverviewResponse.OrderDistribution> distribution = new ArrayList<>();

        DashboardOverviewResponse.OrderDistribution packageOrders = new DashboardOverviewResponse.OrderDistribution();
        packageOrders.setCategory("套餐订单");
        packageOrders.setCount(packageOrderRepository.count());
        distribution.add(packageOrders);

        DashboardOverviewResponse.OrderDistribution productOrders = new DashboardOverviewResponse.OrderDistribution();
        productOrders.setCategory("商品订单");
        productOrders.setCount(productOrderRepository.count());
        distribution.add(productOrders);

        DashboardOverviewResponse.OrderDistribution bookings = new DashboardOverviewResponse.OrderDistribution();
        bookings.setCategory("预约课程");
        bookings.setCount(bookingRepository.count());
        distribution.add(bookings);

        return distribution;
    }

    private List<DashboardOverviewResponse.TodoItem> generateTodoItems() {
        List<DashboardOverviewResponse.TodoItem> todos = new ArrayList<>();

        Long pendingBookings = bookingRepository.findByStatus(BookingStatus.PENDING).stream().count();
        if (pendingBookings > 0) {
            DashboardOverviewResponse.TodoItem pendingBookingItem = new DashboardOverviewResponse.TodoItem();
            pendingBookingItem.setId("1");
            pendingBookingItem.setTitle("待确认预约");
            pendingBookingItem.setCount(pendingBookings);
            pendingBookingItem.setUrgent(true);
            todos.add(pendingBookingItem);
        }

        Long pendingRefunds = packageOrderRepository.findByStatus(PackageOrderStatus.REFUNDING).stream().count();
        if (pendingRefunds > 0) {
            DashboardOverviewResponse.TodoItem refundItem = new DashboardOverviewResponse.TodoItem();
            refundItem.setId("2");
            refundItem.setTitle("待处理退款");
            refundItem.setCount(pendingRefunds);
            refundItem.setUrgent(true);
            todos.add(refundItem);
        }

        Long pendingShipments = productOrderRepository.findByStatus(ProductOrderStatus.PAID).stream().count();
        if (pendingShipments > 0) {
            DashboardOverviewResponse.TodoItem shipmentItem = new DashboardOverviewResponse.TodoItem();
            shipmentItem.setId("3");
            shipmentItem.setTitle("待发货订单");
            shipmentItem.setCount(pendingShipments);
            shipmentItem.setUrgent(false);
            todos.add(shipmentItem);
        }

        if (todos.isEmpty()) {
            DashboardOverviewResponse.TodoItem emptyItem = new DashboardOverviewResponse.TodoItem();
            emptyItem.setId("0");
            emptyItem.setTitle("暂无待办事项");
            emptyItem.setCount(0L);
            emptyItem.setUrgent(false);
            todos.add(emptyItem);
        }

        return todos;
    }
}