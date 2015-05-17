package ro.diana.service;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by diana on 14.04.2015.
 */
public interface HttpService {
    byte[] doGetRequest(String url) throws IOException, Exception;

    byte[] doPostRequest(String url, byte[] payLoad) throws Exception;
}
