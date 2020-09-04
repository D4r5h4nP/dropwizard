package com.dropwizard.demo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.dropwizard.demo.core.Employee;

public class EmployeeDAO{

	private SessionFactory sessionFactory;
	
	public EmployeeDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getAllEmployee() {
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		List<Employee> listOfEmployee = session.getNamedQuery("getAllEmployee").list();
		transaction.commit();
		session.close();
		return listOfEmployee;
	}

	public Employee findById(String id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Employee employee = session.get(Employee.class, Integer.parseInt(id));
		transaction.commit();
		session.close();
		if(employee != null)
			return employee;
		else
			return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public Employee findEmployeeByUsername(String username) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		List<Employee> listOfEmployee = (List<Employee>) session.getNamedQuery("findEmployeeByUsername").
				setString("username", username).list();
		transaction.commit();
		session.close();
		if(listOfEmployee != null && listOfEmployee.size()==1) 
			return listOfEmployee.get(0);
		else
			return null;
	}
	
	public void saveOrUpdate(Employee employee, int id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		employee.setId(id);
		session.saveOrUpdate(employee);
		transaction.commit();
		session.close();
	}

	public void delete(String id, Employee employee) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.delete(employee);
		transaction.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public Employee findEmployeeByCredentials(String username, String password) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		List<Employee> listOfEmployee = (List<Employee>) session.getNamedQuery("findEmployeeByCredentials").
				setString("username", username).setString("password", password).list();
		transaction.commit();
		session.close();
		if(listOfEmployee != null && listOfEmployee.size()==1) 
			return listOfEmployee.get(0);
		else
			return null;
	}

	public void makeItActive(Employee employee) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		employee.setActive(true);
		session.saveOrUpdate(employee);
		transaction.commit();
		session.close();
	}
	
	
}