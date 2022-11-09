package com.virtusa.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.virtusa.dto.CaseRecordDto;
import com.virtusa.dto.ReportDto;
import com.virtusa.exception.CaseRecordNotFoundException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.ReportAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Booking;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;
import com.virtusa.service.LawyerService;

@Controller
@RequestMapping("/lawyer")
public class LawyerController {
	// handles all lawyer requests
	
	private static final Logger log = LogManager.getLogger(LawyerController.class);
	private static final String REDIRECTLOGIN = "redirect:/login";
	private static final String REDIRECTHOME = "redirect:/lawyer/";
	private static final String ERR = "errMessage";
	
	public LawyerController() {
		log.warn("LawyerController Constructor Called");
	}
	@Autowired
	LawyerService service;
	@Autowired
	MessageSource messageSource;
	
	@GetMapping("/")
	public String home(Model model, HttpSession session, @ModelAttribute(ERR) String errMessage) {
		// Lawyer home
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		String email = (String) session.getAttribute(getEmailFromProperties());
		model.addAttribute("username",service.getLawyer(email).getUsername());
		model.addAttribute("allBooking", service.getAllAppointment(email));
		model.addAttribute("err", errMessage);
		
		return "LawyerHome";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		// logout lawyer
		String email = (String) session.getAttribute(getEmailFromProperties());
		
		if(email != null) {
			service.logoutUser(email);
			session.removeAttribute(getEmailFromProperties());						// remove session on logout
		}	
		redirectAttributes.addFlashAttribute(ERR,"loggged out");
		return REDIRECTLOGIN;
	}
	
	@GetMapping("/approveBooking/{bookingId}")
	public String approveBooking(@PathVariable("bookingId") int id, RedirectAttributes redirectAttribute, 
			HttpSession session) {
		// approve an appointment
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
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
			RedirectAttributes redirectAttribute, HttpSession session) {
		// cancel an appointment
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
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
			@ModelAttribute(ERR)String errMessage, Model model, HttpSession session) {
		// get list of all case records by the lawyer
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		String email = (String) session.getAttribute(getEmailFromProperties());
		model.addAttribute("allCaseRecord", service.getAllCase(email));
		model.addAttribute(ERR, errMessage);
		
		return "LawyerCase";
	}
	
	@PostMapping("/caseRecord")
	public String addCaseRecord(@Valid @ModelAttribute("case")CaseRecordDto caseRecordDto, 
			Errors error, HttpSession session, RedirectAttributes redirectAttributes) {
		// add a case record

		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		if(error.hasErrors()) {			
			redirectAttributes.addFlashAttribute(ERR, "Enter details correctly");
		}
		else {			
			String email = (String) session.getAttribute(getEmailFromProperties());
			caseRecordDto.setIssuedBy(service.getLawyer(email));
			
			try {			
				service.addCaseRecord(caseRecordDto);
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
			RedirectAttributes redirectAttributes, HttpSession session) {
		// deletes a case record
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		try {
			service.deleteCaseRecord(caseRecordId);
			redirectAttributes.addFlashAttribute(ERR, "Case Record deleted successfully");
		}
		catch(CaseRecordNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERR, e.getMessage());
		}
		return "redirect:/lawyer/caseRecord";
	}
	
	@PostMapping("caseRecord/{caseRecordId}")
	public String editCaseRecord(@PathVariable("caseRecordId") int caseRecordId,
			@Valid @ModelAttribute("case")CaseRecordDto caseRecordDto, Errors error,
			RedirectAttributes redirectAttributes, HttpSession session) {
		// edit case record
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
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
		return "redirect:/lawyer/caseRecord";
	}
	
	
	@GetMapping("report/{bookingId}")
	public String reportPage(@PathVariable("bookingId") int bookingId,
			@ModelAttribute("report") ReportDto report, 
			Model model, RedirectAttributes redirectAttributes, HttpSession session) {
		// Create Report page
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
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
			Errors error, RedirectAttributes redirectAttributes, HttpSession session) {
		// save report
		
		if(sessionChecker(session)) return REDIRECTLOGIN;
		
		if(error.hasErrors()) {			
			redirectAttributes.addFlashAttribute(ERR, "Select Minimum one case record");
			return "redirect:report/"+reportDto.getBookingId();
		}
		
		// setting lawyer from session email
		String email = (String) session.getAttribute(getEmailFromProperties());
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
	
	public boolean sessionChecker(HttpSession session) {
		// checks if lawyer session is active or not
		return (String) session.getAttribute(getEmailFromProperties()) == null;
	}
	
	public String getEmailFromProperties() {
		// returns session key value for admin email
		return messageSource.getMessage("email.lawyer", null, "lawyerEmail", new Locale("en"));
	}
}
