package xyz.hardliner.align;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String READ = "READ";
	private static final String CRUD = "CRUD";

	private AuthenticationManagerBuilder auth;

	@Value("${user.login}")
	private String userLogin;
	@Value("${user.password}")
	private String userPassword;
	@Value("${admin.login}")
	private String adminLogin;
	@Value("${admin.password}")
	private String adminPassword;

	@Autowired
	public void inject(AuthenticationManagerBuilder auth) {
		this.auth = auth;
	}

	@PostConstruct
	public void configureGlobal() throws Exception {
		auth.inMemoryAuthentication()
				.withUser(userLogin).password(passwordEncoder().encode(userPassword))
				.roles(READ).and()
				.withUser(adminLogin).password(passwordEncoder().encode(adminPassword))
				.roles(READ, CRUD);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/products/**").hasAnyRole(READ)
				.antMatchers("/product").hasAnyRole(CRUD)
				.antMatchers("/leftovers").hasAnyRole(READ)
				.and().httpBasic();
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
