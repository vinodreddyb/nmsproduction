package org.vinod;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		try {
			System.out.println("Data source------------------------>" + dataSource);
  auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery(
				"select username,password, enabled from tbl_users where username=?")
			.authoritiesByUsernameQuery(
				"select users.username, roles.role from tbl_user_roles as roles join tbl_users as users on users.userId = roles.userId where users.username= ?");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

	  http.authorizeRequests()
	  
		.antMatchers("/javax.faces.resource/*").anonymous()
		.antMatchers("/login.jsf").anonymous()
		.antMatchers("/home.jsf**").authenticated()
		.and()
		  .formLogin().loginPage("/login.jsf").failureUrl("/login.jsf?error")
		  .usernameParameter("username").passwordParameter("password")
		.and()
		  .logout().logoutSuccessUrl("/login.jsf?logout")
		.and()
		  .exceptionHandling().accessDeniedPage("/default.jsf")
		.and()
		  .csrf();
	}
}
