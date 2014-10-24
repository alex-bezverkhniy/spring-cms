package org.alexbezverkhniy.springcms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alexbezverkhniy.springcms.core.domain.*;
import org.alexbezverkhniy.springcms.core.repositories.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.Date;

@Configuration
@ComponentScan
//@ComponentScan(basePackages = {"org.alexbezverkhniy.springcms.core.services.security", "org.alexbezverkhniy.springcms.core.repositories", "org.alexbezverkhniy.springcms.web.controllers"})
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
        //UserRepository userRepository = context.getBean(UserRepository.class);
        AuthorityRepository authorityRepository = context.getBean(AuthorityRepository.class);
        CommentRepository commentRepository = context.getBean(CommentRepository.class);
        PostRepository postRepository = context.getBean(PostRepository.class);
        AuthorRepository authorRepository = context.getBean(AuthorRepository.class);
        CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);

        Authority adminRole;
        Authority userRole;
        Author user;
        Author admin;

        adminRole = new Authority();
        adminRole.setAuthority("ADMIN");
        authorityRepository.save(adminRole);

        userRole = new Authority();
        userRole.setAuthority("USER");
        authorityRepository.save(userRole);

        user = new Author();
        user.setUsername("alexhustas");
        user.setPassword("alexhustas");
        user.addAuthority(userRole);
        user.addAuthority(adminRole);
        authorRepository.save(user);

        admin = new Author();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEnabled(true);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.addAuthority(adminRole);
        admin.addAuthority(userRole);
        authorRepository.save(admin);


        user = new Author();
        user.setUsername("bob");
        user.setPassword("bob");
        user.addAuthority(userRole);
        authorRepository.save(user);

        user = new Author();
        user.setUsername("jack");
        user.setPassword("jack");
        user.addAuthority(userRole);
        authorRepository.save(user);

        Comment comment1 = new Comment(user, "jack comment", "First comment");
        commentRepository.save(comment1);

        Comment comment2 = new Comment(user, "jack comment", "Second comment");
        commentRepository.save(comment2);

        //user.addComment(comment1);
        //user.addComment(comment2);
        authorRepository.save(user);

        Comment comment3 = new Comment(admin, "Admin comment", "**On** - Used to express a surface of something. [see more...](http://www.talkenglish.com/Grammar/prepositions-on-at-in.aspx)");
        commentRepository.save(comment3);

        Comment comment4 = new Comment(admin, "Admin comment", "**Best way** to improve vocabulary [lingualeo.com](http://lingualeo.com)");
        commentRepository.save(comment4);

        /*
        Comment commentN;
        for(int i = 0; i <= 50; i++) {
            commentN = new Comment(user, "jack comment " + i, "Comment №"+i);
            commentRepository.save(commentN);
        }
        */

        //admin.addComment(comment3);
        //admin.addComment(comment4);
        authorRepository.save(admin);


        admin.setRegistrationDate(new Date());
        authorRepository.save(admin);

        Post post1 = new Post();
        post1.setTitle("Hello");
        post1.setText("Hi there!");
        post1.setAuthor(admin);
        postRepository.save(post1);

        post1.addComment(comment1);
        postRepository.save(post1);

        Category parentCategory = new Category("Parent", "Parent Category");
        categoryRepository.save(parentCategory);

        Category category1 = new Category("First", "First child", parentCategory);
        //category1.addComment(post1);
        categoryRepository.save(category1);

        Category category2 = new Category("Second", "Second child", parentCategory);
        //category2.addComment(post1);
        categoryRepository.save(category2);


        post1.addCategory(category1);
        postRepository.save(post1);

        Post postN;
        for(int i = 0; i <= 50; i++) {
            postN = new Post();
            postN.setTitle("Hello #" + i);
            postN.setText("Post text #" + i);
            post1.setAuthor(admin);
            postRepository.save(postN);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(System.out, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
