package de.bsi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*@EnableWebSecurity
@Configuration*/
public class SecurityConfiguration {
	
	/*@Value("${trsso.logout_url}")
	private String logoutUrl;*/
	@Value("${trsso.login_process_path}")
	private String loginProcessPath;
	
	/*@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println("=====================Inside=============");
		http.authorizeHttpRequests()
				.antMatchers("/secured").authenticated()
				.anyRequest().permitAll()
			.and().logout()
				// In this demo HTTP GET instead of POST is used for logout, 
				// so the logoutRequestMatcher is required to detect logout requests.
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessHandler((req, resp, auth) -> new DefaultRedirectStrategy().sendRedirect(req, resp, logoutUrl))
			.and().oauth2Login(c -> {
				c.loginProcessingUrl(loginProcessPath);
				c.loginPage("/oauth2/authorization/sam");
			});
		return http.build();
	}*/

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.out.println("=====================Inside=============");
		http
				.authorizeHttpRequests(authorize -> authorize
						.anyRequest().authenticated()
				)
				.oauth2Login(c -> {
					c.loginProcessingUrl(loginProcessPath);
					c.loginPage("/oauth2/authorization/trsso");
				});

		return http.build();
	}

	/*@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf()
				.disable()
				.authorizeHttpRequests()
				.anyRequest()
				.authenticated()
				.and()
				.oauth2Login()
		;
		return http.build();
	}*/
	
}