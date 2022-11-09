package com.virtusa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import com.virtusa.dto.LoginUserDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.BookingAlreadyConfirmedException;
import com.virtusa.exception.IncorrectLoginDetailsException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.SlotAlreadyReservedException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Lawyer;
import com.virtusa.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {
	// handles all user requests
	// handles general login and registration requests
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	private static final String REDIRECTLOGIN = "redirect:login";
	private static final String REDIRECTHOME = "redirect:home";
	private static final String MISSINGVALUE = "Enter Valid Details";
	private static final String ERR = "errMessage";
	
	@Autowired
	UserService service;

	@Autowired
	MessageSource messageSource;
	
	public UserController() {
		log.warn("UserController Constructor called");
	}
	
	@GetMapping("/")
	public String index() {
		// Index Page
		return REDIRECTLOGIN;
	}
	
	@GetMapping("/register")
	public String getRegisterPage(@ModelAttribute("user") UserDto myuser, Model model) {
		// registration page
		model.addAttribute("roles",getAllRoles());
		return "UserRegistration";
	}
	
	@PostMapping("/registerForm")
	public String postRegisterForm(@Valid @ModelAttribute("user") UserDto myUser,
			Errors error, Model model, RedirectAttributes redirectAttributes) {
		// registration form submission
		
		try {			
			if(error.hasErrors()) {	
				throw new IncorrectLoginDetailsException(MISSINGVALUE);
			}
			// registers users or throws error if user already exist
			service.saveUser(myUser);
			redirectAttributes.addFlashAttribute(ERR,"Account registerd");
		}
		catch(UserAlreadyExistException | IncorrectLoginDetailsException e){	
			model.addAttribute(ERR, e.getMessage());
			model.addAttribute("roles",getAllRoles());
			return "UserRegistration";
		}
		
		return	REDIRECTLOGIN;
	}

	@GetMapping("/login")
	public String getLoginPage(@ModelAttribute("user") LoginUserDto myuser) {
		// login page
		return "UserLogin";
	}
	
	@PostMapping("/loginForm")
	public String postLoginForm(@Valid @ModelAttribute("user") LoginUserDto myUser,
			Errors error, Model model, HttpSession session) {
		// login form submission
		
		try {
			if(error.hasErrors()) {			
				throw new IncorrectLoginDetailsException(MISSINGVALUE);
			}			
			
			// login user or throws error UserNotFound or IncorrectLoginDetailException
			String role = service.loginUser(myUser);
			
			if(role.equals(getValueFromProperties("role.user","user"))) {
				session.setAttribute(getValueFromProperties(), myUser.getEmail());	// set session for user
				return	REDIRECTHOME;				
			}
			
			else if(role.equals(getValueFromProperties("role.lawyer","lawyer"))) {
				String email = getValueFromProperties("email.lawyer","lawyerEmail");
				session.setAttribute(email, myUser.getEmail());						// set session for lawyer
				return	"redirect:lawyer/";				
			}
			
			else {				
				String email = getValueFromProperties("email.admin","adminEmail");
				session.setAttribute(email, myUser.getEmail());						// set session for admin
				return	"redirect:admin/";				
			}
		}
		catch(IncorrectLoginDetailsException | UserNotFoundException e) {
			model.addAttribute(ERR, e.getMessage());
			return "UserLogin";
		}
	}
	
	@GetMapping("/home")
	public String getHome(Model model, HttpSession session, 
			@ModelAttribute(ERR) String bookingStatus,
			@ModelAttribute("booking") BookingDto bookingDto) {
		// User home page
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		String email = (String) session.getAttribute(getValueFromProperties());
		model.addAttribute("userName",service.getUser(email).getUsername());
		model.addAttribute("allLawyer", service.getAllLawyer());		
		model.addAttribute(ERR, bookingStatus);
		
		return "UserHome";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		// logout user

		String email = (String) session.getAttribute(getValueFromProperties());
		
		if(email != null) {
			service.logoutUser(email);
			session.removeAttribute(getValueFromProperties());						// remove session on logout
		}		
		redirectAttributes.addFlashAttribute(ERR,"loggged out");
		return REDIRECTLOGIN;
	}
	
	@GetMapping("/caseRecord")
	public String getCases(Model model, HttpSession session) {
		// Shows all cases for the user
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		String email = (String) session.getAttribute(getValueFromProperties());
		model.addAttribute("allCase", service.getUserCase(email));
		
		return "UserCase";
	}
	
	@PostMapping("/bookingForm")
	public String bookAppointment(@Valid @ModelAttribute("booking") BookingDto booking,
			Errors error, RedirectAttributes redirectAttribute, HttpSession session) {
		// book appointment of a lawyer
		
		if(sessionChecker(session)) return REDIRECTLOGIN;

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
	public String allAppointment(Model model, HttpSession session, 
			@ModelAttribute("message") String message ) {
		// returns list of bookings made by user

		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		String email = (String) session.getAttribute(getValueFromProperties());
		model.addAttribute("allBooking",service.getAllBooking(email));
		model.addAttribute("removeBookingMessage", message);
		
		return "UserBooking";
	}
	
	@GetMapping("/removeBooking/{bookingId}")
	public String removeBooking(@PathVariable("bookingId") int id,
			RedirectAttributes redirectAttributes, HttpSession session) {
		// removes booking if it is not confirmed by lawyer

		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		try {
			service.removeBooking(id);
			redirectAttributes.addFlashAttribute("message", "Appointment cancelled");
		}
		catch(NoBookingFoundException | BookingAlreadyConfirmedException e) {			
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/allBooking";
	}
	
	@PostMapping("/searchByExpertise")
	public String getLawyerByExpertise(@RequestParam("searchField") String searchField,
			@ModelAttribute("booking") BookingDto bookingDto, RedirectAttributes redirectAttributes ,
			Model model, HttpSession session) {
		// search all lawyers by expertise
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		// if search value is empty
		if(searchField.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "Enter Search value");
			return REDIRECTHOME;
		}
		
		String email = (String) session.getAttribute(getValueFromProperties());
		List<Lawyer> searchedLawyer = service.getLawyerByExpertise(searchField);

		// if resulting list is empty
		if(searchedLawyer.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERR, "No lawyers with expertise "+ searchField);
			return REDIRECTHOME;
		}

		model.addAttribute("userName",service.getUser(email).getUsername());
		model.addAttribute("allLawyer", searchedLawyer);
		
		return "UserHome";
	}
	
	public boolean sessionChecker(HttpSession session) {
		// checks if lawyer session is active or not
		return (String) session.getAttribute(getValueFromProperties()) == null;
	}
	
	public String getValueFromProperties() {
		// returns session key value for user email
		return getValueFromProperties("email.user","userEmail");
	}

	public String getValueFromProperties(String key, String defaultValue) {
		// returns properties based on key and default value
		return messageSource.getMessage(key, null, defaultValue, Locale.ENGLISH);
	}
	
	public List<String> getAllRoles(){
		// collects all roles from properties file and create hashmap to show in registration
		List<String> propertiesList = new ArrayList<>();
		propertiesList.add(getValueFromProperties("role.user","user"));
		propertiesList.add(getValueFromProperties("role.lawyer","lawyer"));
		
		return propertiesList;
	}
}
