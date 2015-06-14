package ro.diana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.diana.entity.Extension;
import ro.diana.entity.User;

/**
 * Created by diana on 15.03.2015.
 */
public interface ExtensionRepository extends JpaRepository<Extension,Integer>{
    Extension findOneByExtension(String extension);
}
