package com.virtusa.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.service.LawyerService;

@Controller
@RequestMapping("/lawyer")
public class LawyerController {
	private static final Logger log = LogManager.getLogger(LawyerController.class);
	private static final String REDIRECTLOGIN = "redirect:/login";
	private static final String EMAIL = "lawyerEmail";
	
	public LawyerController() {
		log.warn("LawyerController Constructor Called");
	}
	@Autowired
	LawyerService service;
	
	@GetMapping("/")
	public String home(Model model, HttpSession session, @ModelAttribute("errMessage") String errMessage) {
		String email = (String) session.getAttribute(EMAIL);
		if(email == null) {
			return REDIRECTLOGIN;
		}
		model.addAttribute("username",service.getLawyer(email).getUsername());
		model.addAttribute("allBooking", service.getAllAppointment(email));
		model.addAttribute("err", errMessage);
		return "LawyerHome";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		String email = (String) session.getAttribute(EMAIL);
		if(email != null) {
			service.logoutUser(email);
			session.removeAttribute(EMAIL);						// remove session on logout
		}		
		return REDIRECTLOGIN;
	}
	
	@GetMapping("/approveBooking/{bookingId}")
	public String approveBooking(@PathVariable("bookingId") int id, RedirectAttributes redirectAttribute, 
			HttpSession session) {
		String email = (String) session.getAttribute(EMAIL);
		if(email == null) {
			return REDIRECTLOGIN;
		}
		try {			
			service.approveBooking(id);
		}
		catch(NoBookingFoundException e) {
			redirectAttribute.addFlashAttribute("errMessage", e.getMessage());
		}
		return "redirect:/lawyer/";
	}

	@GetMapping("/cancelBooking/{bookingId}")
	public String cancelBooking(@PathVariable("bookingId") int id, RedirectAttributes redirectAttribute, 
			HttpSession session) {
		String email = (String) session.getAttribute(EMAIL);
		if(email == null) {
			return REDIRECTLOGIN;
		}
		try {			
			service.cancelBooking(id);
		}
		catch(NoBookingFoundException e) {
			redirectAttribute.addFlashAttribute("errMessage", e.getMessage());
		}
		return "redirect:/lawyer/";
	}
}
