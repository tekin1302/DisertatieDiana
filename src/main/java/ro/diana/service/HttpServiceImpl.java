package ro.diana.service;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Service;
import ro.common.JSTreeNode;
import ro.diana.config.DProperties;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

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

    @Override
    public byte[] getFileContent(String filePath) {
        try {
            String url = DProperties.getInstance().getClientRootUrl() + "?get=" + URLEncoder.encode(filePath, "UTF-8");
            return this.doGetRequest(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<JSTreeNode> getWebapps(String root) throws Exception {
        byte[] bytes = this.doGetRequest(DProperties.getInstance().getClientRootUrl() + "?list=" + URLEncoder.encode(root, "UTF-8"));
        List<JSTreeNode> jsTree;
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))){
            jsTree = (List<JSTreeNode>) objectInputStream.readObject();
        }
        return jsTree;
    }
}
