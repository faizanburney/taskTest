package com.freenow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter
{

    @Value("${application.user}")
    private String user;

    @Value("${application.password}")
    private String password;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {

        auth.inMemoryAuthentication()
            .withUser(this.user)
            .password("{noop}"+this.password)
            .authorities("USER");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {

        http.authorizeRequests()
            .antMatchers("/**")
            .authenticated()
            .and()
            .httpBasic();

        http.csrf()
            .disable().formLogin();
    }
}
