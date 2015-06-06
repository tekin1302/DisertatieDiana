package ro.diana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.common.JSTreeNode;
import ro.diana.config.DProperties;
import ro.diana.service.HttpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by diana on 16.03.2015.
 */
@Controller
@RequestMapping("/logs")
public class LogsController {

    @Autowired
    private HttpService httpService;

    @RequestMapping("/list")
    public void getAllLogs(HttpServletResponse resp) throws Exception {
        byte[] bytes = httpService.doGetRequest("http://localhost:8080/read/?logs");
        resp.getOutputStream().write(bytes);
    }

    @RequestMapping("/")
    public void getLog(@RequestParam("name") String log, HttpServletResponse resp) throws Exception {
        log = URLDecoder.decode(log, "UTF-8");
        byte[] bytes = httpService.doGetRequest("http://localhost:8080/read/?log=" + log);
        resp.getOutputStream().write(bytes);
    }
}
