package com.possible.loanbanking.config;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final  JwtAuthenticationHelper jwtHelper;

	private final UserRepository userRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
			
		String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		String username;
		String token;
		if(requestHeader!=null && requestHeader.startsWith("Bearer"))
		{
			token = requestHeader.substring(7);
			
			username= jwtHelper.getUsernameFromToken(token);
			
			if(username!= null && SecurityContextHolder.getContext().getAuthentication() == null)
			{
				AppUser userDetails = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("User not found"));
				if(Boolean.FALSE.equals(jwtHelper.isTokenExpired(token)))
				{
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(token, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
				
			}
		}
		filterChain.doFilter(request, response);
	}

}
