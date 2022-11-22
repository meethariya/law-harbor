package com.virtusa.controllertest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.virtusa.controller.GeneralController;

@RunWith(SpringRunner.class)
@WebAppConfiguration
class GeneralControllerTest {
	
	private static final Logger logger = LogManager.getLogger(GeneralControllerTest.class);
	private MockMvc mockMvc;
	
	@BeforeEach
	public void init() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		mockMvc = MockMvcBuilders
						.standaloneSetup(new GeneralController())
						.setViewResolvers(resolver)
						.build();
		logger.info("Initialised test");
	}
	
	@Test
	void indexTest() throws Exception {
		mockMvc.perform(get("/"))
			    .andExpect(status().is3xxRedirection())
			    .andExpect(redirectedUrl("postLogin"))
			    .andDo(log());
	}
	
	@Test
	void loginPage() throws Exception{
		mockMvc.perform(get("/login"))
			    .andExpect(status().isOk())
			    .andExpect(view().name("UserLogin"))
			    .andDo(log());
	}
	
	@Test
	void registrationPage() throws Exception{
		mockMvc.perform(get("/register"))
			    .andExpect(status().isOk())
			    .andExpect(view().name("UserRegistration"))
			    .andDo(log());
	}
	
	@Test
	void postRegistrationPage1() throws Exception {
		mockMvc.perform(post("/register").param("username", "myUsername"))
				.andExpect(status().isOk())
				.andExpect(model().hasErrors())
				.andDo(log());
	}
}
