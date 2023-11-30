package com.apiserver.greengift._core.security;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userJPARepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(BaseException.USER_EMAIL_NOT_FOUND.getMessage())
        );

        return new CustomUserDetails(user);
    }
}
