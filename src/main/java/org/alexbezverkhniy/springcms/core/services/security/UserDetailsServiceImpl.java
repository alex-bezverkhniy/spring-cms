package org.alexbezverkhniy.springcms.core.services.security;

import org.alexbezverkhniy.springcms.core.domain.User;
import org.alexbezverkhniy.springcms.core.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bezverkhniy_10534 on 07/08/2014.
 */
@Repository
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User with name %s is not found!", username));
        }

        log.info("loadUserByUsername: " + user);
        return user;
    }
}
