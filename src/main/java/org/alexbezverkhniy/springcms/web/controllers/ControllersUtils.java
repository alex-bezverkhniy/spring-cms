package org.alexbezverkhniy.springcms.web.controllers;

import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bezverkhniy_10534 on 29/08/2014.
 */
public class ControllersUtils {
    public static String mapHtmlPage(HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if(restOfTheUrl.indexOf(".html") > 0) {
            return restOfTheUrl.replace(".html", "");
        } else {
            return restOfTheUrl+"/index.html";
        }
    }
}
