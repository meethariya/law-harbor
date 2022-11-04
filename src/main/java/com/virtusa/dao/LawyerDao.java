package com.virtusa.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.virtusa.exception.ReportNotFoundException;
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

	public List<CaseRecord> getCaseOfUser(User user, Lawyer lawyer) {
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :issuedByLawyer and user = :user and report is null", CaseRecord.class);
		query.setParameter("issuedByLawyer", lawyer);
		query.setParameter("user", user);
		return query.getResultList();
	}

	public Report getReportByBooking(Booking booking) {
		Session session = factory.getCurrentSession();
		Query<Report> query = session.createQuery("From Report where appointment = :booking", Report.class);
		query.setParameter("booking", booking);
		return query.uniqueResult();
	}
	
	public int addReport(Report report) {
		return (int) factory.getCurrentSession().save(report);
	}

	public Report getReport(int id) {
		Report report = factory.getCurrentSession().get(Report.class, id);
		if(report==null) {
			throw new ReportNotFoundException("No report with ID "+id);
		}
		return report;
	}
	
	public void updateCaseRecordReport(CaseRecord caseRecord) {
		factory.getCurrentSession().update(caseRecord);
	}

	public void updateBookingReport(Booking appointment) {
		factory.getCurrentSession().update(appointment);
	}
}
