package com.mis.Configuration;

import com.mis.Entity.Organisation;
import com.mis.Repository.OrganisationRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter
{
    private final JwtUtil jwtUtil;
    private final OrganisationRepository organisationRepository;
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
try {

    final String requestTokenHeader = request.getHeader("Authorization");
    if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
        filterChain.doFilter(request, response);
        return;
    }
    String token = requestTokenHeader.split("Bearer ")[1];
    String username = jwtUtil.getOrganisationFromToken(token);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        Organisation organisation = organisationRepository.findByEmail(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(organisation, null, organisation.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    filterChain.doFilter(request, response);
}catch (Exception e){
//    handlerExceptionResolver.resolveException(request,response,null,e);
    throw new RuntimeException(e.getMessage());
}
    }
}
