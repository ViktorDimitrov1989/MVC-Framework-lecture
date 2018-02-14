package com.mvcFramework.testControllers;

import com.mvcFramework.annotations.controller.Controller;
import com.mvcFramework.annotations.parameters.PathVariable;
import com.mvcFramework.annotations.parameters.RequestParam;
import com.mvcFramework.annotations.requests.GetMapping;
import com.mvcFramework.annotations.requests.PostMapping;
import com.mvcFramework.model.Model;

@Controller
public class BeerController {

    @GetMapping("/beer/{id}")
    public String getBeerById(@PathVariable("id") Integer id){

        System.out.println(id);
        return "beer";
    }

    @GetMapping("/beer")
    public String getBeer(){
        return "beer";
    }

    @PostMapping("/beer")
    public String getBeer(@RequestParam("brand") String beerBrand){
        System.out.println(beerBrand);
        return "beer";
    }

    @GetMapping("/beer/model")
    public String passDataToTheView(Model model){
        System.out.println("asdasdasd");
        model.addAttribute("key", "test-beer");
        return "beer";
    }

}
