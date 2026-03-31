package org.example.fitnessjava.service.impl;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.ClientRepository;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Booking;
import org.example.fitnessjava.pojo.BookingStatus;
import org.example.fitnessjava.pojo.vo.BookingVO;
import org.example.fitnessjava.repository.BookingRepository;
import org.example.fitnessjava.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Resource
    private BookingRepository bookingRepository;

    @Resource
    private ClientRepository clientRepository;

    private BookingVO convertToVO(Booking booking) {
        BookingVO vo = new BookingVO();
        vo.setId(booking.getId());
        vo.setUserId(booking.getUserId());
        vo.setCoachId(booking.getCoachId());
        vo.setCoachName(booking.getCoachName());
        vo.setCoachAvatar(booking.getCoachAvatar());
        vo.setSpecialty(booking.getSpecialty());
        vo.setBookingDate(booking.getBookingDate());
        vo.setStartTime(booking.getStartTime());
        vo.setEndTime(booking.getEndTime());
        vo.setLocation(booking.getLocation());
        vo.setStatus(booking.getStatus());
        vo.setStatusText(booking.getStatusText());
        vo.setSource(booking.getSource());
        vo.setPackageOrderId(booking.getPackageOrderId());
        vo.setPhone(booking.getPhone());
        
        // 获取用户名
        try {
            Client client = clientRepository.findById((long) booking.getUserId()).orElse(null);
            if (client != null) {
                vo.setUserName(client.getNickname());
            }
        } catch (Exception e) {
            vo.setUserName("未知用户");
        }
        
        return vo;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookingVO> getAllBookingsWithUserInfo() {
        return bookingRepository.findAll().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<Booking> getBookingsByCoachId(int coachId) {
        return bookingRepository.findByCoachId(coachId);
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<BookingVO> getBookingsWithUserInfo(BookingStatus status, Integer coachId, Integer userId) {
        return bookingRepository.findBookings(status, coachId, userId).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> getBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    public BookingVO getBookingByIdWithUserInfo(Integer id) {
        return bookingRepository.findById(id)
                .map(this::convertToVO)
                .orElse(null);
    }

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }
        if (booking.getStatusText() == null) {
            booking.setStatusText("待确认");
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBookingStatus(Integer id, BookingStatus status) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(status);
                    booking.setStatusText(getStatusText(status));
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("预约不存在"));
    }

    @Override
    @Transactional
    public Booking cancelBooking(Integer id, String reason) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(BookingStatus.CANCELLED);
                    booking.setStatusText("已取消：" + reason);
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("预约不存在"));
    }

    @Override
    @Transactional
    public Booking confirmBooking(Integer id) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(BookingStatus.CONFIRMED);
                    booking.setStatusText("已确认");
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("预约不存在"));
    }

    @Override
    @Transactional
    public void deleteBooking(Integer id) {
        bookingRepository.deleteById(id);
    }

    private String getStatusText(BookingStatus status) {
        switch (status) {
            case PENDING: return "待确认";
            case CONFIRMED: return "已确认";
            case COMPLETED: return "已完成";
            case CANCELLED: return "已取消";
            case CHECKED_IN: return "已核销";
            default: return "未知";
        }
    }
}
