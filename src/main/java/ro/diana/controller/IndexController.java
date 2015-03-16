package ro.diana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by diana on 16.03.2015.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/")
    public String redirect() {
        return "forward:/resources/index.html";
    }
}
