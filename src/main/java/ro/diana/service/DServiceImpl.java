package ro.diana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.diana.entity.Extension;
import ro.diana.entity.FileHistory;
import ro.diana.entity.User;
import ro.diana.repository.ExtensionRepository;
import ro.diana.repository.FileHistoryRepository;
import ro.diana.repository.UserRepository;
import ro.diana.util.DUtils;

import java.util.List;

/**
 * Created by diana on 15.03.2015.
 */
@Service
public class DServiceImpl implements DService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileHistoryRepository fileHistoryRepository;

    @Autowired
    private HttpService httpService;

    @Autowired
    private ExtensionRepository extensionRepository;

    @Override
    public User getUserById(Integer userId) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    @Transactional
    public void saveFileHistory(FileHistory fileHistory, byte[] originalContent) {
        Long count = fileHistoryRepository.countByPath(fileHistory.getPath());

        String extensionName = DUtils.getExtension(fileHistory.getPath());
        Extension extension = extensionRepository.findOneByExtension(extensionName);

        if (count == 0) {
            FileHistory originalFile = new FileHistory();
            originalFile.setExtension(extension);
            originalFile.setContent(originalContent);
            originalFile.setPath(fileHistory.getPath());

            fileHistoryRepository.save(originalFile);
        }
        fileHistory.setExtension(extension);
        fileHistoryRepository.save(fileHistory);
    }

    @Override
    public List<FileHistory> getFileHistoryList(String filePath) {
        return fileHistoryRepository.findAllByPathOrderByDateDesc(filePath);
    }
}
