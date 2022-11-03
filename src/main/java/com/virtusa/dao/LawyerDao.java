package com.virtusa.dao;

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
public class LawyerDao {
	private static final Logger log = LogManager.getLogger(LawyerDao.class);
		
	public LawyerDao() {
		log.warn("LawyerDao Constructor Called");
	}
	
	@Autowired
	SessionFactory factory;
	
	@Autowired
	UserDao userDao;
	
	public Lawyer getLawyer(String email) {
		return userDao.getLawyer(email);
	}

	public User getUser(String email) {
		return userDao.getUser(email);
	}
	
	public List<Booking> getAllAppointment(Lawyer lawyer) {
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where lawyer = :lawyer", Booking.class);
		query.setParameter("lawyer", lawyer);
		return query.getResultList();
	}
	
	public Booking getBooking(int bookingId) {
		return userDao.getBooking(bookingId);
	}
	
	public void approveBooking(int bookingId) {
		Booking appointment = getBooking(bookingId);
		appointment.setBookingStatus(true);
		factory.getCurrentSession().update(appointment);
	}

	public void cancelBooking(Booking booking) {
		userDao.removeBooking(booking);
	}

	public List<CaseRecord> getAllCase(Lawyer lawyer) {
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :lawyer", CaseRecord.class);
		query.setParameter("lawyer", lawyer);
		return query.getResultList();
	}
	public void addCaseRecord(CaseRecord caseRecord) {
		factory.getCurrentSession().save(caseRecord);
	}
	public void deleteCaseRecord(CaseRecord caseRecord) {
		factory.getCurrentSession().delete(caseRecord);
	}
	public CaseRecord getCaseRecord(int caseRecordId) {
		return factory.getCurrentSession().get(CaseRecord.class, caseRecordId);
	}
}
