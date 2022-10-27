package com.example.auctionchat.config;


import com.example.auctionchat.filter.LocalHostCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor // 요즘 autowierd 대신쓰기위해 나온것
@Configuration
public class AdvancedSecurityConfig {



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /**
     * 필터에서도 써줘야하니 여기서 미리 빈 등록을 해주자
     * */


    private final CorsFilter corsFilter;



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http , AuthenticationManager authenticationManager, HttpServletRequest request) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().hasIpAddress("192.168.219.104")
                //.anyRequest().permitAll()
                .and()
                // cors config 클래스로 설정을 줄꺼여서 그냥 이대로 주석처리
                // 유저 패스워드 값으로 로그인을 진행 안함 , 폼로그인 x
                .formLogin().disable()
                //.cors().disable()
                .csrf().disable()
                .addFilterBefore(new LocalHostCheckFilter(), WebAsyncManagerIntegrationFilter.class)
                .addFilter(corsFilter) // @CrossOrigin (인증 x), 시큐리티 필터 등록 인증
                // 기본적인 http 로그인방식도 사용하지않는다.
                .httpBasic().disable()
                // session은 안하는걸로 , csrf 끄기
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        return http.build();
    }
}
