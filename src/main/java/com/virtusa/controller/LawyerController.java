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

import com.virtusa.dto.CaseRecordDto;
import com.virtusa.exception.CaseRecordNotFound;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.service.LawyerService;

@Controller
@RequestMapping("/lawyer")
public class LawyerController {
	private static final Logger log = LogManager.getLogger(LawyerController.class);
	private static final String REDIRECTLOGIN = "redirect:/login";
	private static final String EMAIL = "lawyerEmail";
	private static final String ERR = "errMessage";
	
	public LawyerController() {
		log.warn("LawyerController Constructor Called");
	}
	@Autowired
	LawyerService service;
	
	@GetMapping("/")
	public String home(Model model, HttpSession session, @ModelAttribute(ERR) String errMessage) {
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		String email = (String) session.getAttribute(EMAIL);
		
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
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		
		try {			
			service.approveBooking(id);
		}
		catch(NoBookingFoundException e) {
			redirectAttribute.addFlashAttribute(ERR, e.getMessage());
		}
		
		return "redirect:/lawyer/";
	}

	@GetMapping("/cancelBooking/{bookingId}")
	public String cancelBooking(@PathVariable("bookingId") int id,
			RedirectAttributes redirectAttribute, HttpSession session) {
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		
		try {			
			service.cancelBooking(id);
		}
		catch(NoBookingFoundException e) {
			redirectAttribute.addFlashAttribute(ERR, e.getMessage());
		}
		
		return "redirect:/lawyer/";
	}
	
	@GetMapping("/caseRecord")
	public String getCaseRecord(@ModelAttribute("case")CaseRecordDto caseRecordDto,
			@ModelAttribute(ERR)String errMessage, Model model, HttpSession session) {
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		
		String email = (String) session.getAttribute(EMAIL);
		model.addAttribute("allCaseRecord", service.getAllCase(email));
		model.addAttribute(ERR, errMessage);
		
		return "LawyerCase";
	}
	
	@PostMapping("/caseRecord")
	public String addCaseRecord(@Valid @ModelAttribute("case")CaseRecordDto caseRecordDto, 
			Errors error, HttpSession session, RedirectAttributes redirectAttributes) {
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		if(error.hasErrors()) {			
			redirectAttributes.addFlashAttribute(ERR, "Enter details correctly");
		}
		String email = (String) session.getAttribute(EMAIL);
		caseRecordDto.setIssuedBy(service.getLawyer(email));
		
		try {			
			service.addCaseRecord(caseRecordDto);
			redirectAttributes.addFlashAttribute(ERR, "Case Record created successfully");
		}
		catch(UserNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERR, e.getMessage());
		}
		
		return "redirect:caseRecord";
	}
	
	@GetMapping("/caseRecord/{caseRecordId}")
	public String deleteCaseRecord(@PathVariable("caseRecordId") int caseRecordId,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		
		try {
			service.deleteCaseRecord(caseRecordId);
			redirectAttributes.addFlashAttribute(ERR, "Case Record deleted successfully");
		}
		catch(CaseRecordNotFound e) {
			redirectAttributes.addFlashAttribute(ERR, e.getMessage());
		}
		return "redirect:/lawyer/caseRecord";
	}
	
	@PostMapping("caseRecord/{caseRecordId}")
	public String editCaseRecord(@PathVariable("caseRecordId") int caseRecordId,
			@Valid @ModelAttribute("case")CaseRecordDto caseRecordDto, Errors error,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if(sessionChecker(session)) {
			return REDIRECTLOGIN;
		}
		if(error.hasErrors()) {			
			log.info(error.getAllErrors());
			redirectAttributes.addFlashAttribute(ERR, "Enter details correctly");			
		}
		else {			
			try {
				service.editCaseRecord(caseRecordDto, caseRecordId);
				redirectAttributes.addFlashAttribute(ERR, "Case Record edited successfully");
			}
			catch(CaseRecordNotFound e) {
				redirectAttributes.addFlashAttribute(ERR, e.getMessage());			
			}
		}
		return "redirect:/lawyer/caseRecord";
	}
	
	
	public boolean sessionChecker(HttpSession session) {
		// checks if lawyer session is active or not
		return (String) session.getAttribute(EMAIL) == null;
	}
}
