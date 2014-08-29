package org.alexbezverkhniy.springcms.core.services.security;

import org.alexbezverkhniy.springcms.core.repositories.UserRepository;
import org.alexbezverkhniy.springcms.core.services.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

/**
 * Created by bezverkhniy_10534 on 01/08/2014.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    //private DataSource dataSource;

    //@Autowired
    //private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // DROP auth tables
        /*
        if(dataSource != null) {
            dataSource.getConnection().createStatement().execute("DROP TABLE USERS;\nDROP TABLE AUTHORITIES;");
        }

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser("user").password("user").roles("USER")
                .and().withUser("admin").password("admin").roles("USER", "ADMIN");
        */
        //auth.userDetailsService(new UserDetailsServiceImpl());
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                //.antMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,   "/users/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.PUT,    "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,     "/lib/**", "/js/**", "/img/**", "/css/**", "/fonts/**").permitAll()
                .antMatchers(HttpMethod.POST,    "/login/**").permitAll()

/*
                .antMatchers(HttpMethod.POST,    "/**", "/js/**", "/img/**", "/css/**", "/fonts/**").permitAll()
                .antMatchers(HttpMethod.PUT,     "/**", "/js/**", "/img/**", "/css/**", "/fonts/**").permitAll()
                .antMatchers(HttpMethod.DELETE,  "/**", "/js/**", "/img/**", "/css/**", "/fonts/**").permitAll()
*/
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login").failureUrl("/login?error").permitAll();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new UserDetailsServiceImpl();
    }
}