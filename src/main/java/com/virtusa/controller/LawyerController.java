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

import com.virtusa.dto.CaseRecordDto;
import com.virtusa.dto.ReportDto;
import com.virtusa.exception.CaseRecordNotFoundException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.ReportAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;
import com.virtusa.service.LawyerService;

@Controller
@RequestMapping("/lawyer")
public class LawyerController {
	// handles all lawyer requests
	
	private static final Logger log = LogManager.getLogger(LawyerController.class);
	private static final String REDIRECTHOME = "redirect:/lawyer/";
	private static final String REDIRECTCASERECORD = "redirect:/lawyer/caseRecord";
	private static final String REDIRECTLOGIN = "redirect:/login";
	
	private static final String LAWYERHOMEPAGE = "LawyerHome";
	private static final String LAWYERCASEPAGE = "LawyerCase";

	private static final String ERR = "errMessage";
	
	public LawyerController() {
		log.warn("LawyerController Constructor Called");
	}
	@Autowired
	LawyerService service;
	@Autowired
	MessageSource messageSource;
	
	@GetMapping("/")
	public String home(Model model, Authentication authentication, @ModelAttribute(ERR) String errMessage) {
		// Lawyer home
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		homeLoader(model,service.getAllAppointment(email), service.getLawyer(email).getUsername());
		model.addAttribute(ERR, errMessage);
		
		return LAWYERHOMEPAGE;
	}
		
	@GetMapping("/approveBooking/{bookingId}")
	public String approveBooking(@PathVariable("bookingId") int id, RedirectAttributes redirectAttribute,
			Authentication authentication) {
		// approve an appointment
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		try {
			service.approveBooking(id);
		}
		catch(NoBookingFoundException e) {
			redirectAttribute.addFlashAttribute(ERR, e.getMessage());
		}
		
		return REDIRECTHOME;
	}

	@GetMapping("/cancelBooking/{bookingId}")
	public String cancelBooking(@PathVariable("bookingId") int id,
			RedirectAttributes redirectAttribute, Authentication authentication) {
		// cancel an appointment
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		try {			
			service.cancelBooking(id);
		}
		catch(NoBookingFoundException e) {
			redirectAttribute.addFlashAttribute(ERR, e.getMessage());
		}
		
		return REDIRECTHOME;
	}
	
	@GetMapping("/caseRecord")
	public String getCaseRecord(@ModelAttribute("case")CaseRecordDto caseRecordDto,
			@ModelAttribute(ERR)String errMessage, Model model, Authentication authentication) {
		// get list of all case records by the lawyer
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		caseLoader(model, service.getAllCase(email));
		model.addAttribute(ERR, errMessage);
		
		return LAWYERCASEPAGE;
	}
	
	@PostMapping("/caseRecord")
	public String addCaseRecord(@Valid @ModelAttribute("case")CaseRecordDto caseRecordDto, 
			Errors error, Authentication authentication, RedirectAttributes redirectAttributes) {
		// add a case record
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		if(error.hasErrors()) {			
			redirectAttributes.addFlashAttribute(ERR, "Enter details correctly");
		}
		else {			
			String email = authentication.getName();
			caseRecordDto.setIssuedBy(service.getLawyer(email));
			
			try {
				String role = messageSource.getMessage("role.user", null, "user", new Locale("en"));
				service.addCaseRecord(caseRecordDto, role);
				redirectAttributes.addFlashAttribute(ERR, "Case Record created successfully");
			}
			catch(UserNotFoundException e) {
				redirectAttributes.addFlashAttribute(ERR, e.getMessage());
			}
		}
		
		return "redirect:caseRecord";
	}
	
