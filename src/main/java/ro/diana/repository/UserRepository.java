package ro.diana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.diana.entity.User;

/**
 * Created by diana on 15.03.2015.
 */
public interface UserRepository extends JpaRepository<User,Integer>{
    User findOneByEmail(String email);
}
