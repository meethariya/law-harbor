package com.virtusa.controller;

import java.util.List;
import java.util.Locale;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.virtusa.dto.BookingDto;
import com.virtusa.exception.BookingAlreadyConfirmedException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.SlotAlreadyReservedException;
import com.virtusa.model.Lawyer;
import com.virtusa.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	// handles all user requests
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	private static final String REDIRECTHOME = "redirect:/user/home";
	private static final String MISSINGVALUE = "Enter Valid Details";
	private static final String ERR = "errMessage";
	
	@Autowired
	UserService service;

	@Autowired
	MessageSource messageSource;
	
	public UserController() {
		log.warn("UserController Constructor called");
	}
	
	@GetMapping("/home")
	public String getHome(Model model, Authentication authentication, 
			@ModelAttribute(ERR) String bookingStatus,
			@ModelAttribute("booking") BookingDto bookingDto) {
		// User home page
		
		String email = authentication.getName();
		model.addAttribute("userName",service.getUser(email).getUsername());
		model.addAttribute("allLawyer", service.getAllLawyer());		
		model.addAttribute(ERR, bookingStatus);
		
		log.info("in home loading Home page");
		return "UserHome";
	}
	
	@GetMapping("/caseRecord")
	public String getCases(Model model, Authentication authentication) {
		// Shows all cases for the user
		
		String email = authentication.getName();
		model.addAttribute("allCase", service.getUserCase(email));
		
		return "UserCase";
	}
	
	@PostMapping("/bookingForm")
	public String bookAppointment(@Valid @ModelAttribute("booking") BookingDto booking,
			Errors error, RedirectAttributes redirectAttribute) {
		// book appointment of a lawyer

		if(error.hasErrors()) {
			redirectAttribute.addFlashAttribute(ERR, MISSINGVALUE);
		}
		else {
			try {
				service.bookAppointment(booking);
				redirectAttribute.addFlashAttribute(ERR, "Slot reserved");
			}
			catch(SlotAlreadyReservedException e) {
				redirectAttribute.addFlashAttribute(ERR, "Slot already exists");			
			}
		}
		return REDIRECTHOME;
	}
	
	@GetMapping("/allBooking")
	public String allAppointment(Model model, Authentication authentication, 
			@ModelAttribute("message") String message ) {
		// returns list of bookings made by user
		
		String email = authentication.getName();
		model.addAttribute("allBooking",service.getAllBooking(email));
		model.addAttribute("removeBookingMessage", message);
		
		return "UserBooking";
	}
	
	@GetMapping("/removeBooking/{bookingId}")
	public String removeBooking(@PathVariable("bookingId") int id,
			RedirectAttributes redirectAttributes) {
		// removes booking if it is not confirmed by lawyer
		
		try {
			service.removeBooking(id);
			redirectAttributes.addFlashAttribute("message", "Appointment cancelled");
		}
		catch(NoBookingFoundException | BookingAlreadyConfirmedException e) {			
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/user/allBooking";
	}
	
	@PostMapping("/searchByExpertise")
	public String getLawyerByExpertise(@RequestParam("searchField") String searchField,
			@ModelAttribute("booking") BookingDto bookingDto, RedirectAttributes redirectAttributes ,
			Model model, Authentication authentication) {
		// search all lawyers by expertise
		
		// if search value is empty
		if(searchField.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "Enter Search value");
			return REDIRECTHOME;
		}
		
		String email = authentication.getName();
		// max edit distance
		int z = Integer.parseInt(messageSource.getMessage("editDistance", null, "3", Locale.ENGLISH));
		List<Lawyer> searchedLawyer = service.getLawyerByExpertise(searchField, z);

		// if resulting list is empty
		if(searchedLawyer.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "No lawyers with expertise "+ searchField);
			return REDIRECTHOME;
		}

		model.addAttribute("userName",service.getUser(email).getUsername());
		model.addAttribute("allLawyer", searchedLawyer);
		
		return "UserHome";
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
