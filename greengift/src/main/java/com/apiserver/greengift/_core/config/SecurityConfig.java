package com.apiserver.greengift._core.config;

import com.apiserver.greengift._core.errors.BaseException;
import com.apiserver.greengift._core.errors.exception.ForbiddenException;
import com.apiserver.greengift._core.errors.exception.UnauthorizedException;
import com.apiserver.greengift._core.security.JwtAuthenticationFilter;
import com.apiserver.greengift._core.utils.FilterResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            super.configure(builder);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf(CsrfConfigurer::disable);

        // 2. iframe 거부
        http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // 3. cors 재설정
        http.cors((cors) -> cors.configurationSource(configurationSource()));

        // 4. jSessionId 사용 거부 (5번을 설정하면 jsessionId가 거부되기 때문에 4번은 사실 필요 없다)
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 5. form 로긴 해제 (UsernamePasswordAuthenticationFilter 비활성화)
        http.formLogin(FormLoginConfigurer::disable);

        // 6. 로그인 인증창이 뜨지 않게 비활성화
        http.httpBasic(HttpBasicConfigurer::disable);

        // 7. 커스텀 필터 적용 (시큐리티 필터 교환)
        http.apply(new CustomSecurityFilterManager());

        // 8. 인증 실패 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.authenticationEntryPoint((request, response, authException)
                        -> FilterResponseUtils.unAuthorized(response, new UnauthorizedException(BaseException.USER_UNAUTHORIZED)))
        );

        // 9. 권한 실패 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedHandler((request, response, accessDeniedException)
                        -> FilterResponseUtils.forbidden(response, new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS)))
        );

        // 11. 인증, 권한 필터 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests

                        .requestMatchers(
                                new AntPathRequestMatcher("/user/grade"),
                                new AntPathRequestMatcher("/festival/join"),
                                new AntPathRequestMatcher("/festival/result/**"),
                                new AntPathRequestMatcher("/festival/all/**"),
                                new AntPathRequestMatcher("/gift/**", "POST")
                        ).hasAuthority("participant")

                        .requestMatchers(
                                new AntPathRequestMatcher("/trash", "POST")
                        ).hasAuthority("trash_manager")

                        .requestMatchers(
                                new AntPathRequestMatcher("/festival", "POST"),
                                new AntPathRequestMatcher("/festival/product/**"),
                                new AntPathRequestMatcher("/festival/self"),
                                new AntPathRequestMatcher("/festival/random/**"),
                                new AntPathRequestMatcher("/gift/**", "GET")
                        ).hasAuthority("festival_manager")

                        .requestMatchers(
                                new AntPathRequestMatcher("/user/grade"),
                                new AntPathRequestMatcher("/festival/**"),
                                new AntPathRequestMatcher("/gift/**"),
                                new AntPathRequestMatcher("/trash/**")
                        ).authenticated()

                        .anyRequest().permitAll()
        );

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
