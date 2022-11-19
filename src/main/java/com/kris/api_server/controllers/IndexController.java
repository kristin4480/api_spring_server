package com.kris.api_server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    
    @GetMapping(produces = "application/json")
    public String index() {

        return "{ \"name\":  \"kur\"}";
    }
    
    @GetMapping("/asd")
    public String index2() {

        return "Hello World2!";
    }
}
