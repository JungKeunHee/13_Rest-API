package com.ohgiraffers.restapitest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuController {

    @GetMapping("/")
    public String mainPage() {
        return "main/main";
    }

    
}
