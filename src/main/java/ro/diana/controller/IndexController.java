package ro.diana.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.common.AppFile;
import ro.common.JSTreeNode;
import ro.diana.config.DProperties;
import ro.diana.entity.FileHistory;
import ro.diana.model.CompileModel;
import ro.diana.service.DSecurityService;
import ro.diana.service.DService;
import ro.diana.service.HttpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
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
@RequestMapping("/")
public class IndexController {

    @Autowired
    private HttpService httpService;

    @Autowired
    private DService dService;

    @Autowired
    private DSecurityService dSecurityService;

    @RequestMapping("/")
    public String redirect(HttpServletRequest request) {
        return "forward:/resources/index.html";
    }

    @RequestMapping(value = "/compileFile", method = RequestMethod.POST)
    @ResponseBody
    public String compileFile(@RequestBody CompileModel compileModel) throws Exception {
        byte[] originalContent = httpService.getFileContent(compileModel.getPath());
        AppFile appFile = new AppFile(compileModel.getPath(), compileModel.getContent().getBytes());
        byte[] compilationResultBytes = httpService.doPostRequest(DProperties.getInstance().getClientRootUrl() , appFile);
        String compilationResult = new String(compilationResultBytes);
        if (compilationResult == null || compilationResult.length() == 0) {
            FileHistory fileHistory = new FileHistory();
            fileHistory.setContent(appFile.getContent());
            fileHistory.setDate(new DateTime());
            fileHistory.setPath(compileModel.getPath());
            fileHistory.setUser(dSecurityService.getCurrentUser());

            dService.saveFileHistory(fileHistory, originalContent);
        }
        return compilationResult;
    }

    @RequestMapping("/getContent")
    public void decompile(@RequestParam String urlpath, HttpServletResponse response) throws Exception {

        byte[] bytes = httpService.getFileContent(urlpath);
        if (urlpath.endsWith(".class") || urlpath.endsWith(".xml") || urlpath.endsWith(".jsp") || urlpath.endsWith(".html")
                    || urlpath.endsWith(".js") || urlpath.endsWith(".css") || urlpath.endsWith(".MF") ) {
            response.getWriter().write(new String(bytes));
        } else {
            response.setContentType("application/octet-stream");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition","attachment; filename=\"" + urlpath +"\"");
        }
    }

    @RequestMapping("/getWebApps")
    @ResponseBody
    public List<JSTreeNode> getNodes(@RequestParam String root) throws Exception {
        return httpService.getWebapps(root);
    }
}
