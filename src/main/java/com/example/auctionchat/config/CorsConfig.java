package com.example.auctionchat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Value("${securityIpAddress.AuthorizedAddress}")
    private String ipPort;

    @Value("${domainName.test}")
    private String ALLOWED_ORIGIN1;

    @Value("${domainName.real}")
    private String ALLOWED_ORIGIN2;

    @Value("${domainName.stest}")
    private String ALLOWED_ORIGIN3;

    @Value("${domainName.sreal}")
    private String ALLOWED_ORIGIN4;

    // 스프링 시큐리티가 들고있는 cors 필터입니다.
    // 프론트쪽에서 계속 막힌게 이녀석 때문
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();

        // 내 서버 데이터 응답시 json을 자바 스크립트에서 처리할수 있도록
        config.setAllowCredentials(true);

        // 지금 코드가 위의 setAloowCredentials 와 같이 사용되는걸 권장한다
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("http://"+ipPort+":3000");
        config.addAllowedOriginPattern(ALLOWED_ORIGIN1);
        config.addAllowedOriginPattern(ALLOWED_ORIGIN2);
        config.addAllowedOriginPattern(ALLOWED_ORIGIN3);
        config.addAllowedOriginPattern(ALLOWED_ORIGIN4);
        config.addAllowedOriginPattern(ALLOWED_ORIGIN1+":3000");
        config.addAllowedOriginPattern(ALLOWED_ORIGIN2+":3000");
        config.addAllowedOriginPattern(ALLOWED_ORIGIN3+":3000");
        config.addAllowedOriginPattern(ALLOWED_ORIGIN4+":3000");
        // 재밌는 점은 아래 코드는 이제 위의 setAllowCredentials 와 함께 사용하는걸
        // 권장하지 않는다
        //config.addAllowedOrigin("*"); // 모든 ip 응답을 허용

        // 해당 헤더를 모두 허용해줘야 프론트에서 확인받아서 체크할수있다.

        // jwt 를 담은 헤더를 리액트 쪽에서 확인할수있다는 뜻
        config.setExposedHeaders(Arrays.asList("Authorization","RefreshToken"));

        config.setAllowedHeaders(Arrays.asList("Authorization","RefreshToken"));

        config.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","OPTIONS"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

