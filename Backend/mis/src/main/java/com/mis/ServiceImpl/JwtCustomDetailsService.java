package com.mis.ServiceImpl;

import com.mis.Repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtCustomDetailsService implements UserDetailsService {

private final OrganisationRepository organisationRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return organisationRepository.findByEmail(email);
    }
}
