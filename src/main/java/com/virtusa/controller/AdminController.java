package com.virtusa.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.virtusa.dto.EditLawyerDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.IncorrectDetailsException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	// handles all admin requests
	
	private static final Logger log = LogManager.getLogger(AdminController.class);
	private static final String REDIRECTHOME = "redirect:/admin/";
	private static final String REDIRECTLOGIN = "redirect:/login";
	
	private static final String ERRMESSAGE = "errMessage";
	private static final String ADMINHOME = "AdminHome";
	
	public AdminController() {
		log.warn("AdminController Constructor Called");
	}
	@Autowired
	AdminService service;
	@Autowired
	MessageSource messageSource;
	
	@GetMapping("/")
	public String home(Model model, Authentication authentication, 
			@ModelAttribute("user") UserDto myuser, @ModelAttribute("errMessage") String err,
			@ModelAttribute("editUser") EditLawyerDto editLawyerModel) {
		//	admin home page
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		model.addAttribute("username",service.getUser(email).getUsername());
		model.addAttribute("allLawyer",service.getAllLawyer());
		model.addAttribute(ERRMESSAGE,err);
		
		return ADMINHOME;
	}
	
	@GetMapping("/lawyer/{id}")
	public String deleteLawyer(@PathVariable("id")int lawyerId, RedirectAttributes redirectAttributes,
			Authentication authentication) {
		// delete lawyer given id
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		try {			
			service.deleteLawyer(lawyerId);
			redirectAttributes.addFlashAttribute(ERRMESSAGE, "Lawyer Deleted Successfully");
		}
		catch(UserNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERRMESSAGE, e.getMessage());
		}
		
		return REDIRECTHOME;
	}
	
	@PostMapping("/lawyer")
	public String editLawyer(@Valid @ModelAttribute("editUser") EditLawyerDto lawyer,  
			Errors error, @ModelAttribute("user") UserDto myuser, Model model, 
			Authentication authentication, RedirectAttributes redirectAttributes) {
		// updates lawyer information
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		try {	
			if(error.hasErrors()) {
				log.info(error.getAllErrors());
				throw new IncorrectDetailsException("Enter Valid Details");
			}
			// updates lawyer information
			service.updateLawyer(lawyer);
			redirectAttributes.addFlashAttribute(ERRMESSAGE,"lawyer edited successfully");
		}
		catch(IncorrectDetailsException | UserAlreadyExistException e){
			model.addAllAttributes(homeDataLoader(authentication, e.getMessage()));
			return ADMINHOME;
		}
		
		return REDIRECTHOME;
	}
	
	@PostMapping("/addLawyer")
	public String addLawyer(@Valid @ModelAttribute("user") UserDto lawyer, 
			Errors error, RedirectAttributes redirectAttributes, Model model,
			@ModelAttribute("editUser")EditLawyerDto editLawyer, Authentication authentication) {
		// adds new lawyer
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		try {			
			if(error.hasErrors()) {	
				log.info("Errors");
				throw new IncorrectDetailsException("Enter Valid Details");
			}
			
			// registers users or throws error if user already exist
			String role = messageSource.getMessage("role.lawyer", null, "lawyer", Locale.ENGLISH);
			service.saveUser(lawyer, role);
			
			redirectAttributes.addFlashAttribute(ERRMESSAGE,"lawyer added successfully");
			return	REDIRECTHOME;
		}
		catch(UserAlreadyExistException | IncorrectDetailsException e){
			model.addAllAttributes(homeDataLoader(authentication, e.getMessage()));
			return ADMINHOME;
		}
	}
	
	
	public String getEmailFromProperties() {
		// returns session key value for admin email
		return messageSource.getMessage("email.admin", null, "adminEmail", Locale.ENGLISH);
	}
	
	public Map<String, Object> homeDataLoader(Authentication authentication, String err) {
		// loads model attributes for home page
		Map<String, Object> model = new HashMap<>();	
		String email = authentication.getName();
		model.put("username", service.getUser(email).getUsername());
		model.put("allLawyer",service.getAllLawyer());
		model.put(ERRMESSAGE,err);
		return model;
	}
	
	public boolean sessionChecker(Authentication authentication) {
		// checks if user is authenticated. Should redirect login page if null
		return authentication==null;
	}
}
