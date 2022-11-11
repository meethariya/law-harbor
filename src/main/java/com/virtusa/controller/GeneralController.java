package com.virtusa.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.virtusa.dto.LoginUserDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.IncorrectLoginDetailsException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.service.UserService;

@Controller
@RequestMapping("/")
public class GeneralController {
	// handles general login and registration requests
	
	private static final Logger log = LogManager.getLogger(GeneralController.class);
	
	private static final String ERR = "errMessage";
	
	@Autowired
	UserService service;

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public GeneralController() {
		log.warn("GeneralController Constructor called");
	}
	
	@GetMapping("/")
	public String index() {
		// Index Page
		return "redirect:postLogin";
	}
	
	@GetMapping("/register")
	public String getRegisterPage(@ModelAttribute("user") UserDto myuser) {
		// registration page
		return "UserRegistration";
	}
	
	@PostMapping("/register")
	public String postRegisterForm(@Valid @ModelAttribute("user") UserDto myUser,
			Errors error, Model model, RedirectAttributes redirectAttributes) {
		// registration form submission
		
		try {			
			if(error.hasErrors()) {	
				throw new IncorrectLoginDetailsException("Enter Valid Details");
			}
			// registers users or throws error if user already exist
			myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
			String role = messageSource.getMessage("role.lawyer", null, "lawyer", Locale.ENGLISH);
			service.saveUser(myUser, role);
			redirectAttributes.addFlashAttribute(ERR,"Account registerd");
		}
		catch(UserAlreadyExistException | IncorrectLoginDetailsException e){	
			model.addAttribute(ERR, e.getMessage());
			return "UserRegistration";
		}
		
		return	"redirect:login";
	}

	@GetMapping("/login")
	public String getLoginPage(@ModelAttribute("user") LoginUserDto myuser) {
		// login page
		return "UserLogin";
	}
	
	@GetMapping("/postLogin")
	public String afterLogin(Authentication authentication) {
		
		String loginEmail = authentication.getName();
		String role = service.loginUser(loginEmail);
		
		if(role.equals(getValueFromProperties("role.user","user"))) {
			return	"redirect:user/home/";
		}
		
		else if(role.equals(getValueFromProperties("role.lawyer","lawyer"))) {
			return	"redirect:lawyer/";				
		}
		
		else {
			return	"redirect:admin/";				
		}
	}

	@GetMapping("/logoutUser")
	@ResponseBody
	public String logout(Authentication authentication) {
		// logout user
		
		String email = authentication.getName();
		log.info("Logging out");
		if(email != null) {
			service.logoutUser(email);			// change active status of user
		}
		return "Success logout";
	}
	
	public String getValueFromProperties() {
		// returns session key value for user email
		return getValueFromProperties("email.user","userEmail");
	}

	public String getValueFromProperties(String key, String defaultValue) {
		// returns properties based on key and default value
		return messageSource.getMessage(key, null, defaultValue, Locale.ENGLISH);
	}
}
