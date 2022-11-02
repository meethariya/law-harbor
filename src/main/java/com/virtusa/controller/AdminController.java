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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.virtusa.dto.UserDto;
import com.virtusa.exception.IncorrectLoginDetailsException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final Logger log = LogManager.getLogger(AdminController.class);
	private static final String REDIRECTLOGIN = "redirect:/login";
	private static final String REDIRECTHOME = "redirect:/admin/";
	private static final String EMAIL = "adminEmail";
	private static final String ERRMESSAGE = "errMessage";
	
	public AdminController() {
		log.warn("AdminController Constructor Called");
	}
	@Autowired
	AdminService service;
	
	@GetMapping("/")
	public String home(Model model, HttpSession session,
			@ModelAttribute("user") UserDto myuser, @ModelAttribute("errMessage") String err) {
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		String adminEmail = (String) session.getAttribute(EMAIL);
		model.addAttribute("username",service.getUser(adminEmail).getUsername());
		model.addAttribute("allLawyer",service.getAllLawyer());
		model.addAttribute(ERRMESSAGE,err);
		return "AdminHome";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// logout admin
		String email = (String) session.getAttribute(EMAIL);
		if(email != null) {
			service.logoutUser(email);
			session.removeAttribute(EMAIL);						// remove session on logout
		}		
		return REDIRECTLOGIN;
	}
	
	@GetMapping("/lawyer/{id}")
	public String deleteLawyer(@PathVariable("id")int lawyerId, HttpSession session, 
			RedirectAttributes redirectAttributes) {
		// delete lawyer given id
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
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
	public String editLawyer(@Valid @ModelAttribute("user") UserDto lawyer,Errors error, 
			HttpSession session, RedirectAttributes redirectAttributes) {
		// updates lawyer information
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		try {	
			if(error.hasErrors()) {	
				throw new IncorrectLoginDetailsException("Enter Details");
			}
			
			// updates lawyer information
			service.updateLawyer(lawyer);
			redirectAttributes.addFlashAttribute(ERRMESSAGE,"lawyer edited successfully");
		}
		catch(IncorrectLoginDetailsException | UserAlreadyExistException e){
			redirectAttributes.addFlashAttribute(ERRMESSAGE, e.getMessage());
		}
		return REDIRECTHOME;
	}
	
	@PostMapping("/addLawyer")
	public String addLawyer(@Valid @ModelAttribute("user") UserDto lawyer, 
			Errors error, RedirectAttributes redirectAttributes, HttpSession session) {
		// adds new lawyer
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		try {			
			if(error.hasErrors()) {	
				throw new IncorrectLoginDetailsException("Enter Details");
			}
			
			// registers users or throws error if user already exist
			service.saveUser(lawyer);
			redirectAttributes.addFlashAttribute(ERRMESSAGE,"lawyer added successfully");
		}
		catch(UserAlreadyExistException | IncorrectLoginDetailsException e){	
			redirectAttributes.addFlashAttribute(ERRMESSAGE, e.getMessage());
		}
		return	REDIRECTHOME;
	}
	
	public boolean sessionChecker(HttpSession session) {
		// checks if admin session is active or not
		return (String) session.getAttribute(EMAIL) == null;
	}
}
