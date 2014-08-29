package org.alexbezverkhniy.springcms.core.repositories;

import org.alexbezverkhniy.springcms.Application;
import org.alexbezverkhniy.springcms.core.domain.Authority;
import org.alexbezverkhniy.springcms.core.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

//@ContextConfiguration(locations = "/META-INF/spring/application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("local")
@PropertySource("application.properties")
public class UserRepositoryTests {

	protected static Logger log = LoggerFactory.getLogger(UserRepositoryTests.class);

	@Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;


	User user;
	Authority authority;

	@Before
	public void setUp() {
		user = new User();
		user.setUsername("alexhustas");
        user.setPassword("alexhustas");

        authority = new Authority();
        authority.setAuthority("USER");
        authorityRepository.save(authority);
        user.addAuthority(authority);
		user = userRepository.save(user);
	}

	@After
	public void tearDown() throws Exception {
		userRepository.delete(user);
	}

	@Test
    @Transactional
	public void findById() {

		User u = userRepository.findOne(user.getId());
		assertEquals(user, u);
	}



    @Test
    @Transactional
    public void findByUsername() {

        User u = userRepository.findByUsername(user.getUsername());
        assertEquals(user, u);
    }

    @Test
    @Transactional
    public void addRemoveRole() throws Exception {

        User user = userRepository.findByUsername(this.user.getUsername());

        assertNotNull(user.getAuthorities());

        // Add role
        Authority admin = new Authority();
        admin.setAuthority("ADMIN");
        authorityRepository.save(admin);

        user.addAuthority(admin);
        userRepository.save(user);

        user = userRepository.findByUsername(this.user.getUsername());
        log.info("user: " + user.toString());

        assertTrue(user.getAuthorities().size() == 2);
        for (Authority a : user.getAuthorities()) {
            log.info("user role: " + a.getAuthority());
        }

        // Remove role
        admin = (Authority) user.getAuthorities().toArray()[1];
        user.getAuthorities().remove(admin);
        user = userRepository.save(user);
        authorityRepository.delete(admin);

        user = userRepository.findByUsername(this.user.getUsername());
        log.info("user: " + user.toString());

        assertTrue(user.getAuthorities().size() == 1);
        for (Authority a : user.getAuthorities()) {
            log.info("user role: " + a.getAuthority());
        }

    }

}
