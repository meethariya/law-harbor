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
import com.virtusa.exception.CaseRecordNotFoundException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.SlotAlreadyReservedException;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	// handles all user requests
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	private static final String REDIRECTHOME = "redirect:/user/home";
	private static final String REDIRECTBOOKING = "redirect:/user/allBooking";
	private static final String REDIRECTCASE = "redirect:/user/caseRecord";
	private static final String REDIRECTLOGIN = "redirect:/login";

	private static final String HOMEPAGE = "UserHome";
	private static final String CASEPAGE = "UserCase";
	private static final String BOOKINGPAGE = "UserBooking";
	
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
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		homeLoader(model, service.getUser(email).getUsername(), service.getAllLawyer());
		model.addAttribute(ERR, bookingStatus);
		
		return HOMEPAGE;
	}
	
	@GetMapping("/caseRecord")
	public String getCases(Model model, Authentication authentication,
			@ModelAttribute(ERR) String message ) {
		// Shows all cases for the user
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		caseLoader(model, service.getUserCase(email));
		model.addAttribute(ERR, message);		
		return CASEPAGE;
	}
	
	@PostMapping("/bookingForm")
	public String bookAppointment(@Valid @ModelAttribute("booking") BookingDto booking,
			Errors error, RedirectAttributes redirectAttribute, Authentication authentication) {
		// book appointment of a lawyer

		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
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
			@ModelAttribute(ERR) String message ) {
		// returns list of bookings made by user
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		bookingLoader(model, service.getAllBooking(email));
		model.addAttribute(ERR, message);
		
		return BOOKINGPAGE;
	}
	
	@GetMapping("/removeBooking/{bookingId}")
	public String removeBooking(@PathVariable("bookingId") int id,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		// removes booking if it is not confirmed by lawyer
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		try {
			service.removeBooking(id);
			redirectAttributes.addFlashAttribute(ERR, "Appointment cancelled");
		}
		catch(NoBookingFoundException | BookingAlreadyConfirmedException e) {			
			redirectAttributes.addFlashAttribute(ERR, e.getMessage());
		}
		
		return REDIRECTBOOKING;
	}
	
	@PostMapping("/searchByExpertise")
	public String getLawyerByExpertise(@RequestParam("searchField") String searchField,
			@ModelAttribute("booking") BookingDto bookingDto, RedirectAttributes redirectAttributes ,
			Model model, Authentication authentication) {
		// search all lawyers by expertise
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		// if search value is empty
		if(searchField.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, MISSINGVALUE);
			return REDIRECTHOME;
		}
		
		String email = authentication.getName();
		// max edit distance
		int z = Integer.parseInt(getEditDistance());
		List<Lawyer> searchedLawyer = service.getLawyerByExpertise(searchField, z);

		// if resulting list is empty
		if(searchedLawyer.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "No lawyers with expertise "+ searchField);
			return REDIRECTHOME;
		}
		homeLoader(model, service.getUser(email).getUsername(), searchedLawyer);
		return HOMEPAGE;
	}
	
	@GetMapping("/allbooking/{year}")
	public String bookingByYear(@PathVariable("year")String year, Authentication authentication,
			Model model, RedirectAttributes redirectAttributes) {
		// returns list of bookings in given year
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		try {
			bookingLoader(model, service.getBookingByYear(email, year));
		}
		catch(NoBookingFoundException e) {
			redirectAttributes.addFlashAttribute(ERR,e.getMessage());
			return REDIRECTBOOKING;
		}
		return BOOKINGPAGE;
	}

	@GetMapping("/caseRecord/{year}")
	public String caseRecordByYear(@PathVariable("year")String year, Authentication authentication,
			Model model, RedirectAttributes redirectAttributes) {
		// returns list of case record in given year
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		try{
			caseLoader(model, service.getCaseRecordByYear(email, year));
		}
		catch(CaseRecordNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERR,e.getMessage());
			return REDIRECTCASE;
		}
		return CASEPAGE;
	}
	
	@PostMapping("/searchByLawyername")
	public String getLawyerByName(@RequestParam("searchField") String searchField, Model model,
			RedirectAttributes redirectAttributes, Authentication authentication,
			@ModelAttribute("booking") BookingDto bookingDto) {
		// search all lawyers by name

		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		// if search value is empty
		if(searchField.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, MISSINGVALUE);
			return REDIRECTHOME;
		}
		
		String email = authentication.getName();
		// max edit distance
		int z = Integer.parseInt(getEditDistance());
		List<Lawyer> searchedLawyer = service.getLawyerByName(searchField, z);

		// if resulting list is empty
		if(searchedLawyer.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "No lawyers with name "+ searchField);
			return REDIRECTHOME;
		}

		homeLoader(model, service.getUser(email).getUsername(), searchedLawyer);
		return HOMEPAGE;
	}

	@PostMapping("/bookingByLawyername")
	public String getBookingByLawyerName(@RequestParam("searchField") String searchField, Model model,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		// search all booking by lawyer name
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		// if search value is empty
		if(searchField.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, MISSINGVALUE);
			return REDIRECTBOOKING;
		}
		
		String email = authentication.getName();
		// max edit distance
		int z = Integer.parseInt(getEditDistance());
		List<Booking> searchedBooking = service.getBookingByLawyerName(searchField, email, z);
		
		// if resulting list is empty
		if(searchedBooking.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "No appointment with lawyername "+ searchField);
			return REDIRECTBOOKING;
		}
		bookingLoader(model, searchedBooking);
		
		return BOOKINGPAGE;
	}
	
	@PostMapping("/caseRecordByLawyername")
	public String getCaseRecordByLawyerName(@RequestParam("searchField") String searchField, Model model,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		// search all booking by lawyer name
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		// if search value is empty
		if(searchField.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, MISSINGVALUE);
			return REDIRECTCASE;
		}
		
		String email = authentication.getName();
		// max edit distance
		int z = Integer.parseInt(getEditDistance());
		List<CaseRecord> searchedCase = service.getCaseRecordByLawyerName(searchField, email, z);
		
		// if resulting list is empty
		if(searchedCase.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "No case record with lawyername "+ searchField);
			return REDIRECTCASE;
		}
		caseLoader(model, searchedCase);
		
		return CASEPAGE;
	}
	
	public void homeLoader(Model model, String username, List<Lawyer> allLawyer) {
		// loads model attributes for home page
		model.addAttribute("userName", username);
		model.addAttribute("allLawyer", allLawyer);
	}

	public void bookingLoader(Model model, List<Booking> allBooking) {
		// loads model attributes for booking page
		model.addAttribute("allBooking", allBooking);
	}
	
	public void caseLoader(Model model, List<CaseRecord> allCase) {
		// loads model attributes for case page
		model.addAttribute("allCase", allCase);
	}
	
	public String getValueFromProperties() {
		// returns session key value for user email
		return getValueFromProperties("email.user","userEmail");
	}

	public String getEditDistance() {
		// returns session key value for user email
		return getValueFromProperties("editDistance","3");
	}

	public String getValueFromProperties(String key, String defaultValue) {
		// returns properties based on key and default value
		return messageSource.getMessage(key, null, defaultValue, Locale.ENGLISH);
	}
	
	public boolean sessionChecker(Authentication authentication) {
		// checks if user is authenticated. Should redirect login page if null
		return authentication==null;
	}

}
