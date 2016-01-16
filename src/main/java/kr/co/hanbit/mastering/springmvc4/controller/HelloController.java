package kr.co.hanbit.mastering.springmvc4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "hello";
    }
}
