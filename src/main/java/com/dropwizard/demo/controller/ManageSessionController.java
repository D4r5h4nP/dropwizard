package com.dropwizard.demo.controller;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dropwizard.demo.authentication.User;
import com.dropwizard.demo.core.Employee;
import com.dropwizard.demo.dao.EmployeeDAO;

import io.dropwizard.auth.Auth;

@Path("/account")
public class ManageSessionController {

	private EmployeeDAO employeeDAO;
	public ManageSessionController(Validator validator, EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	
	@GET
	@RolesAllowed({"USER"})
	@Path("/login/user/{username}/{password}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findEmployeeByCredentials(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) {
		
		HttpSession httpSession = request.getSession(true);
		Employee employee = employeeDAO.findEmployeeByCredentials(username, password);
		if(employee == null) {
			httpSession.invalidate();
			return Response.status(Status.OK).entity("{\"status\": \"false\","+
					"\"message\": \"Invalid Credentials...\"},").build();
		}else if(!employee.isActive()) {
			httpSession.invalidate();
			return Response.status(Status.OK).entity("{\"status\": \"false\","+
					"\"message\": \"Account is not activated...\"}").build();
		} else { 
			httpSession.setAttribute("employee", employee);
			return Response.status(Status.OK).entity("{\"status\": \"true\","+
					"\"message\": \"Logged in...\"}").build();
		}
	}
	
	@GET
//	@RolesAllowed({"USER"})
	@Path("/logout/user")
	@Produces({MediaType.APPLICATION_JSON})
	public Response removeUserFromSession(@Context HttpServletRequest request, @Auth User user) {
		request.getSession(true).invalidate();
		return Response.status(Status.OK).entity("{\"status\": \"true\"}").build();
	}
	
}
