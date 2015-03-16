package ro.diana.service;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.diana.security.DUserDetails;

/**
 * Created by tekin.omer on 3/16/2015.
 */
@Controller
public class LoginController {

    @RequestMapping(value="/principal",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DUserDetails getPrincipal(){
        return (DUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
