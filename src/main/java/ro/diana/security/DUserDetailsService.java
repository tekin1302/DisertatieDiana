package ro.diana.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.diana.entity.User;
import ro.diana.service.DService;


public class DUserDetailsService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(DUserDetailsService.class);

    @Autowired
    private DService service;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = null;
        username = username.trim();

        logger.debug("loadUserByUsername( " + username + ")");
        try {
            user = service.findUserByEmail(username);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (user != null) {
            DUserDetails authUser = new DUserDetails();
            authUser.setPassword(user.getPassword());
            authUser.setEnabled(true);
            authUser.setUserId(user.getId());
            authUser.setEmail(user.getEmail());
            authUser.addAuthority(user.getRole());

            logger.debug("User login");
            return authUser;
        } else {
            logger.debug("Username not found.");
            throw new UsernameNotFoundException("");
        }
    }
}
