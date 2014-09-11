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
                .antMatchers("/", "/index.html").permitAll()
                .antMatchers(HttpMethod.PUT,    "/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,   "/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,    "/lib/**", "/js/**", "/img/**", "/css/**", "/fonts/**").permitAll()
                .antMatchers(HttpMethod.POST,   "/login/**").permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error").permitAll()
                    .defaultSuccessUrl("/admin")
                .and()
                .logout().logoutSuccessUrl("/");

    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new UserDetailsServiceImpl();
    }
}