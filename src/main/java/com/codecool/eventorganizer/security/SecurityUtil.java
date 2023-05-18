package com.codecool.eventorganizer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpStatusRequestRejectedHandler;
import org.springframework.security.web.firewall.RequestRejectedHandler;

@Order(1)
@Configuration
public class SecurityUtil {
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RequestRejectedHandler requestRejectedHandler() {
        // TODO ask why is it working (tomcat html) and not working (still 500 with blank) at the same time
        return new HttpStatusRequestRejectedHandler();
    }
}


