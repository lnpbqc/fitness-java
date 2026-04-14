package org.example.fitnessjava.controller.client;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.example.fitnessjava.dao.CoachWithUserRepository;
import org.example.fitnessjava.pojo.Client;
import org.example.fitnessjava.pojo.Coach;
import org.example.fitnessjava.pojo.CoachWithUser;
import org.example.fitnessjava.service.ClientService;
import org.example.fitnessjava.service.CoachService;
import org.example.fitnessjava.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/client/member")
@Slf4j
@Tag(name = "客户端会员档案接口", description = "客户端会员档案的查询与编辑接口")
public class ClientMemberController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ClientService clientService;

    @Resource
    private CoachService coachService;

    @Resource
    private CoachWithUserRepository coachWithUserRepository;

    @GetMapping("/qr")
    @Operation(summary = "获取当前用户会员的二维码", description = "根据 Authorization token 形成二维码")
    public String getMyProfileQRCode(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        String openid = getCurrentOpenid(token);
        Client client = clientService.existUser(openid);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在");
        }
        Integer userId = client.getId();
        String qrContent = "MEMBER_QR:" + userId +"," + token;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            HashMap<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            return "data:image/png;base64," + Base64.encodeBase64String(imageBytes);
        } catch (WriterException | IOException e) {
            log.error("生成二维码失败", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "生成二维码失败");
        }
    }

    @GetMapping
    @Operation(summary = "获取当前用户会员档案", description = "根据 Authorization token 获取当前登录用户的会员信息")
    public Client getMyProfile(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        String openid = getCurrentOpenid(token);
        Client client = clientService.existUser(openid);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在");
        }
        return client;
    }

    @PutMapping
    @Operation(summary = "更新会员档案", description = "更新当前登录用户的会员信息，支持修改性别、年龄、标签、加入日期等")
    public Client updateMyProfile(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token,
            @RequestBody Client updates
    ) {
        String openid = getCurrentOpenid(token);
        Client client = clientService.existUser(openid);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在");
        }
        if (updates.getGender() != null) {
            client.setGender(updates.getGender());
        }
        if (updates.getAge() != null) {
            client.setAge(updates.getAge());
        }
        if (updates.getPhone() != null) {
            client.setPhone(updates.getPhone());
        }
        if (updates.getMemberLevel() != null) {
            client.setMemberLevel(updates.getMemberLevel());
        }
        if (updates.getTags() != null) {
            client.setTags(updates.getTags());
        }
        if (updates.getJoinDate() != null) {
            client.setJoinDate(updates.getJoinDate());
        }
        if (updates.getMembershipExpireAt() != null) {
            client.setMembershipExpireAt(updates.getMembershipExpireAt());
        }
        if (updates.getAvatar() != null) {
            client.setAvatar(updates.getAvatar());
        }
        if (updates.getNickname() != null) {
            client.setNickname(updates.getNickname());
        }
        return clientService.updateClient(client);
    }

    @GetMapping("/summary")
    @Operation(summary = "获取会员摘要信息", description = "返回当前用户的会员等级、积分、优惠券、训练次数等汇总信息")
    public Map<String, Object> getMemberSummary(
            @Parameter(description = "客户端登录 token", example = "Bearer eyJhbGciOiJIUzI1NiJ9...")
            @RequestHeader("Authorization") String token
    ) {
        Client client = clientService.existUser(getCurrentOpenid(token));
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在");
        }
        return Map.of(
                "userId", client.getId(),
                "nickname", client.getNickname() != null ? client.getNickname() : "",
                "avatar", client.getAvatar() != null ? client.getAvatar() : "",
                "memberLevel", client.getMemberLevel() != null ? client.getMemberLevel() : "普通会员",
                "memberNumber", client.getMemberNumber() != null ? client.getMemberNumber() : "",
                "points", client.getPoints(),
                "coupons", client.getCoupons(),
                "totalTrainingCount", client.getTotalTrainingCount(),
                "membershipExpireAt", client.getMembershipExpireAt() != null ? client.getMembershipExpireAt() : ""
        );
    }

    private String getCurrentOpenid(String token) {
        String openid = jwtUtil.getSubjectFromAuthorization(token);
        if (openid == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户登录信息无效");
        }
        return openid;
    }
}
