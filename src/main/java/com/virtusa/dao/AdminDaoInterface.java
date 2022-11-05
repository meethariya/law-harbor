package com.virtusa.dao;

import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

public interface AdminDaoInterface {

	public void deleteLawyer(Lawyer lawyer);

	public User getUserByNumber(String number);

	public Lawyer getLawyer(int lawyerId);

}
