package ro.diana.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.diana.model.Credentials;
import ro.diana.security.DUserDetails;

/**
 * Created by tekin.omer on 3/16/2015.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(value="/principal",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DUserDetails getPrincipal(){
        return (DUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @RequestMapping(value="/denied")
    public ResponseEntity accessDenied() {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
    @RequestMapping(value="/needAuth")
    public ResponseEntity needAuth() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
