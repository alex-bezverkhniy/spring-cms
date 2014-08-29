package org.alexbezverkhniy.springcms.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bezverkhniy_10534 on 21/08/2014.
 */
@Controller
public class WelcomeController {

    final static Logger log = LoggerFactory.getLogger(WelcomeController.class);

    @RequestMapping("/")
    public String index(){
        log.info("index");
        System.out.println("index");
        return "index";
    }
/*
    @RequestMapping("login")
    public String login(){
        return "login";
    }


    @RequestMapping("error")
    public String error(){
        return "error";
    }
*/

    @RequestMapping("/**.html")
    public String htmlPages(HttpServletRequest request){
        return ControllersUtils.mapHtmlPage(request);
    }

    @RequestMapping("layout")
    public String layout(){
        log.info("layout");
        System.out.println("layout");
        return "layouts/layout";
    }


}
