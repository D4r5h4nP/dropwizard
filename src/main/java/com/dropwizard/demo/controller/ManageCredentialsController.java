package com.dropwizard.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;

import com.dropwizard.demo.authentication.User;
import com.dropwizard.demo.core.Employee;
import com.dropwizard.demo.dao.EmployeeDAO;
import com.dropwizard.demo.send.SendMail;

import io.dropwizard.auth.Auth;

@Path("/manageaccount")
public class ManageCredentialsController {
	
	private EmployeeDAO employeeDAO;
	
	public ManageCredentialsController(Validator validator, EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	
	@GET
	//@RolesAllowed({"user"})
	@Path("/forgotpassword/sendmail/{username}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response forgotPassword(@PathParam("username") String username, @Context HttpServletRequest request, @Auth User user) {
		
		HttpSession httpSession = request.getSession(true);
		Employee employee = employeeDAO.findEmployeeByUsername(username);
		if(employee != null) {
			String email = employee.getEmail();
			SendMail.sendForgotPasswordMail(email, httpSession, "forgotpassword");
			return Response.status(Status.OK).entity("{\"status\": \"true\","+
					"\"message\": \"OTP to reset password is sent on " + email + "\"}").build();
		}else {
			return Response.status(Status.OK).entity("{\"status\": \"false\","+
					"\"message\": \"User not Found...\"}").build();
		}
	}
	
	
	@GET
	//@RolesAllowed({"user"})
	@Path("/verifyotp/updatepassword/{otp}/{username}/{password}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response userOtpVerify(@PathParam("otp") int otp, @PathParam("username") String username,
			@PathParam("password") String password, @Context HttpServletRequest request, @Auth User user) {
		
		HttpSession httpSession = request.getSession(true);
		int otpToVerify = (int) httpSession.getAttribute("otpToVerifyToForgotPassword");
		long OtpSentAtMilliSeconds = (long) httpSession.getAttribute("otpSentAtMilliSecondsToForgotPassword");
		long currentMilliSeconds = System.currentTimeMillis();
		
		Employee employee = employeeDAO.findEmployeeByUsername(username);
		if(otp == otpToVerify) {
			if(currentMilliSeconds < OtpSentAtMilliSeconds && employee != null) {
					employee.setPassword(password);
					employeeDAO.saveOrUpdate(employee, (int) employee.getId());
					return Response.status(Status.OK).entity("{\"status\": \"true\","+
							"\"message\": \"OTP Verified...\"}").build();
			}else {
				return Response.status(Status.OK).entity("{\"status\": \"false\","+
						"\"message\": \"OTP Expired...\"}").build();
			}
		}else {
			return Response.status(Status.OK).entity("{\"status\": \"false\","+
					"\"message\": \"Invalid OTP...\"}").build();
		}
	}
	
	
	@GET
	//@RolesAllowed({"user"})
	@Path("/activateaccount/sendmail/{username}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response activeAccount(@PathParam("username") String username, @Context HttpServletRequest request, @Auth User user) {
		
		HttpSession httpSession = request.getSession(true);
		Employee employee = employeeDAO.findEmployeeByUsername(username);
		if(employee != null && !employee.isActive()) {
			String email = employee.getEmail();
			SendMail.sendForgotPasswordMail(email, httpSession, "verifyaccount");
			return Response.status(Status.OK).entity("{\"status\": \"true\","+
					"\"message\": \"Verification mail is sent to " + email + "...\"}").build();
		}else {
			return Response.status(Status.OK).entity("{\"status\": \"false\","+
					"\"message\": \"Account is already verified...\"}").build();
		}
	}
	

	@GET
	//@RolesAllowed({"user"})
	@Path("/verifyotp/activateaccount/{otp}/{username}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response makeUserAccountActivate(@PathParam("otp") int otp, @PathParam("username") String username ,
			@Context HttpServletRequest request, @Auth User user) {
		
		HttpSession httpSession = request.getSession(true);
		int otpToVerify = (int) httpSession.getAttribute("otpToVerifyForAccountActivation");
		long OtpSentAtMilliSeconds = (long) httpSession.getAttribute("otpSentAtMilliSecondsForAccountActivation");
		long currentMilliSeconds = System.currentTimeMillis();
		
		Employee employee = employeeDAO.findEmployeeByUsername(username);
		if(!employee.isActive()) {
			if(otp == otpToVerify) {
				if(currentMilliSeconds < OtpSentAtMilliSeconds) {
						employee.setActive(true);
						employeeDAO.saveOrUpdate(employee, (int) employee.getId());
						return Response.status(Status.OK).entity("{\"status\": \"true\","+
								"\"message\": \"OTP Verified...\"}").build();
				}else {
					return Response.status(Status.OK).entity("{\"status\": \"false\","+
							"\"message\": \"OTP Expired...\"}").build();
				}
			}else {
				return Response.status(Status.OK).entity("{\"status\": \"false\","+
						"\"message\": \"Invalid OTP...\"}").build();
			}
		}else {
			return Response.status(Status.OK).entity("{\"status\": \"true\","+
					"\"message\": \"Account is already verified and activated...\"}").build();
		}
	}	
}
