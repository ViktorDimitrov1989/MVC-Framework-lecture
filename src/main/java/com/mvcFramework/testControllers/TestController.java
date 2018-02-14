package com.mvcFramework.testControllers;

import com.mvcFramework.annotations.controller.Controller;
import com.mvcFramework.annotations.requests.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String getPage(){
        return  null;
    }

}
