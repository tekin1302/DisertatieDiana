package ro.diana.service;


import ro.diana.entity.FileHistory;
import ro.diana.entity.User;

import java.util.List;

/**
 * Created by diana on 2/11/14.
 */
public interface DService {

    // USER
    User getUserById(Integer userId);

    User findUserByEmail(String email);

    void saveFileHistory(FileHistory fileHistory, byte[] content);

    List<FileHistory> getFileHistoryList(String filePath);
}
