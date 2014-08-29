package org.alexbezverkhniy.springcms.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bezverkhniy_10534 on 29/08/2014.
 */
@Controller
public class AdminController {
    final static Logger log = LoggerFactory.getLogger(AdminController.class);

    @Secured("ADMIN")
    @RequestMapping("admin")
    public String admin(HttpServletRequest request){
        return "admin/index";
    }

    @Secured("ADMIN")
    @RequestMapping("admin/**.html")
    public String htmlPages(HttpServletRequest request){
        return ControllersUtils.mapHtmlPage(request);
    }
}
