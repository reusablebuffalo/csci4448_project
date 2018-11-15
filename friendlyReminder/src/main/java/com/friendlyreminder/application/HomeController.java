package com.friendlyreminder.application;

import com.friendlyreminder.application.event.CommunicationEventRepository;
import com.friendlyreminder.application.person.ContactRepository;
import com.friendlyreminder.application.person.User;
import com.friendlyreminder.application.person.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import java.util.List;


@Controller
public class HomeController {

    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${home.author}")
    private String author;

    @RequestMapping("/")
    public String welcome(Model model) {
        model.addAttribute("author", author);
        return "welcome";
    }

    @RequestMapping("/signUp")
    public String signUp(Model model){
        return "signUp";
    }

    @PostMapping("/signUp/register")
    public String registerUser(RedirectAttributes redirectAttributes, @RequestParam String firstName, @RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword){
        if (password.equals(confirmPassword)){
            User newUser = new User(username, password);
            newUser.setFirstName(firstName);
            userRepository.save(newUser);
            return "redirect:/users/all";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match!")
                    .addFlashAttribute("defaultUsername",username)
                    .addFlashAttribute("defaultFirstName",firstName);
            return "redirect:/signUp";
        }
    }


    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }

    @PostMapping("/login/validate")
    public String validateLogin(RedirectAttributes redirectAttributes, @RequestParam String username, @RequestParam String password){
        List<User> userList = userRepository.findByUsername(username);
        if(!userList.isEmpty()){
            if(userList.get(0).validatePassword(password)){
                redirectAttributes.addFlashAttribute("firstName",userList.get(0).getFirstName());
                return "redirect:/home";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Username/Password is Incorrect").addFlashAttribute("defaultUsername",username);
        return "redirect:/login";
    }

    @RequestMapping("/home")
    public String home(Model model){
        return "home";
    }

}