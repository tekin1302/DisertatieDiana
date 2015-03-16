package ro.diana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.diana.entity.User;
import ro.diana.repository.UserRepository;

/**
 * Created by diana on 15.03.2015.
 */
@Service
public class DServiceImpl implements DService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Integer userId) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }
}
