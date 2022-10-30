package com.virtusa.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.virtusa.dto.BookingData;
import com.virtusa.dto.LoginUserDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.IncorrectLoginDetailsException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {
	private static final Logger log = LogManager.getLogger(UserController.class);
	private static final String REDIRECTLOGIN = "redirect:login";
	private static final String EMAIL = "email";
	
	@Autowired
	UserService service;
	
	public UserController() {
		log.warn("Controller Constructor called");
	}
	
	@GetMapping("/")
	public String index() {
		log.warn("Index called");
		return "Index";
	}
	
	@GetMapping("/register")
	public String getRegisterPage(@ModelAttribute("user") UserDto myuser) {
		log.info("Get Register Called");
		return "UserRegistration";
	}
	
	@PostMapping("/registerForm")
	public String postRegisterForm(@Valid @ModelAttribute("user") UserDto myUser,
			Errors error, Model model) {
		try {			
			if(error.hasErrors()) {	
				throw new IncorrectLoginDetailsException("Enter Details");
			}
			
			// registers users or throws error if user already exist
			service.saveUser(myUser);
		}
		catch(UserAlreadyExistException | IncorrectLoginDetailsException e){	
			model.addAttribute("errMessage", e.getMessage());
			return "UserRegistration";
		}
		return	REDIRECTLOGIN;
	}

	@GetMapping("/login")
	public String getLoginPage(@ModelAttribute("user") LoginUserDto myuser) {
		log.info("Get Register Called");
		return "UserLogin";
	}
	
	@PostMapping("/loginForm")
	public String postLoginForm(@Valid @ModelAttribute("user") LoginUserDto myUser,
			Errors error, Model model, HttpSession session) {
		try {
			if(error.hasErrors()) {			
				throw new IncorrectLoginDetailsException("Enter Details");
			}
			
			// login user or throws error UserNotFound or IncorrectLoginDetailException
			service.loginUser(myUser);
		}
		catch(IncorrectLoginDetailsException | UserNotFoundException e) {
			model.addAttribute("errMessage", e.getMessage());
			return "UserLogin";
		}
		session.setAttribute(EMAIL, myUser.getEmail());		// set session on login
		return	"redirect:home";
	}
	
	@GetMapping("/home")
	public String getHome(Model model, HttpSession session) {
		String email = (String) session.getAttribute(EMAIL);
		if(email == null) {
			return REDIRECTLOGIN;
		}
		
		model.addAttribute("userName",service.getUser(email).getUsername());
		model.addAttribute("allLawyer", service.getAllLawyer());
		return "UserHome";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		String email = (String) session.getAttribute(EMAIL);
		if(email != null) {
			service.logoutUser(email);
			session.removeAttribute(EMAIL);						// remove session on logout
			log.info(email);
		}		
		return REDIRECTLOGIN;
	}
	
	@GetMapping("/caseRecord")
	public String getCases(Model model, HttpSession session) {
		String email = (String) session.getAttribute(EMAIL);
		if(email == null) {
			return REDIRECTLOGIN;
		}
		model.addAttribute("allCase", service.getUserCase(email));
		return "UserCase";
	}
	
	@PostMapping("/bookingForm")
	public String bookAppointment(@ModelAttribute("bookingData") BookingData booking) {
		
		return "redirect:home";
	}
}
