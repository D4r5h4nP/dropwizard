package com.dropwizard.demo.configure;

import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import com.dropwizard.demo.authentication.AppAuthenticator;
import com.dropwizard.demo.authentication.AppAuthorizer;
import com.dropwizard.demo.authentication.User;
import com.dropwizard.demo.controller.EmployeeController;
import com.dropwizard.demo.controller.HtmlDashboard;
import com.dropwizard.demo.controller.ManageCredentialsController;
import com.dropwizard.demo.controller.ManageSessionController;
import com.dropwizard.demo.core.Employee;
import com.dropwizard.demo.dao.EmployeeDAO;
import com.dropwizard.demo.health.DatabaseHealthCheck;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MainService extends Application<MainConfiguration> {

  public static void main(String[] args) throws Exception {
    new MainService().run(args);
  }

  private final HibernateBundle<MainConfiguration> hibernateBundle = new HibernateBundle<MainConfiguration>(Employee.class) {

	@Override
	public PooledDataSourceFactory getDataSourceFactory(MainConfiguration configuration) {
		// TODO Auto-generated method stub
		return configuration.getDataSourceFactory();
	}
};
  
  @Override
  public void initialize(Bootstrap<MainConfiguration> bootstrap) {
//	  super.initialize(bootstrap);
//	  bootstrap.addBundle(new AssetsBundle("/assets/"));
	  bootstrap.addBundle(hibernateBundle);
  }

  @Override
  public void run(MainConfiguration configuration, Environment environment) throws Exception {
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
//    final EmployeeDAO dao = jdbi.onDemand(EmployeeDAO.class);
    EmployeeDAO dao = new EmployeeDAO(hibernateBundle.getSessionFactory());

    environment.jersey().setUrlPattern("/dropwizard/");
    
    environment.jersey().register(new HtmlDashboard());
    environment.jersey().register(new EmployeeController(environment.getValidator(), dao));
    environment.jersey().register(new ManageSessionController(environment.getValidator(), dao));
    environment.jersey().register(new ManageCredentialsController(environment.getValidator(), dao));
//    environment.jersey().register(HttpSessionProvider.class);
    environment.servlets().setSessionHandler(new SessionHandler());
    
    environment.jersey().register((new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new AppAuthenticator())
            .setAuthorizer(new AppAuthorizer())
//            .setRealm("SUPER SECRET STUFF")
            .setRealm("BASIC-AUTH-REALM")
//            .setPrefix("Basic")
            .buildAuthFilter())));
    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    environment.jersey().register(RolesAllowedDynamicFeature.class);
    
    environment.healthChecks().register("health",
        new DatabaseHealthCheck(jdbi, configuration.getDataSourceFactory().getValidationQuery()));
  }

}