	@GetMapping("/caseRecord/{caseRecordId}")
	public String deleteCaseRecord(@PathVariable("caseRecordId") int caseRecordId,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		// deletes a case record
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		try {
			service.deleteCaseRecord(caseRecordId);
			redirectAttributes.addFlashAttribute(ERR, "Case Record deleted successfully");
		}
		catch(CaseRecordNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERR, e.getMessage());
		}
		return REDIRECTCASERECORD;
	}
	
	@PostMapping("caseRecord/{caseRecordId}")
	public String editCaseRecord(@PathVariable("caseRecordId") int caseRecordId,
			@Valid @ModelAttribute("case")CaseRecordDto caseRecordDto, Errors error,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		// edit case record
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		if(error.hasErrors()) {	
			redirectAttributes.addFlashAttribute(ERR, "Enter details correctly");			
		}
		else {			
			try {
				service.editCaseRecord(caseRecordDto, caseRecordId);
				redirectAttributes.addFlashAttribute(ERR, "Case Record edited successfully");
			}
			catch(CaseRecordNotFoundException e) {
				redirectAttributes.addFlashAttribute(ERR, e.getMessage());			
			}
		}
		return REDIRECTCASERECORD;
	}
	
	
	@GetMapping("report/{bookingId}")
	public String reportPage(@PathVariable("bookingId") int bookingId,
			@ModelAttribute("report") ReportDto report, 
			Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
		// Create Report page
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		model.addAttribute("bookingId", bookingId);
		
		try {			
			// get client and lawyer for a given booking. find all case records for same combination.
			Booking booking = service.getBooking(bookingId);
			User user = booking.getClient();
			Lawyer lawyer = booking.getLawyer();
			model.addAttribute("userCaseRecord",service.getCaseOfUser(user, lawyer));
		}
		catch(NoBookingFoundException | CaseRecordNotFoundException e){
			redirectAttributes.addFlashAttribute(ERR, e.getMessage());
			return REDIRECTHOME;
		}
		
		return "LawyerReport";
	}
	
	@PostMapping("report")
	public String addReport(@Valid @ModelAttribute("report") ReportDto reportDto,
			Errors error, RedirectAttributes redirectAttributes, Authentication authentication) {
		// save report
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		if(error.hasErrors()) {			
			redirectAttributes.addFlashAttribute(ERR, "Select Minimum one case record");
			return "redirect:report/"+reportDto.getBookingId();
		}
		
		// setting lawyer from session email
		String email = authentication.getName();
		reportDto.setLawyer(service.getLawyer(email));
		
		try {
			service.addReport(reportDto);
			redirectAttributes.addFlashAttribute(ERR, "Report generated Successfully");
		}
		catch(ReportAlreadyExistException e){
			redirectAttributes.addFlashAttribute(ERR, e.getMessage());
		}
		
		return REDIRECTHOME;
	}
	
	@GetMapping("/booking/{year}")
	public String bookingByYear(@PathVariable("year")String year, Authentication authentication,
			Model model, RedirectAttributes redirectAttributes) {
		// returns list of bookings in given year
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		try {
			homeLoader(model,service.getBookingByYear(email, year), service.getLawyer(email).getUsername());
		}
		catch(NoBookingFoundException e) {
			redirectAttributes.addFlashAttribute(ERR,e.getMessage());
			return REDIRECTHOME;
		}
		return LAWYERHOMEPAGE;
	}
	
	@GetMapping("/caseRecordByYear/{year}")
	public String caseRecordByYear(@PathVariable("year")String year, Authentication authentication,
			@ModelAttribute("case")CaseRecordDto caseRecordDto, 
			Model model, RedirectAttributes redirectAttributes) {
		// returns list of case record in given year
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		try{
			caseLoader(model, service.getCaseRecordByYear(email, year));			
		}
		catch(CaseRecordNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERR,e.getMessage());
			return REDIRECTCASERECORD;
		}
		return LAWYERCASEPAGE;
	}

	@PostMapping("/searchByUsername")
	public String bookingByUsername(@RequestParam("username") String username, Authentication authentication,
			Model model, RedirectAttributes redirectAttributes) {
		// returns list of case record in given year
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		int z = Integer.parseInt(messageSource.getMessage("editDistance", null, "3", Locale.ENGLISH));
		try{
			homeLoader(model,service.getBookingByUsername(email, username, z), service.getLawyer(email).getUsername());
		}
		catch(NoBookingFoundException e) {
			redirectAttributes.addFlashAttribute(ERR,e.getMessage());
			return REDIRECTHOME;
		}
		return LAWYERHOMEPAGE;
	}

	@PostMapping("/caseRecordByUsername")
	public String caseRecordByUsername(@RequestParam("username") String username, Authentication authentication,
			Model model, RedirectAttributes redirectAttributes, @ModelAttribute("case")CaseRecordDto caseRecordDto) {
		// returns list of case record in given year
		
		if(sessionChecker(authentication)) return REDIRECTLOGIN;
		
		String email = authentication.getName();
		int z = Integer.parseInt(messageSource.getMessage("editDistance", null, "3", Locale.ENGLISH));
		try{
			caseLoader(model, service.getCaseRecordByUsername(email, username, z));
		}
		catch(CaseRecordNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERR,e.getMessage());
			return REDIRECTCASERECORD;
		}
		return LAWYERCASEPAGE;
	}
	
	public String getEmailFromProperties() {
		// returns session key value for admin email
		return messageSource.getMessage("email.lawyer", null, "lawyerEmail", Locale.ENGLISH);
	}
	
	public void homeLoader(Model model, List<Booking> allBooking, String username) {
		// loads model attributes for homepage
		model.addAttribute("allBooking",allBooking);			
		model.addAttribute("username",username);
	}
	
	public void caseLoader(Model model, List<CaseRecord> allCase) {
		// loads model attributes for case page
		model.addAttribute("allCaseRecord",allCase);	
	}
	
	public boolean sessionChecker(Authentication authentication) {
		return authentication==null;
	}
}
