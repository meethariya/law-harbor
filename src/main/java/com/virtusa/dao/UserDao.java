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
public class UserDao implements UserDaoInterface{
	// DAO class for user and general login/registration transactions
	
	private static final Logger log = LogManager.getLogger(UserDao.class);

	public UserDao() {
		log.warn("UserDao Constructor called");
	}

	@Autowired
	SessionFactory factory;

	@Override
	public void saveUser(User user) {
		// Saves User to database
		factory.getCurrentSession().persist(user);
	}

	@Override
	public void saveLawyer(Lawyer user) {
		// Saves Lawyer to database
		factory.getCurrentSession().persist(user);
	}

	@Override
	public User getUser(String email) {
		// Returns user matching with email. Returns Null if no such record present
		Session session = factory.getCurrentSession();
		Query<User> query = session.createQuery("from User where email = :email", User.class);
		query.setParameter("email", email);
		return query.uniqueResult();
	}

	@Override
	public Lawyer getLawyer(String email) {
		// Returns user matching with email. Returns Null if no such record present
		Session session = factory.getCurrentSession();
		User user = getUser(email);
		Query<Lawyer> query = session.createQuery("from Lawyer where id = :id", Lawyer.class);
		query.setParameter("id",user.getId());
		return query.uniqueResult();
	}
	
	@Override
	public void userActiveStatusUpdate(User user) {
		// changes user's active attribute to online
		factory.getCurrentSession().update(user);
	}
	
	@Override
	public List<Lawyer> getAllLawyer(){
		// returns list of lawyers
		Session session = factory.getCurrentSession();
		Query<Lawyer> allLawyer = session.createQuery("from Lawyer", Lawyer.class);
		return allLawyer.getResultList();
	}
	
	@Override
	public User getUserByNumber(String mobileNumber) {
		// Finds user by mobile number
		Session session = factory.getCurrentSession();
		Query<User> query = session.createQuery("from User where mobile_number = :number", User.class);
		query.setParameter("number", mobileNumber);
		return query.uniqueResult();
	}
	
	@Override
	public List<CaseRecord> getUserCase(User user){
		// return list of cases registered by given user
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where user = :user", CaseRecord.class);
		query.setParameter("user", user);
		return query.getResultList();
	}
	
	@Override
	public boolean existingBooking(Lawyer lawyer, Date bookingDate) {
		// returns boolean value to check if any appointment is reserved
		// for given lawyer for given date and time.
		Session session = factory.getCurrentSession();
		String q = "from Booking where lawyer = :lawyer and date = :bookingDate";
		Query<Booking> query = session.createQuery(q, Booking.class);
		query.setParameter("lawyer", lawyer);
		query.setParameter("bookingDate", bookingDate);
		return query.uniqueResult()!=null;
	}
	
	@Override
	public void bookAppointment(Booking booking) {
		// books an appointment
		factory.getCurrentSession().save(booking);
	}

	@Override
	public List<Booking> getAllBooking(User user) {
		// returns List of Bookings by given user
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where client = :user", Booking.class);
		query.setParameter("user", user);
		return query.getResultList();
	}
	
	@Override
	public Booking getBooking(int id) {
		// returns booking object provided id
		return factory.getCurrentSession().get(Booking.class, id);
	}

	@Override
	public void removeBooking(Booking booking) {
		// removes/cancels a booking made by user
		if(booking==null) return;
		factory.getCurrentSession().remove(booking);
	}

	@Override
	public List<String> getAllExpertise() {
		// returns all unique expertise list from lawyers table
		Session session = factory.getCurrentSession();
		Query<String> query = session.createQuery("select distinct(expertise) from Lawyer",String.class);
		return query.getResultList();
	}

	@Override
	public List<Lawyer> getLawyerByExpertise(List<String> matchingExpertise) {
		// returns all lawyers whose expertise is in list of strings
		Session session = factory.getCurrentSession();
		Query<Lawyer> query = session.createQuery("from Lawyer where expertise in :allExpertise", Lawyer.class);
		query.setParameter("allExpertise", matchingExpertise);
		return query.getResultList();
	}

	@Override
	public List<Booking> getAllBookingEarlier(User user, int bookingYear) {
		// returns list of bookings by user before provided year
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where client = :user and EXTRACT(YEAR FROM date_time) < :bookingYear", Booking.class);
		query.setParameter("user", user);
		query.setParameter("bookingYear", bookingYear);
		return query.getResultList();
	}
	
	@Override
	public List<Booking> getAllBookingByYear(User user, int bookingYear) {
		// returns list of bookings by user in provided year
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where client = :user and EXTRACT(YEAR FROM date_time) = :bookingYear", Booking.class);
		query.setParameter("user", user);
		query.setParameter("bookingYear", bookingYear);
		return query.getResultList();
	}

	@Override
	public List<CaseRecord> getAllCaseRecordEarlier(User user, int caseRecordYear) {
		// returns list of CaseRecords by user before provided year
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where user = :user and EXTRACT(YEAR FROM date_time) < :caseRecordYear", CaseRecord.class);
		query.setParameter("user", user);
		query.setParameter("caseRecordYear", caseRecordYear);
		return query.getResultList();
	}

	@Override
	public List<CaseRecord> getAllCaseRecordByYear(User user, int caseRecordYear) {
		// returns list of CaseRecords by user in provided year
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where user = :user and EXTRACT(YEAR FROM date_time) = :caseRecordYear", CaseRecord.class);
		query.setParameter("user", user);
		query.setParameter("caseRecordYear", caseRecordYear);
		return query.getResultList();
	}

	@Override
	public List<String> getAllLawyerName() {
		// returns all unique name list from lawyers table
		Session session = factory.getCurrentSession();
		Query<String> query = session.createQuery("select distinct(username) from Lawyer",String.class);
		return query.getResultList();
	}

	@Override
	public List<Lawyer> getLawyerByName(List<String> matchingName) {
		// returns all lawyers whose expertise is in list of strings
		Session session = factory.getCurrentSession();
		Query<Lawyer> query = session.createQuery("from Lawyer where username in :allLawyerName", Lawyer.class);
		query.setParameter("allLawyerName", matchingName);
		return query.getResultList();
	}

	@Override
	public List<Booking> getBookingByLawyerName(User user, List<String> matchingName) {
		// returns list of booking for given user and list of lawyer names
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where client = :user and lawyer in (from Lawyer where username in :allLawyername)",Booking.class);
		query.setParameter("user", user);
		query.setParameter("allLawyername",matchingName);
		return query.getResultList();
	}

	@Override
	public List<CaseRecord> getCaseRecordByLawyerName(User user, List<String> matchingName) {
		// returns list of case record for given user and list of lawyer names
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where user = :user and issuedBy in (from Lawyer where username in :allLawyername)",CaseRecord.class);
		query.setParameter("user", user);
		query.setParameter("allLawyername",matchingName);
		return query.getResultList();
	}
}

