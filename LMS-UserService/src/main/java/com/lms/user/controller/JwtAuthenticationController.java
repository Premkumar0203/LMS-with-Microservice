package com.lms.user.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lms.user.constants.UserConstants;
import com.lms.user.model.Users;
import com.lms.user.respository.UserRespository;
import com.lms.user.security.JwtRequest;
import com.lms.user.security.JwtResponse;
import com.lms.user.security.JwtTokenUtil;
import com.lms.user.security.JwtUserDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(description = "Author can authenticate a credientials and logout", name = "Author Portal - Login and Logout")
@RestController
public class JwtAuthenticationController {

	@Autowired(required=true)
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	UserRespository UserResp;

//	@Operation(summary = "Logout Author", description = "Logout Author")
//	@GetMapping(UserConstants.LogoutAuthor)
//	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
//	        String authToken = request.getHeader("Authorization");
//	        final String token = authToken.substring(7);
//	        String username = jwtTokenUtil.getUsernameFromToken(token);
//	        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
//	            String refreshedToken = jwtTokenUtil.refreshToken(token);
//	            return ResponseEntity.ok(new JwtResponse(refreshedToken));
//	        } else {
//	            return ResponseEntity.badRequest().body(null);
//	        }
//	    }


	@Operation(summary = "Login User", description = "Login User")
	@PostMapping(UserConstants.LoginUser)
	public ResponseEntity<?> authenticate(@RequestBody JwtRequest req) throws Exception {
		authenticate(req.getUsername(), req.getPassword());
		final UserDetails userDetails = loadUserByUsernameAndPassword(req.getUsername(), req.getPassword());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	public UserDetails loadUserByUsernameAndPassword(String username, String password)
			throws UsernameNotFoundException {

		Optional<Users> user = UserResp.getUserDetails(username, password);
		if (user.isPresent()) {
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + "USER"));
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			return new User(username, encoder.encode(user.get().getPassword()), authorities);
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}