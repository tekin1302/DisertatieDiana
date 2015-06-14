package ro.diana.service;

import ro.common.JSTreeNode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by diana on 14.04.2015.
 */
public interface HttpService {
    byte[] doGetRequest(String url) throws IOException, Exception;

    byte[] doPostRequest(String url, Serializable payLoad) throws Exception;

    byte[] getFileContent(String filePath);

    List<JSTreeNode> getWebapps(String root) throws Exception;
}
