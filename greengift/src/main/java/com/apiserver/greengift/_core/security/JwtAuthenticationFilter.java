package com.apiserver.greengift._core.security;

import com.apiserver.greengift.user.constant.Role;
import com.apiserver.greengift.user.User;
import com.apiserver.greengift.user.festival_manager.FestivalManager;
import com.apiserver.greengift.user.participant.Participant;
import com.apiserver.greengift.user.trash_manager.TrashManager;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(JWTProvider.HEADER);

        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT decodedJWT = JWTProvider.verify(jwt);
            Long userId = decodedJWT.getClaim("id").asLong();
            String roleName = decodedJWT.getClaim("role").asString();

            Role role = Role.valueOfRole(roleName);
            User user = getUser(userId, role);

            CustomUserDetails myUserDetails = new CustomUserDetails(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            myUserDetails,
                            myUserDetails.getPassword(),
                            myUserDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 인증 객체 만들어짐");
        } catch (SignatureVerificationException sve) {
            log.error("토큰 검증 실패");
        } catch (TokenExpiredException tee) {
            log.error("토큰 만료됨");
        } catch (Exception e ) {
            log.error("토큰 검증 중 예상하지 못한 에러 발생");
        } finally {
            chain.doFilter(request, response);
        }
    }

    private static User getUser(Long userId, Role role) {
        if (role == Role.PARTICIPANT){
            return Participant.builder().id(userId).build();
        }
        else if (role == Role.TRASH_MANAGER){
            return TrashManager.builder().id(userId).build();
        }
        else {
            return FestivalManager.builder().id(userId).build();
        }
    }
}
