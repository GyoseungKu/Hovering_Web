package net.hovering.Hovering_Web.main;

import net.hovering.Hovering_Web.login.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User()); // 필요한 데이터 추가
        return "index";
    }
}