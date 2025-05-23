package backend.academy.diplom.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class RedirectController {

    @GetMapping("/api/redirect")
    public String redirectToApp(
            @RequestParam("token") String token,
            @RequestParam("mail") String mail
    ) {
        String uri = String.format(
                "protimrf://reset-password?token=%s&mail=%s", token, mail
        );
        return "redirect:" + uri;
    }
}
