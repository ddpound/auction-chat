package com.example.auctionchat.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
@Component
public class LocalHostCheckFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info(request.getRemoteAddr());

        chain.doFilter(request, response);
    }


}
