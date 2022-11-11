package com.virtusa.controller;

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
	public String home(Model model, Authentication authentication, @ModelAttribute(ERR) String errMessage) {
		// Lawyer home
		
		String email = authentication.getName();
		model.addAttribute("username",service.getLawyer(email).getUsername());
		model.addAttribute("allBooking", service.getAllAppointment(email));
		model.addAttribute("err", errMessage);
		
		return "LawyerHome";
	}
		
	@GetMapping("/approveBooking/{bookingId}")
	public String approveBooking(@PathVariable("bookingId") int id, RedirectAttributes redirectAttribute) {
		// approve an appointment
		
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
			RedirectAttributes redirectAttribute) {
		// cancel an appointment
		
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
		
		String email = authentication.getName();
		model.addAttribute("allCaseRecord", service.getAllCase(email));
		model.addAttribute(ERR, errMessage);
		
		return "LawyerCase";
	}
	
	@PostMapping("/caseRecord")
	public String addCaseRecord(@Valid @ModelAttribute("case")CaseRecordDto caseRecordDto, 
			Errors error, Authentication authentication, RedirectAttributes redirectAttributes) {
		// add a case record
		
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
			RedirectAttributes redirectAttributes) {
		// deletes a case record
		
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
			RedirectAttributes redirectAttributes) {
		// edit case record
		
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
			Model model, RedirectAttributes redirectAttributes) {
		// Create Report page
		
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
	
	public String getEmailFromProperties() {
		// returns session key value for admin email
		return messageSource.getMessage("email.lawyer", null, "lawyerEmail", new Locale("en"));
	}
}
