package org.example.fitnessjava.controller;

import jakarta.annotation.Resource;
import org.example.fitnessjava.dao.UserProfileRepository;
import org.example.fitnessjava.pojo.UserProfile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Resource
    UserProfileRepository userProfileRepository;

    @GetMapping
    public String get(){
        ArrayList<UserProfile> userProfiles = new ArrayList<>();
        userProfileRepository.findAll().forEach(userProfiles::add);
        return userProfiles.toString();
    }
}
