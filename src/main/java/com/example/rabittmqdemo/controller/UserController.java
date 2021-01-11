package com.example.rabittmqdemo.controller;


import com.example.rabittmqdemo.mq.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.rabittmqdemo.entity.User;
import java.time.LocalDate;

/**
 *
 *
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private Sender sender;

    @RequestMapping("/send")
    public void send() {
        sender.send(User
                .builder()
                .userName("天文")
                .birthday(LocalDate.of(1995, 1, 31))
                .address("浙江杭州")
                .build());
    }
}
