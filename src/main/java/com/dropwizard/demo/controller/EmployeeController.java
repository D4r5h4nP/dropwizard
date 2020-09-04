package com.dropwizard.demo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;

import com.dropwizard.demo.authentication.User;
import com.dropwizard.demo.core.Employee;
import com.dropwizard.demo.dao.EmployeeDAO;

import io.dropwizard.auth.Auth;

@Path("/employee")
public class EmployeeController {

	private EmployeeDAO employeeDAO;
	
	private Validator validator;
	
	public EmployeeController(Validator validator, EmployeeDAO employeeDAO) {
		this.validator = validator;
		this.employeeDAO = employeeDAO;
	}
	
	@GET
	@RolesAllowed({"ADMIN"})
	@Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})
	public List<Employee> getEmployeeList(@Auth User user){
		return employeeDAO.getAllEmployee();
	}
	
	@GET
	@RolesAllowed({"ADMIN"})
	@Path("/get/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findById(@PathParam("id") String id, @Auth User user) {
		Employee employee = employeeDAO.findById(id); 
		if(employee != null)
			return Response.ok(employee).build();
		else
			return Response.status(Status.NOT_FOUND).build(); 
	}
	
	@POST
	@RolesAllowed({"ADMIN, USER"})
	@Path("/insert")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response insert(Employee employee, @Auth User user) throws URISyntaxException {
		
		Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
		if (violations.size() > 0) {
			ArrayList<String> validationMessages = new ArrayList<String>();
	        for (ConstraintViolation<Employee> violation : violations) {
	        	validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
	        }
	        return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
		}else {
			employee.setActive(false);
			employeeDAO.saveOrUpdate(employee, 0);
			return Response.ok().build();
		}
	}
	
	@PUT
	@RolesAllowed({"ADMIN"})
	@Path("/update/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") String id, Employee employee) throws URISyntaxException {
		
		Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
		Employee employeeIsExist = employeeDAO.findById(id);
		if(violations.size() > 0) {
			ArrayList<String> validationMessages = new ArrayList<String>();
			for(ConstraintViolation<Employee> violation : violations) {
				validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
			}
			return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
		}
		if(employeeIsExist != null) {
			employeeDAO.saveOrUpdate(employee, Integer.parseInt(id));	
			return Response.created(new URI("/employee" + employee.getId()))
                    .build();
		}else {
			return Response.status(Status.NOT_FOUND).build();
		}	
	}
	
	@DELETE
	@RolesAllowed({"ADMIN"})
	@Path("/delete/{id}")
	public void delete(@PathParam("id") String id) {
		Employee employee = employeeDAO.findById(id);
		if(employee != null) 
			employeeDAO.delete(id, employee);
	}
}
