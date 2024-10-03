package core.application.config;

import core.application.Util.JwtUtil;
import core.application.filter.JWTFilter;
import core.application.filter.CustomLoginFilter;
import core.application.filter.CustomLogoutFilter;
import core.application.users.service.CustomUserDetailsService;
import core.application.users.service.RedisService;
import core.application.users.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomUserDetailsService userDetailsService; // Add this field
    private final RedisService redisService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, CustomUserDetailsService userDetailsService, TokenService tokenService, JwtUtil jwtUtil, RedisService redisService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userDetailsService = userDetailsService; // Initialize the field
        this.tokenService = tokenService;
        this.redisService = redisService;
        this.jwtUtil = jwtUtil;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // 비밀 번호 인코딩 시 사용
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenService tokenService) throws Exception {
        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeRequests((auth) -> auth
                        // 추후 허가 경로 수정 // role이 "ADMIN"인 관리자에 대한 추가 사항 추후 수정 가능성
                        .requestMatchers("/users/signup", "/users/signin", "users/reissue").permitAll() // 회원가입, 로그인, refresh token 기반으로 access token 재발급
                        .requestMatchers(HttpMethod.GET, "/movies/list").permitAll() // 영화 목록 조회 (메인 페이지)
                        .requestMatchers(HttpMethod.GET, "/movies/search").permitAll() // 영화 검색
                        .requestMatchers(HttpMethod.GET, "/movies/").permitAll() // 영화 내용 상세 조회
                        .requestMatchers(HttpMethod.GET, "/movies/*/reviews/list").permitAll() // 리뷰 목록 조회
                        .requestMatchers(HttpMethod.GET, "/movies/*/reviews/{reviewId}").permitAll() // 리뷰 상세 조회
                        .requestMatchers(HttpMethod.GET, "/movies/*/reviews/list").permitAll() // 리뷰 목록 조회
                        .requestMatchers(HttpMethod.GET, "/movies/*/reviews/{reviewId}/comments").permitAll() // 리뷰 댓글 목록 조회
                        .requestMatchers(HttpMethod.GET, "/movies/*/reviews/{reviewId}/comments/{groupId}").permitAll() // 리뷰 대댓글 목록 조회
                        .requestMatchers(HttpMethod.GET, "/movies/*/comments").permitAll() // 한줄평 목록 조회
                        .requestMatchers("/redis/set").permitAll() // 추후 삭제
                        .requestMatchers("/redis/delete").permitAll() // 추후 삭제
                        .requestMatchers("/redis/get").permitAll() // 추후 삭제
                        .anyRequest().authenticated());


        http
                .addFilterBefore(new JWTFilter(tokenService), CustomLoginFilter.class);

        http
                .addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(tokenService), LogoutFilter.class);

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Configure AuthenticationManager to use the UserDetailsService
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        return http.build();
    }
}




