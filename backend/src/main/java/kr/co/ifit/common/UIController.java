package kr.co.ifit.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {

    @RequestMapping({"path:^(?!api).*"})
    public String redirect() {
        // static/resources/index.html로 리다이렉트
        return "forward:/index.html";
    }
}