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

import com.virtusa.dto.BookingDto;
import com.virtusa.dto.LoginUserDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.BookingAlreadyConfirmedException;
import com.virtusa.exception.IncorrectLoginDetailsException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.SlotAlreadyReservedException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	private static final String REDIRECTLOGIN = "redirect:login";
	private static final String REDIRECTHOME = "redirect:home";
	private static final String MISSINGVALUE = "Enter Details";
	private static final String EMAIL = "userEmail";
	
	@Autowired
	UserService service;
	
	public UserController() {
		log.warn("UserController Constructor called");
	}
	
	@GetMapping("/")
	public String index() {
		return REDIRECTLOGIN;
	}
	
	@GetMapping("/register")
	public String getRegisterPage(@ModelAttribute("user") UserDto myuser) {
		return "UserRegistration";
	}
	
	@PostMapping("/registerForm")
	public String postRegisterForm(@Valid @ModelAttribute("user") UserDto myUser,
			Errors error, Model model) {
		try {			
			if(error.hasErrors()) {	
				throw new IncorrectLoginDetailsException(MISSINGVALUE);
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
		return "UserLogin";
	}
	
	@PostMapping("/loginForm")
	public String postLoginForm(@Valid @ModelAttribute("user") LoginUserDto myUser,
			Errors error, Model model, HttpSession session) {
		try {
			if(error.hasErrors()) {			
				throw new IncorrectLoginDetailsException(MISSINGVALUE);
			}
			
			// login user or throws error UserNotFound or IncorrectLoginDetailException
			String role = service.loginUser(myUser);
			if(role.equals("user")) {
				session.setAttribute(EMAIL, myUser.getEmail());		// set session for user
				return	REDIRECTHOME;				
			}
			else if(role.equals("lawyer")) {				
				session.setAttribute("lawyerEmail", myUser.getEmail());		// set session for lawyer
				return	"redirect:lawyer/";				
			}
			else {				
				session.setAttribute("adminEmail", myUser.getEmail());		// set session for admin
				return	"redirect:admin/";				
			}
		}
		catch(IncorrectLoginDetailsException | UserNotFoundException e) {
			model.addAttribute("errMessage", e.getMessage());
			return "UserLogin";
		}
	}
	
	@GetMapping("/home")
	public String getHome(Model model, HttpSession session, 
			@ModelAttribute("bookingStatus") String bookingStatus,
			@ModelAttribute("booking") BookingDto bookingDto) {
		String email = (String) session.getAttribute(EMAIL);
		if(email == null) {
			return REDIRECTLOGIN;
		}
		
		model.addAttribute("userName",service.getUser(email).getUsername());
		model.addAttribute("allLawyer", service.getAllLawyer());
		model.addAttribute("bookingStatus", bookingStatus);
		return "UserHome";
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
	
	@GetMapping("/caseRecord")
	public String getCases(Model model, HttpSession session) {
		// Shows all cases by a user
		String email = (String) session.getAttribute(EMAIL);
		if(email == null) {
			return REDIRECTLOGIN;
		}
		model.addAttribute("allCase", service.getUserCase(email));
		return "UserCase";
	}
	
	@PostMapping("/bookingForm")
	public String bookAppointment(@Valid @ModelAttribute("booking") BookingDto booking,
			Errors error, RedirectAttributes redirectAttribute) {
		// take appointment for user
		String bookingStatus = "bookingStatus";
		if(error.hasErrors()) {
			redirectAttribute.addFlashAttribute(bookingStatus, MISSINGVALUE);
		}
		else {
			try {
				service.bookAppointment(booking);
				redirectAttribute.addFlashAttribute(bookingStatus, "Slot reserved");
			}
			catch(SlotAlreadyReservedException e) {
				redirectAttribute.addFlashAttribute(bookingStatus, "Slot already exists");			
			}
		}
		return REDIRECTHOME;
	}
	
	@GetMapping("/allBooking")
	public String allAppointment(Model model, HttpSession session, 
			@ModelAttribute("message") String message ) {
		// returns list of bookings made by user
		model.addAttribute("allBooking",service.getAllBooking((String) session.getAttribute(EMAIL)));
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
		return "redirect:/allBooking";
	}
}
