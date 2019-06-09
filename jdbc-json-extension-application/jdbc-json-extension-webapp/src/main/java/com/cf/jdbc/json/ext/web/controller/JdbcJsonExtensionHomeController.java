package com.cf.jdbc.json.ext.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JdbcJsonExtensionHomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
