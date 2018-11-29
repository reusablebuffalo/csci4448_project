package com.friendlyreminder.application;

import com.friendlyreminder.application.person.User;
import com.friendlyreminder.application.person.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Spring Controller that handles landing page, login, logout, and sign-up
 */
@Controller
@RequestMapping(path = "/")
public class HomeController {

    private final UserRepository userRepository;

    private static final String LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME = "loggedInUserId";

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * HomeController method that handles welcome/landing page (requests to '/')
     * @param httpSession current session that contains session attributes
     * @param model model object with model attributes used in displaying view
     * @return {@link String} name of view file to display
     */
    @RequestMapping("")
    public String welcome(HttpSession httpSession, Model model) {
        Integer userId = getUserIdFromSession(httpSession);
        if(userId != null) {
            return "redirect:/home";
        }
        return "welcome";
    }

    /**
     * method that handles requests to view signUp ('/signUp')
     * @return filename of signUp view to display
     */
    @RequestMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

    /**
     * method that handles User registration
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession contains session attrbutes, shared across session, handled by controller
     * @param firstName name of user to
     * @param username username identifier for registration
     * @param password password for user registration
     * @param confirmPassword string that should be identical to password
     * @return filename of view to display
     */
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
        try {
            User newUser = new User(username, password);
            newUser.setFirstName(firstName);
            userRepository.save(newUser);
            return validateLogin(redirectAttributes, httpSession, username, password);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e);
            return "redirct:/signUp";
        }
    }

    /**
     * method that displays login page
     * @return filename of view for login
     */
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * method that validates login and adds login status to session
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session to save logged in user to
     * @param username username of User to login
     * @param password password to attempt validation of
     * @return filename of view to display (if login successful show home, else return to login page)
     */
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
        setSessionLoggedInUserId(httpSession, userList.get(0).getId());
        return "redirect:/home";
    }

    /**
     * method to display home view
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @return filename of view to display (if logged in it is home, otherwise back to landing page)
     */
    @RequestMapping("/home")
    public String home(HttpSession httpSession, Model model){
        Integer loggedInUserId = getUserIdFromSession(httpSession);
        if(loggedInUserId == null){return "redirect:/";}

        Optional<User> optionalUser = userRepository.findById(loggedInUserId);
        if(!optionalUser.isPresent()){
            removeLoggedInUserIdFromSession(httpSession);
            return "redirect:/";
        }
        User loggedInUser = optionalUser.get();

        model.addAttribute("firstName",loggedInUser.getFirstName());
        model.addAttribute("contactBooks",loggedInUser.getContactBookList());

        return "home";
    }

    /**
     * HomeController method that handles logout
     * - invalidates session
     * - redirects to landing page
     * @param httpSession httpSession that contains login status attributes
     * @return view filename and redirect command
     */
    @RequestMapping("/logout")
    public String logout(HttpSession httpSession){
        removeLoggedInUserIdFromSession(httpSession);
        httpSession.invalidate();
        return "redirect:/";
    }

    // helpers
    /**
     * Method to extract unique id of logged in {@link User}
     * @param httpSession session that contains (if it exists) logged in user status
     * @return id of user logged into current session (or null if none is logged in)
     */
    public static Integer getUserIdFromSession(HttpSession httpSession){
        return (Integer) httpSession.getAttribute(LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME);
    }

    /**
     * Method to set unique id of new logged in {@link User}
     * @param httpSession session that will contain new logged in attribute
     * @param userId userId to set
     */
    public static void setSessionLoggedInUserId(HttpSession httpSession, Integer userId){
        httpSession.setAttribute(LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME,userId);
    }

    /**
     * Method that removes any login status attribute from current session
     * @param httpSession session to return logged in status attributes from (if they exist)
     */
    public static void removeLoggedInUserIdFromSession(HttpSession httpSession){
        httpSession.removeAttribute(LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME);
    }

}