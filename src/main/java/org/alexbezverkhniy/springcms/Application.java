package org.alexbezverkhniy.springcms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexbezverkhniy.springcms.core.domain.Authority;
import org.alexbezverkhniy.springcms.core.domain.Comment;
import org.alexbezverkhniy.springcms.core.domain.User;
import org.alexbezverkhniy.springcms.core.repositories.AuthorityRepository;
import org.alexbezverkhniy.springcms.core.repositories.CommentRepository;
import org.alexbezverkhniy.springcms.core.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {"org.alexbezverkhniy.springcms.core.services.security", "org.alexbezverkhniy.springcms.core.repositories", "org.alexbezverkhniy.springcms.web.controllers"})
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "org.alexbezverkhniy.springcms.core.repositories")
//@Import(RestDataConfig.class)
public class Application  extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
    }

    /*
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {

                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
                //ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");

                container.addErrorPages(error401Page, error404Page, error500Page);
            }
        };
    }
    */

    public static void main(String[] args) {

        //SpringApplication.run(Application.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(Application.class);

        // Initial data
        UserRepository userRepository = context.getBean(UserRepository.class);
        AuthorityRepository authorityRepository = context.getBean(AuthorityRepository.class);

        CommentRepository commentRepository = context.getBean(CommentRepository.class);

        Authority adminRole;
        Authority userRole;
        User user;
        User admin;

        adminRole = new Authority();
        adminRole.setAuthority("ADMIN");
        authorityRepository.save(adminRole);

        userRole = new Authority();
        userRole.setAuthority("USER");
        authorityRepository.save(userRole);

        user = new User();
        user.setUsername("alexhustas");
        user.setPassword("alexhustas");
        user.addAuthority(userRole);
        user.addAuthority(adminRole);
        userRepository.save(user);

        admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEnabled(true);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.addAuthority(adminRole);
        admin.addAuthority(userRole);
        userRepository.save(admin);


        user = new User();
        user.setUsername("bob");
        user.setPassword("bob");
        user.addAuthority(userRole);
        userRepository.save(user);

        user = new User();
        user.setUsername("jack");
        user.setPassword("jack");
        user.addAuthority(userRole);
        userRepository.save(user);

        Comment comment1 = new Comment(user, "jack comment", "First comment");
        commentRepository.save(comment1);

        Comment comment2 = new Comment(user, "jack comment", "Second comment");
        commentRepository.save(comment2);

        user.addComment(comment1);
        user.addComment(comment2);
        userRepository.save(user);

        Comment comment3 = new Comment(admin, "Admin comment", "**On** - Used to express a surface of something. [see more...](http://www.talkenglish.com/Grammar/prepositions-on-at-in.aspx)");
        commentRepository.save(comment3);

        Comment comment4 = new Comment(admin, "Admin comment", "**Best way** to improve vocabulary [lingualeo.com](http://lingualeo.com)");
        commentRepository.save(comment4);

        admin.addComment(comment3);
        admin.addComment(comment4);
        userRepository.save(admin);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(System.out, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}