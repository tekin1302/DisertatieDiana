package ro.diana.service;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created by diana on 14.04.2015.
 */
@Service
public class HttpServiceImpl implements HttpService {
    @Override
    public byte[] doGetRequest(String url) throws Exception {
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(url);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        // Execute the method.
        int statusCode = client.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
        }

        // Read the response body.
        byte[] responseBody = method.getResponseBody();

        // Deal with the response.
        // Use caution: ensure correct character encoding and is not binary data
        return responseBody;

    }

    @Override
    public byte[] doPostRequest(String url, Serializable payLoad) throws Exception {
        HttpClient client = new HttpClient();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);){
            ous.writeObject(payLoad);
            // Create a method instance.
            PostMethod method = new PostMethod(url);
            method.setRequestBody(new ByteArrayInputStream(baos.toByteArray()));

            // Provide custom retry handler is necessary
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));

            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            return responseBody;
        }
    }
}
