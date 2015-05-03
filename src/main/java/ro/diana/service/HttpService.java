package ro.diana.service;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by diana on 14.04.2015.
 */
public interface HttpService {
    byte[] doRequest(String url) throws IOException, Exception;
}
