package ro.diana.service;


import ro.diana.entity.User;

/**
 * Created by tekin on 2/11/14.
 */
public interface DService {

    // USER
    User getUserById(Integer userId);

    User findUserByEmail(String email);


}
