package com.example.webshop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.webshop.dto.UserDTO;
import com.example.webshop.model.Authority;
import com.example.webshop.model.Korisnik;
import com.example.webshop.common.security.TokenUtils;
import com.example.webshop.common.security.auth.JwtAuthenticationRequest;
import com.example.webshop.userServices.LoginService;
import com.example.webshop.common.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	LoginService loginService;

	@Autowired
	TokenUtils tokenUtils;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
													   HttpServletResponse response) throws AuthenticationException, IOException {

		Korisnik user=loginService.checkCredentials(authenticationRequest);
		if(user!=null){
			String jwt = tokenUtils.generateToken(authenticationRequest.getUsername());
			UserDTO userDTO= ObjectMapperUtils.map(user,UserDTO.class);
			int expiresIn = tokenUtils.getExpiredIn();
			userDTO.setToken(jwt);
			userDTO.setExpiresIn(expiresIn);

			Authority authority=((List<Authority>)user.getAuthorities()).get(0);
			if(authority.getName().equals("ROLE_USER")) {
				userDTO.setRole("Registrovani korisnik");
			}else if(authority.getName().equals("ROLE_ADMIN")) {
				userDTO.setRole("Administrator");
			}else if(authority.getName().equals("ROLE_UREDNIK")) {
				userDTO.setRole("Urednik");
			}else if(authority.getName().equals("ROLE_RECENZENT")) {
				userDTO.setRole("Recenzent");
			}
			// Vrati user-a sa tokenom kao odgovor na uspesno autentifikaciju
			return ResponseEntity.ok(userDTO);
		}else{
			return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
		}

	}
}