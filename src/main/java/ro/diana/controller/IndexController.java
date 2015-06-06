package ro.diana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.common.AppFile;
import ro.common.JSTreeNode;
import ro.diana.config.DProperties;
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

    @RequestMapping("/")
    public String redirect(HttpServletRequest request) {
        return "forward:/resources/index.html";
    }

    @RequestMapping("/compileFile")
    @ResponseBody
    public void compileFile(@RequestParam String urlpath,  @RequestParam String content, HttpServletResponse response) throws IOException {
        try {
            AppFile appFile = new AppFile(urlpath, content.getBytes());
            httpService.doPostRequest("http://localhost:8080/reader/" , appFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    @RequestMapping("/getContent")
    public void decompile(@RequestParam String urlpath, HttpServletResponse response) throws Exception {
        byte[] bytes = httpService.doGetRequest("http://localhost:8080/reader/?get=" + urlpath);
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
        byte[] bytes = httpService.doGetRequest("http://localhost:8080/reader/?list=" + root);
        List<JSTreeNode> jsTree = null;
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))){
             jsTree = (List<JSTreeNode>) objectInputStream.readObject();
        }
        return jsTree;
    }

    private List<JSTreeNode> getFiles(String root){
            List<JSTreeNode> result = new ArrayList<JSTreeNode>();
            Path path = Paths.get("");
            path = path.toAbsolutePath();
            path = path.getParent();
            if (root == null || root.equals("#")) root = "";
            DProperties dProperties = DProperties.getInstance();
            if (dProperties.getRoot() == null){
                dProperties.setRoot(path.toString() + "\\webapps");
            };
            path = Paths.get(path.toString() + "/webapps" + "/" + root);
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){
            for (Path childPath : directoryStream) {
                JSTreeNode jsTreeNode = new JSTreeNode();
                jsTreeNode.setId(root + "/" + childPath.getFileName());
                jsTreeNode.setText(childPath.getFileName().toString());
                jsTreeNode.setChildren(childPath.toFile().list()!= null ? true : false);
                jsTreeNode.setType(getType(childPath));
                result.add(jsTreeNode);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    private String getType(Path fileName) {
        if (fileName.toFile().isDirectory()){
            return "type_directory";
        }else if (fileName.toString().endsWith(".class")){
            return "type_class";
        }else if (fileName.toString().endsWith(".xml")){
            return "type_xml";
        }else if (fileName.toString().endsWith(".jsp")){
            return "type_jsp";
        }else if (fileName.toString().endsWith(".html")){
            return "type_html";
        }else if (fileName.toString().endsWith(".js")){
            return "type.js";
        }else if (fileName.toString().endsWith(".css")){
            return "type_css";
        }else if (fileName.toString().endsWith(".MF")){
            return "type_mf";
        }

        return null;
    }




}
