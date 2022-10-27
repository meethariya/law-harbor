package com.virtusa.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.virtusa.model.User;
import com.virtusa.service.UserService;

@Controller
public class UserController {
	private static final Logger log = LogManager.getLogger(UserController.class);
	
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
	public String getRegisterPage() {
		log.info("Get Register Called");
		return "UserRegistration";
	}
	
	@PostMapping("/registerForm")
	public String postRegisterForm(User myUser) {
		service.saveUser(myUser);
		return	"redirect:/";
	}
}
