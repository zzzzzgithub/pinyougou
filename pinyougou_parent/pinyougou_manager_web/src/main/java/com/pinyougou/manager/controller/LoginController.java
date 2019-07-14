package com.pinyougou.manager.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("info")
    public Map getName() {
        //从SpringSecurity中获取登录名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<Object, Object> map = new HashMap<>();
        map.put("loginName", username);
        return map;
    }
}
