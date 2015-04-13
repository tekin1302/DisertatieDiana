package ro.diana.controller;

import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.diana.config.DProperties;
import ro.diana.model.JSTreeNode;

import javax.servlet.ServletContext;
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
import java.util.Arrays;
import java.util.List;


/**
 * Created by diana on 16.03.2015.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/")
    public String redirect(HttpServletRequest request) {
        return "forward:/resources/index.html";
    }

    @RequestMapping("/compileFile")
    @ResponseBody
    public String compileFile(@RequestParam String urlpath,  @RequestParam String content, HttpServletResponse response) throws IOException {
        if (urlpath.endsWith(".class")){
            return compileClassFile(urlpath, content, response);
        } else {
            return compileOtherFile(urlpath, response);
        }
    }

    private String compileOtherFile(String urlpath, HttpServletResponse response) {
        return null;
    }

    private String compileClassFile(String urlpath, final String content, HttpServletResponse response) throws IOException {
        DProperties rootPath = DProperties.getInstance();
        String path = rootPath.getRoot() + urlpath.replace("/","\\");
        String javaFilePath = createJavaFile(path, content);
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> optionList = new ArrayList<String>();
        String classPath = buildClassPath(rootPath.getRoot().replace("/","\\") + "\\disertatie\\WEB-INF\\lib\\*" , rootPath.getRoot().replace("/","\\") + "\\disertatie\\WEB-INF\\classes\\");
        optionList.add("-classpath");
        optionList.add(classPath);
        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(null, null, null);
        File[] files = new File[]{new File(javaFilePath)};

        StringWriter sw = new StringWriter();
        JavaCompiler.CompilationTask task = compiler.getTask(sw, standardJavaFileManager, null, optionList, null, standardJavaFileManager.getJavaFileObjects(files));

        task.call();
        sw.close();
        //sterg .java file
        File deleteFile = new File(javaFilePath);
        if (deleteFile.delete()){
            System.out.println("Fisierul a fost sters");
        } else {
            System.out.println("Eroare la stergere");
        }
        return sw.toString();
    }

    private String createJavaFile(String path, String content) throws IOException {
        String javaPath = path.substring(0,path.lastIndexOf(".")) + ".java";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(javaPath))){
            bw.write(content);
        }
        return javaPath;
    }

    @RequestMapping("/getContent")
    public void decompile(@RequestParam String urlpath, HttpServletResponse response) throws IOException {
       if (urlpath.endsWith(".class")){
           getClassContent(urlpath, response);
       } else {
           getTextContent(urlpath, response);
       }
    }

    private HttpServletResponse getTextContent(String urlpath, HttpServletResponse response) throws IOException {
        DProperties rootPath = DProperties.getInstance();
        String path = rootPath.getRoot() + urlpath.replace("/","\\");
        FileInputStream fis = new FileInputStream(path);
        int c;
        while((c = fis.read()) != -1){
            response.getOutputStream().write(c);
        }
        return response;
    }

    private HttpServletResponse getClassContent(String urlPath, HttpServletResponse response)throws IOException{
        final DecompilerSettings settings = DecompilerSettings.javaDefaults();
        DProperties rootPath = DProperties.getInstance();
        String path = rootPath.getRoot() + urlPath.replace("/","\\");
        Decompiler.decompile(
                path,
                new PlainTextOutput(response.getWriter()),
                settings
        );
        return response;
    }

    @RequestMapping("/getWebApps")
    @ResponseBody
    public List<JSTreeNode> getNodes(@RequestParam String root){
        List<JSTreeNode> jsTree = getFiles(root);
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


    private static String buildClassPath(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (path.endsWith("*")) {
                path = path.substring(0, path.length() - 1);
                File pathFile = new File(path);
                for (File file : pathFile.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".jar")) {
                        sb.append(path);
                        sb.append(file.getName());
                        sb.append(System.getProperty("path.separator"));
                    }
                }
            } else {
                sb.append(path);
                sb.append(System.getProperty("path.separator"));
            }
        }
        return sb.toString();
    }


}
