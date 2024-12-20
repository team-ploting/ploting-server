package ploting_server.ploting.core.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ploting_server.ploting.core.security.filter.JwtFilter;
import ploting_server.ploting.core.security.handler.CustomAccessDeniedHandler;
import ploting_server.ploting.core.security.service.jwt.JwtService;
import ploting_server.ploting.core.security.service.oauth.CustomOAuth2FailureHandler;
import ploting_server.ploting.core.security.service.oauth.CustomOAuth2SuccessHandler;
import ploting_server.ploting.core.security.service.oauth.CustomOAuth2UserService;
import ploting_server.ploting.member.entity.RoleType;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * JWT 및 OAuth 관련 설정을 포함하고 있는 Spring Security 설정 클래스입니다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final CorsConfig corsConfig;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    /**
     * 로그인 인증 작업 처리
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 공개 접근 필터 체인
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        defaultSecuritySetting(http);
        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(publicRequestMatchers()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(publicRequestMatchers()).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * 인증 및 권한이 필요한 필터 체인
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticatedFilterChain(HttpSecurity http) throws Exception {
        defaultSecuritySetting(http);
        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(authenticatedRequestMatchers()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(authenticatedRequestMatchers())
                        .hasAnyAuthority(RoleType.ROLE_USER.name(), RoleType.ROLE_ADMIN.name())
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 어드민 권한이 필요한 필터 체인
     */
    @Bean
    @Order(3)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        defaultSecuritySetting(http);
        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(adminRequestMatchers()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(adminRequestMatchers())
                        .hasAuthority(RoleType.ROLE_ADMIN.name())
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 공개 접근 endpoint
     */
    private RequestMatcher[] publicRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                // Swagger
                antMatcher(GET, "/swagger-ui/**"), // Swagger UI 웹 인터페이스를 제공하는 경로
                antMatcher(GET, "/v3/api-docs/**"), // Swagger의 API 문서 데이터를 JSON 형식으로 제공하는 경로

                // OAuth
                antMatcher(GET, "/oauth2/**"), // OAuth 인증 요청 경로
                antMatcher(GET, "/login/oauth2/**"), // OAuth 리다이렉트 경로

                // 유틸리티
                antMatcher(GET, "/utils/health"),

                // 필터
                antMatcher(GET, "/filters/main"),

                // 회원
                antMatcher(GET, "/members/{memberId:\\d+}"),
                antMatcher(GET, "/members/check-nickname"),

                // 단체
                antMatcher(GET, "/organizations"),
                antMatcher(GET, "/organizations/{organizationId}/members"),

                // 모임
                antMatcher(GET, "/meetings/{meetings}/members"),
                antMatcher(GET, "/meetings/organization/{organizationId}"),

                // 미션
                antMatcher(GET, "/missions"),
                antMatcher(GET, "/missions/{missionId}"),

                // 포인트
                antMatcher(GET, "/points/{memberId}"),
                antMatcher(GET, "/points/{memberId}/grass")
        );

        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * 인증 및 권한이 필요한 endpoint
     */
    private RequestMatcher[] authenticatedRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                // 필터
                antMatcher(GET, "/filters/like"),

                // 회원
                antMatcher(DELETE, "/members"),
                antMatcher(GET, "/members/self"),
                antMatcher(PATCH, "/members/registration"),

                // 단체장
                antMatcher(DELETE, "/organizations/{organizationId}"),
                antMatcher(PATCH, "/organizations/{organizationId}"),
                antMatcher(POST, "/organizations"),

                // 단체
                antMatcher(DELETE, "/organizations/{organizationId}/departure"),
                antMatcher(GET, "/organizations/{organizationId}"),
                antMatcher(GET, "/organizations/self"),
                antMatcher(POST, "/organizations/{organizationId}"),

                // 단체 좋아요
                antMatcher(POST, "/organizations/{organizationId}/like"),
                antMatcher(DELETE, "/organizations/{organizationId}/like"),

                // 모임장
                antMatcher(DELETE, "/meetings/{meetingId}"),
                antMatcher(POST, "/meetings"),

                // 모임
                antMatcher(GET, "/meetings/self"),
                antMatcher(DELETE, "/meetings/{meetingId}/departure"),
                antMatcher(GET, "/meetings/{meetingId}"),
                antMatcher(POST, "/meetings/{meetingId}"),

                // 모임 좋아요
                antMatcher(POST, "/meetings/{meetingId}/like"),
                antMatcher(DELETE, "/meetings/{meetingId}/like"),

                // 게시글
                antMatcher(DELETE, "/posts/{postId}"),
                antMatcher(GET, "/posts/{postId}"),
                antMatcher(PATCH, "/posts/{postId}"),
                antMatcher(POST, "/posts"),
                antMatcher(GET, "/posts/self"),
                antMatcher(GET, "/posts/self/comments"),

                // 게시글 좋아요
                antMatcher(POST, "/posts/{postId}/like"),
                antMatcher(DELETE, "/posts/{postId}/like"),

                // 댓글
                antMatcher(DELETE, "/comments/{commentId}"),
                antMatcher(PATCH, "/comments/{commentId}"),
                antMatcher(POST, "/comments"),

                // 댓글 좋아요
                antMatcher(POST, "/comments/{commentId}/like"),
                antMatcher(DELETE, "/comments/{commentId}/like"),

                // 포인트
                antMatcher(POST, "/points")
        );

        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * 어드민 권한이 필요한 endpoint
     */
    private RequestMatcher[] adminRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                // 미션
                antMatcher(POST, "/missions"),
                antMatcher(DELETE, "/missions")
        );

        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * Spring Security 기본 설정 메서드
     */
    private void defaultSecuritySetting(HttpSecurity http) throws Exception {
        http
                // JWT, OAuth 기반
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // Form 기반 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .rememberMe(AbstractHttpConfigurer::disable) // 세션 기반의 인증 비활성화
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 기능 비활성화
                .anonymous(AbstractHttpConfigurer::disable) // 익명 사용자 접근 비할성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 생성하지 않음
                .headers(headers -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable) // X-Frame-Options 헤더 비활성화, 클릭재킹 공격 방지
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2FailureHandler)
                        .userInfoEndpoint(userinfo -> userinfo
                                .userService(customOAuth2UserService))
                ); // OAuth2 로그인 설정
    }
}
