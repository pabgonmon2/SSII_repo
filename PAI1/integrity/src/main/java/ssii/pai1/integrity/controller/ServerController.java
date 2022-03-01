package ssii.pai1.integrity.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.util.JSONPObject;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ssii.pai1.integrity.model.Item;
import ssii.pai1.integrity.service.ServerService;

@RestController
@RequestMapping(value = "/server")
public class ServerController {
    
    private String challenge = "challenge";
    @Autowired
    private ServerService serverService;

    
    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public Map<String,String> requestVerification(Item entity, BindingResult result, HttpServletRequest req, HttpServletResponse resp){
        Map<String, String> response = new HashMap<>();
        response.put("hashFile", entity.getHashFile());
        if(entity.isValid() && serverService.verify(entity)){
            try {
                response.put("mac", serverService.createMAC(entity.getHashFile(),req.getParameter("token"),challenge));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{
            response.put("error", "Hash file not found");
        }
        return response;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Map<String,String> test(Map<String,String> params, HttpServletRequest req, HttpServletResponse resp){
        Map<String, String> response = new HashMap<>();
        try {
            response.put("mac", serverService.createMAC(params.get("hashFile"),params.get("token"),challenge));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public ResponseEntity test2(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("Hago algo.");
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}