package myProject.voting.web.rest;

import myProject.voting.model.User;
import myProject.voting.service.DishService;
import myProject.voting.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;



@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/resources/index.html";
    }


}
