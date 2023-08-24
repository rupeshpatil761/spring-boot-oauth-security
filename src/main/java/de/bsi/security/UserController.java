package de.bsi.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserController {

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	
	@GetMapping("/")
	public ModelAndView defaultPage(@AuthenticationPrincipal OidcUser principal) {
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("user", principal);
		return mav;
	}

	@GetMapping("/test")
	public ModelAndView testUrl(@AuthenticationPrincipal OidcUser principal) {
		System.out.println("=========Inside testUrl method");
		System.out.println("=========Inside testUrl method: principal: "+principal.toString());
		ModelAndView mav = new ModelAndView("loginSuccess");
		mav.addObject("user", principal);
		mav.addObject("name", principal.getName());
		return mav;
	}

	@GetMapping("/test1")
	public ModelAndView testUrl2(@AuthenticationPrincipal OidcUser principal) {
		System.out.println("=========Inside testUrl2 method");
		System.out.println("=========Inside testUrl2 method: principal: "+principal.toString());
		ModelAndView mav = new ModelAndView("loginSuccess");
		mav.addObject("user", principal);
		mav.addObject("name", principal.getName());
		return mav;
	}

//	@GetMapping("/login/oauth2/code/trsso")
//	public ModelAndView redirect(@AuthenticationPrincipal OidcUser principal) {
//		System.out.println("=========Inside redirect method");
//		ModelAndView mav = new ModelAndView("loginSuccess");
//		mav.addObject("user", principal);
//		mav.addObject("name", principal.getName());
//		return mav;
//	}

//	@GetMapping("/loginSuccess")
//	public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
//
//		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
//
//		String userInfoEndpointUri = client.getClientRegistration()
//				.getProviderDetails()
//				.getUserInfoEndpoint()
//				.getUri();
//
//		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
//			RestTemplate restTemplate = new RestTemplate();
//			HttpHeaders headers = new HttpHeaders();
//			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
//					.getTokenValue());
//
//			HttpEntity<String> entity = new HttpEntity<String>("", headers);
//
//			ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
//			Map userAttributes = response.getBody();
//			model.addAttribute("name", userAttributes.get("name"));
//		}
//
//		return "loginSuccess";
//	}
	
}