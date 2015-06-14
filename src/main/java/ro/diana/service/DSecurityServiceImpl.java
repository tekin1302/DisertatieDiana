package ro.diana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.diana.entity.User;
import ro.diana.repository.UserRepository;
import ro.diana.security.DUserDetails;


@Service
public class DSecurityServiceImpl implements DSecurityService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        DUserDetails principal = (DUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findOne(principal.getUserId());
    }
}
