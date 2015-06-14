package ro.diana.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.common.AppFile;
import ro.common.JSTreeNode;
import ro.diana.config.DProperties;
import ro.diana.entity.FileHistory;
import ro.diana.service.DSecurityService;
import ro.diana.service.DService;
import ro.diana.service.HttpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by diana on 16.03.2015.
 */
@Controller
@RequestMapping("/fileHistory")
public class FileHistoryController {

    @Autowired
    private DService dService;

    @RequestMapping("/list")
    @ResponseBody
    public List<FileHistory> compileFile(@RequestParam String filePath) {
        return dService.getFileHistoryList(filePath);
    }
}
