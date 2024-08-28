package vn.dev.managementsystem.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import vn.dev.managementsystem.Entity.BaseResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private static final String Secret_key = "123";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
				FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {//Bearer
			try {
				String token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorthm = Algorithm.HMAC256(Secret_key.getBytes());
				JWTVerifier verifier = JWT.require(algorthm).build();
				DecodedJWT decodeJWT = verifier.verify(token);
				String username = decodeJWT.getSubject();
				String[] roles = decodeJWT.getClaim("roles").asArray(String.class);
				Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
				Arrays.stream(roles).forEach(r->{
					authorities.add(new SimpleGrantedAuthority(r));
				});
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(username, null,authorities);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
				filterChain.doFilter(request, response);
			}catch(Exception e) {
				BaseResponse<String> baseResponse = BaseResponse.<String>builder()
						.code(FORBIDDEN.value())
						.message(e.getMessage())
						.data(null)
						.build();
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), baseResponse);
			}
		}else {
			filterChain.doFilter(request, response);
		}
	}
}

