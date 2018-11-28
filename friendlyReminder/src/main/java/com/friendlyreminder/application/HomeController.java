package com.friendlyreminder.application;

import com.friendlyreminder.application.person.User;
import com.friendlyreminder.application.person.UserController;
import com.friendlyreminder.application.person.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;


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
    public String welcome(HttpSession httpSession, Model model) {
        Integer userId = UserController.getUserIdFromSession(httpSession);
        if(userId != null) {
            return "redirect:/home";
        }
        model.addAttribute("author", author);
        return "welcome";
    }

    @RequestMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

    @PostMapping("/signUp/register")
    public String registerUser(RedirectAttributes redirectAttributes, HttpSession httpSession, @RequestParam String firstName, @RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword){
        // default flash attributes
        redirectAttributes.addFlashAttribute("defaultUsername",username)
                .addFlashAttribute("defaultFirstName",firstName);
        // guard clauses
        if(username.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid Username: can't be empty!");
            return "redirect:/signUp";
        }
        if(!userRepository.findByUsername(username).isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage", "Already exists account with that username!");
            return "redirect:/signUp";
        }
        if(!password.equals(confirmPassword)){
            redirectAttributes.addFlashAttribute("errorMessage","Passwords do not match!");
            return "redirect:/signUp";
        }
        // default behavior (create new user)
        User newUser = new User(username, password);
        newUser.setFirstName(firstName);
        userRepository.save(newUser);
        return validateLogin(redirectAttributes, httpSession, username, password);
//        return "redirect:/users/all";
    }


    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }

    @PostMapping("/login/validate")
    public String validateLogin(RedirectAttributes redirectAttributes, HttpSession httpSession, @RequestParam String username, @RequestParam String password){
        List<User> userList = userRepository.findByUsername(username);
        redirectAttributes.addFlashAttribute("defaultUsername",username);
        if(userList.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage", "Username is Incorrect!");
            return "redirect:/login";
        }
        if(!userList.get(0).validatePassword(password)){
            redirectAttributes.addFlashAttribute("errorMessage", "Password is Incorrect!");
            return "redirect:/login";
        }
        UserController.setSessionLoggedInUserId(httpSession, userList.get(0).getId());
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(HttpSession httpSession, Model model){
        Integer loggedInUserId = UserController.getUserIdFromSession(httpSession);
        if(loggedInUserId == null){return "redirect:/";}

        Optional<User> optionalUser = userRepository.findById(loggedInUserId);
        if(!optionalUser.isPresent()){
            UserController.removeLoggedInUserIdFromSession(httpSession);
            return "redirect:/";
        }
        User loggedInUser = optionalUser.get();

        model.addAttribute("firstName",loggedInUser.getFirstName());
        model.addAttribute("contactBooks",loggedInUser.getContactBookList());

        return "home";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        UserController.removeLoggedInUserIdFromSession(httpSession);
        httpSession.invalidate();
        return "redirect:/";
    }

}