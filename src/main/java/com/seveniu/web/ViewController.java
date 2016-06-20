package com.seveniu.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by seveniu on 6/9/16.
 * ViewController
 */
@Controller
@RequestMapping("/view")
public class ViewController {

    @RequestMapping(value = "/monitor", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String monitorAll() {
        return "monitor/list";
    }
}
