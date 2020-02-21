package com.example.mail.controller;


import com.example.mail.dto.Result;
import com.example.mail.entity.LocalAuth;
import com.example.mail.entity.PersonInfo;
import com.example.mail.service.MailService;
import com.example.mail.service.PersonInfoService;
import com.example.mail.util.HttpServletRequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PersonInfoController {
    @Autowired
    PersonInfoService personInfoService;
    @Autowired
    MailService mailService;

    @RequestMapping(value = "/register")
    private String register(){
        return "register";
    }

    @ResponseBody
    @RequestMapping(value = "/registe",method = RequestMethod.POST)
    private Map<String,Object> registe(HttpServletRequest request) throws JsonProcessingException {
        Map<String,Object> modelMap = new HashMap<String,Object>();
        Result saveResult=null;
        Result mailResult=null;
        ObjectMapper mapper = new ObjectMapper();
        String localAuthStr = HttpServletRequestUtil.getString(request,
                "localAuthStr");
        LocalAuth localAuth = mapper.readValue(localAuthStr,LocalAuth.class);
        PersonInfo personInfo = localAuth.getPersonInfo();
        if(personInfo==null){
            modelMap.put("success",false);
            modelMap.put("errMsg","personInfo is null.");
            return modelMap;
        }else if (localAuth==null){
            modelMap.put("success",false);
            modelMap.put("errMsg","localAuth is null.");
            return modelMap;
        }else {
            saveResult = personInfoService.savePersonInfo(personInfo, localAuth);
            if (saveResult.isSuccess()){
                if (personInfo.getEmail()!=null) {
                    mailResult = mailService.sendVerifyMail(personInfo.getEmail(), localAuth.getUserName());
                    System.out.println(mailResult.getData().toString());
                }
                else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg","email is null.");
                    return modelMap;
                }
                modelMap.put("success",false);
                modelMap.put("errMsg","insert is null.");
                return modelMap;
            }
        }
        System.out.println("发送成功！");
        modelMap.put("success",true);
        modelMap.put("msg","请前往邮箱"+personInfo.getEmail()+"验证！");
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/verify",method = RequestMethod.GET)
    private Map<String,Object> verify(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        String userName=HttpServletRequestUtil.getString(request,"username");
        String verifyCode=HttpServletRequestUtil.getString(request,"verifycode");
        Result effected=personInfoService.enableAuth(userName,verifyCode);
        if (effected.isSuccess()){
            modelMap.put("success",true);
            modelMap.put("username",userName);
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg",effected.getErrorMsg());
            return  modelMap;
        }
    }
}
