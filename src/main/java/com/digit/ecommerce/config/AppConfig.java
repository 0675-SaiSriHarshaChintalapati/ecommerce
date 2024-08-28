package com.digit.ecommerce.config;

import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
    @Bean
    public TokenUtility tokenUtility(){
        return new TokenUtility();
    }
}
