package noodlezip.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.AntPathMatcher;
import java.util.Arrays;
import java.util.List;


@EnableWebSecurity
@Configuration
@Slf4j
@EnableMethodSecurity
public class SecurityConfig {

    // permitAll 패턴을 상수로 분리
    private static final List<String> PERMIT_ALL_PATTERNS = Arrays.asList(
        "/", "/check-login-id", "/check-email", "/verify-email", "/signup", "/login",
        "/images/**", "/css/**", "/img/**", "/js/**", "/assets/**", "/v3/api-docs/**",
        "/swagger-ui/**", "/send-verification-code", "/fragments/**", "/search/**",
        "/store/**", "/admin_section/**", "/admin/**", "/receipt/**", "/location/**", "/ramen/**",
        "/board/**", "/comments/**", "/users/**", "/mypage/**", "/favicon.ico", "/font/**", "error/**",
            "/bs-icon-font/**", "/icon_fonts/**"
    );

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // URL 접근 제어( 인가 설정 )
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(PERMIT_ALL_PATTERNS.toArray(new String[0])).permitAll()
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .requestMatchers("/user/**").hasAnyAuthority("NORMAL")
                    .anyRequest().authenticated();
        });

        // 로그인 처리 설정( 인증 설정 ) => custom
        http.formLogin(form -> {
            form.loginPage("/") // custom loginPage URL
                    .loginProcessingUrl("/login") // login Process URL(POST)
                    .usernameParameter("userId") // 파라미터중 인증처리 사용 아이디
                    .passwordParameter("userPwd") // 파라미터중 인증처리 사용 비밀번호
                    .successHandler((req, resp, auth) -> {
                        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                            resp.sendRedirect("/admin/main"); // 응답 -> /admin/main
                        } else {
                            // 1. 파라미터로 redirectUrl이 있으면 우선 적용 (외부 URL 차단)
                            String redirectUrl = req.getParameter("redirectUrl");
                            if (redirectUrl != null && redirectUrl.startsWith("/")) {
                                resp.sendRedirect(redirectUrl);
                                return;
                            }
                            // 2. Spring Security가 저장한 원래 요청이 있는지 확인
                            SavedRequest savedRequest = (org.springframework.security.web.savedrequest.SavedRequest)
                                    req.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                            if (savedRequest != null) {
                                resp.sendRedirect(savedRequest.getRedirectUrl()); // 원래 요청으로 이동
                            } else {
                                resp.sendRedirect("/"); // 없을 시 메인
                            }
                        }
                    })
                    .failureHandler((req, resp, exce) -> {
                        log.info("exception : {}", exce.getMessage());
                        resp.sendRedirect("/?error=login");
                    });
        });

        AntPathMatcher matcher = new AntPathMatcher();

        // 로그아웃 처리 설정( 인증 설정 ) 로그아웃 시 permitAll 페이지면 해당 페이지로 이동 or "/"
        http.logout(logout -> {
            logout.logoutUrl("/logout")
                    .logoutSuccessHandler((req, resp, auth) -> {
                        String redirectUrl = req.getParameter("redirectUrl");
                        boolean isPermit = false;
                        if (redirectUrl != null && redirectUrl.startsWith("/")) {
                            for (String pattern : PERMIT_ALL_PATTERNS) {
                                if (matcher.match(pattern, redirectUrl)) {
                                    isPermit = true;
                                    break;
                                }
                            }
                        }
                        if (isPermit) {
                            resp.sendRedirect(redirectUrl);
                        } else {
                            resp.sendRedirect("/");
                        }
                    })
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID");
        });


        http.csrf(csrf -> {
            csrf.disable();
        });

        // 동일 도메인에서 iframe 접근 가능 설정
        http.headers().frameOptions().sameOrigin();

        return http.build();

    }
}