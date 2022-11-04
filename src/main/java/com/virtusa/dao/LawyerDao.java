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
import com.virtusa.model.Report;
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
		// finds lawyer for given email. Logic in UserDao
		return userDao.getLawyer(email);
	}

	public User getUser(String email) {
		// finds User for given email. Logic in UserDao
		return userDao.getUser(email);
	}
	
	public List<Booking> getAllAppointment(Lawyer lawyer) {
		// Gets all appointments of a Lawyer
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where lawyer = :lawyer", Booking.class);
		query.setParameter("lawyer", lawyer);
		return query.getResultList();
	}
	
	public Booking getBooking(int bookingId) {
		// returns appointment for given id. Logic in UserDao
		return userDao.getBooking(bookingId);
	}
	
	public void approveBooking(Booking appointment) {
		// Approve an appointment
		appointment.setBookingStatus(true);
		factory.getCurrentSession().update(appointment);
	}

	public void cancelBooking(Booking booking) {
		// cancels an appointment
		userDao.removeBooking(booking);
	}

	public List<CaseRecord> getAllCase(Lawyer lawyer) {
		// returns List of case records made by lawyer
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :lawyer", CaseRecord.class);
		query.setParameter("lawyer", lawyer);
		return query.getResultList();
	}
	public void addCaseRecord(CaseRecord caseRecord) {
		// adds case record
		factory.getCurrentSession().save(caseRecord);
	}
	public void deleteCaseRecord(CaseRecord caseRecord) {
		// deletes case record
		factory.getCurrentSession().delete(caseRecord);
	}
	public CaseRecord getCaseRecord(int caseRecordId) {
		// returns case record given its id
		return factory.getCurrentSession().get(CaseRecord.class, caseRecordId);
	}
	public void updateCaseRecordReport(CaseRecord caseRecord) {
		// updates case record
		factory.getCurrentSession().update(caseRecord);
	}
	public List<CaseRecord> getCaseOfUser(User user, Lawyer lawyer) {
		// returns list of case record made by given lawyer for a given user
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :issuedByLawyer and user = :user and report is null", CaseRecord.class);
		query.setParameter("issuedByLawyer", lawyer);
		query.setParameter("user", user);
		return query.getResultList();
	}

	public Report getReportByBooking(Booking booking) {
		// returns report for a given booking
		Session session = factory.getCurrentSession();
		Query<Report> query = session.createQuery("From Report where appointment = :booking", Report.class);
		query.setParameter("booking", booking);
		return query.uniqueResult();
	}
	
	public int addReport(Report report) {
		// adds report and returns its id
		return (int) factory.getCurrentSession().save(report);
	}

	public Report getReport(int id) {
		// returns report given its id
		return factory.getCurrentSession().get(Report.class, id);
	}
	
	public void updateBookingReport(Booking appointment) {
		// updates booking
		factory.getCurrentSession().update(appointment);
	}
}
