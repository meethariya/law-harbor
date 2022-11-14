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
public class LawyerDao implements LawyerDaoInterface{
	// DAO class for lawyer transactions
	private static final Logger log = LogManager.getLogger(LawyerDao.class);
	
	private static final String LAWYER = "lawyer";
	
	public LawyerDao() {
		log.warn("LawyerDao Constructor Called");
	}
	
	@Autowired
	SessionFactory factory;
	
	@Autowired
	UserDao userDao;
	
	@Override
	public Lawyer getLawyer(String email) {
		// finds lawyer for given email. Logic in UserDao
		return userDao.getLawyer(email);
	}

	@Override
	public User getUser(String email) {
		// finds User for given email. Logic in UserDao
		return userDao.getUser(email);
	}
	
	@Override
	public List<Booking> getAllAppointment(Lawyer lawyer) {
		// Gets all appointments of a Lawyer
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where lawyer = :lawyer", Booking.class);
		query.setParameter(LAWYER, lawyer);
		return query.getResultList();
	}
	
	@Override
	public Booking getBooking(int bookingId) {
		// returns appointment for given id. Logic in UserDao
		return userDao.getBooking(bookingId);
	}
	
	@Override
	public void approveBooking(Booking appointment) {
		// Approve an appointment
		appointment.setBookingStatus(true);
		factory.getCurrentSession().update(appointment);
	}

	@Override
	public void cancelBooking(Booking booking) {
		// cancels an appointment
		userDao.removeBooking(booking);
	}

	@Override
	public List<CaseRecord> getAllCase(Lawyer lawyer) {
		// returns List of case records made by lawyer
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :lawyer", CaseRecord.class);
		query.setParameter(LAWYER, lawyer);
		return query.getResultList();
	}
	
	@Override
	public void addCaseRecord(CaseRecord caseRecord) {
		// adds case record
		factory.getCurrentSession().save(caseRecord);
	}
	
	@Override
	public void deleteCaseRecord(CaseRecord caseRecord) {
		// deletes case record
		factory.getCurrentSession().delete(caseRecord);
	}
	
	@Override
	public CaseRecord getCaseRecord(int caseRecordId) {
		// returns case record given its id
		return factory.getCurrentSession().get(CaseRecord.class, caseRecordId);
	}
	
	@Override
	public void updateCaseRecordReport(CaseRecord caseRecord) {
		// updates case record
		factory.getCurrentSession().update(caseRecord);
	}
	
	@Override
	public List<CaseRecord> getCaseOfUser(User user, Lawyer lawyer) {
		// returns list of case record made by given lawyer for a given user
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :lawyer and user = :user and report is null", CaseRecord.class);
		query.setParameter(LAWYER, lawyer);
		query.setParameter("user", user);
		return query.getResultList();
	}

	@Override
	public Report getReportByBooking(Booking booking) {
		// returns report for a given booking
		Session session = factory.getCurrentSession();
		Query<Report> query = session.createQuery("From Report where appointment = :booking", Report.class);
		query.setParameter("booking", booking);
		return query.uniqueResult();
	}
	
	@Override
	public int addReport(Report report) {
		// adds report and returns its id
		return (int) factory.getCurrentSession().save(report);
	}

	@Override
	public Report getReport(int id) {
		// returns report given its id
		return factory.getCurrentSession().get(Report.class, id);
	}
	
	@Override
	public void updateBookingReport(Booking appointment) {
		// updates booking
		factory.getCurrentSession().update(appointment);
	}

	@Override
	public List<Booking> getBookingEarlier(Lawyer lawyer, int bookingYear) {
		// returns list of bookings of lawyer before provided year
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where lawyer = :lawyer and EXTRACT(YEAR FROM date_time) < :bookingYear", Booking.class);
		query.setParameter(LAWYER, lawyer);
		query.setParameter("bookingYear", bookingYear);
		return query.getResultList();
	}
	
	@Override
	public List<Booking> getBookingByYear(Lawyer lawyer, int bookingYear) {
		// returns list of bookings of lawyer in provided year
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where lawyer = :lawyer and EXTRACT(YEAR FROM date_time) = :bookingYear", Booking.class);
		query.setParameter(LAWYER, lawyer);
		query.setParameter("bookingYear", bookingYear);
		return query.getResultList();
	}
	
	@Override
	public List<CaseRecord> getAllCaseRecordEarlier(Lawyer lawyer, int caseRecordYear) {
		// returns list of CaseRecords by user before provided year
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :lawyer and EXTRACT(YEAR FROM date_time) < :caseRecordYear", CaseRecord.class);
		query.setParameter(LAWYER, lawyer);
		query.setParameter("caseRecordYear", caseRecordYear);
		return query.getResultList();
	}

	@Override
	public List<CaseRecord> getAllCaseRecordByYear(Lawyer lawyer, int caseRecordYear) {
		// returns list of CaseRecords by user in provided year
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :lawyer and EXTRACT(YEAR FROM date_time) = :caseRecordYear", CaseRecord.class);
		query.setParameter(LAWYER, lawyer);
		query.setParameter("caseRecordYear", caseRecordYear);
		return query.getResultList();
	}

	@Override
	public List<String> getUniqueUsernameForBooking(Lawyer lawyer) {
		// returns list of names of users who booked appointment for given lawyer
		Session session = factory.getCurrentSession();
		Query<String> query = session.createQuery("select username from User where id in (select distinct(client) from Booking where lawyer = :lawyer)", String.class);
		query.setParameter(LAWYER, lawyer);
		return query.getResultList();
	}
	
	@Override
	public List<Booking> getBookingByListOfUsername(Lawyer lawyer, List<String> usernames){
		// returns list of booking for a lawyer and given list of user names
		Session session = factory.getCurrentSession();
		Query<Booking> query = session.createQuery("from Booking where lawyer = :lawyer and client in (from User where username in :usernames)", Booking.class);
		query.setParameter(LAWYER, lawyer);
		query.setParameter("usernames", usernames);
		return query.getResultList();
	}

	@Override
	public List<String> getUniqueUsernameForCaseRecord(Lawyer lawyer) {
		// returns list of user name who booked appointment for given lawyer
		Session session = factory.getCurrentSession();
		Query<String> query = session.createQuery("select username from User where id in (select distinct(user) from CaseRecord where issuedBy = :lawyer)", String.class);
		query.setParameter(LAWYER, lawyer);
		return query.getResultList();
	}
	
	@Override
	public List<CaseRecord> getCaseRecordByListOfUsername(Lawyer lawyer, List<String> usernames) {
		// returns list of case records for a lawyer and given list of user names
		Session session = factory.getCurrentSession();
		Query<CaseRecord> query = session.createQuery("from CaseRecord where issuedBy = :lawyer and user in (from User where username in :usernames)", CaseRecord.class);
		query.setParameter(LAWYER, lawyer);
		query.setParameter("usernames", usernames);
		return query.getResultList();
	}
}
