package ro.diana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.diana.entity.FileHistory;

import java.util.List;

/**
 * Created by diana on 15.03.2015.
 */
public interface FileHistoryRepository extends JpaRepository<FileHistory,Integer>{
    Long countByPath(String path);

    List<FileHistory> findAllByPath(String filePath);

    List<FileHistory> findAllByPathOrderByDateDesc(String filePath);
}
