package com.virtusa.dao;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

@Repository
public class UserDao {
	private static final Logger log = LogManager.getLogger(UserDao.class);

	public UserDao() {
		log.warn("UserDao Constructor called");
	}

	@Autowired
	SessionFactory factory;

	public void saveUser(User user) {
		// Saves User to database
		factory.getCurrentSession().persist(user);
	}

	public void saveLawyer(Lawyer user) {
		// Saves Lawyer to database
		factory.getCurrentSession().persist(user);
	}

	public User getUser(String email) {
		// Returns user matching with email. Returns Null if no such record present
		Session session = factory.getCurrentSession();
		Query<User> query = session.createQuery("from User where email='"+email+"'", User.class);
		return query.uniqueResult();
	}

	public Lawyer getLawyer(String email) {
		// Returns user matching with email. Returns Null if no such record present
		Session session = factory.getCurrentSession();
		User user = getUser(email);
		Query<Lawyer> query = session.createQuery("from Lawyer where id = '"+user.getId()+"'", Lawyer.class);
		return query.uniqueResult();
	}
	
	public void userActiveStatusUpdate(User user) {
		// changes user's active attribute to online
		factory.getCurrentSession().update(user);
	}
	
	public List<Lawyer> getAllLawyer(){
		// returns list of lawyers
		Session session = factory.getCurrentSession();
		Query<Lawyer> allLawyer = session.createQuery("from Lawyer", Lawyer.class);
		return allLawyer.getResultList();
	}
	
	public User getUserByNumber(String mobileNumber) {
		// Finds user by mobile number
		Session session = factory.getCurrentSession();
		Query<User> query = session.createQuery("from User where mobile_number = '" + 
								mobileNumber + "'", User.class);
		return query.uniqueResult();
	}
	
	public List<CaseRecord> getUserCase(User user){
		// return list of cases registered by given user
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where user_id = '"
						+user+"'", CaseRecord.class);
		return query.getResultList();
	}
	
	public boolean existingBooking(Lawyer lawyer, Date bookingDate) {
		// returns boolean value to check if any appointment is reserved
		// for given lawyer for given date and time.
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where Lawyer = '" + 
					lawyer.getId()+"' and date='"+bookingDate+"'", Booking.class);
		return query.uniqueResult()!=null;
	}
	public void bookAppointment(Booking booking) {
		// books an appointment
		factory.getCurrentSession().save(booking);
	}

	public List<Booking> getAllBooking(User user) {
		// returns List of Bookings by given user
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where client='"+user.getId()+"'", Booking.class);
		return query.getResultList();
	}
	
	public Booking getBooking(int id) {
		// returns booking object provided id
		return factory.getCurrentSession().get(Booking.class, id);
	}

	public void removeBooking(Booking booking) {
		// removes/cancels a booking made by user
		factory.getCurrentSession().remove(booking);
	}
}

