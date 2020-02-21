package com.example.mail;

import com.example.mail.service.MailService;
import com.example.mail.util.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendAttachFileMailTests {
    @Autowired
    MailService mailService;
    @Test
    public void sendAttachFileMail(){
       /* mailService.sendAttachFileMail("1915368237@qq.com",
                "测试邮件主题","测试邮件内容",new File("C:\\Users\\Admin\\Desktop\\123.docx"));*/
        String content= MD5.getMd5(new SimpleDateFormat().toLocalizedPattern());    //根据日期随机生成验证码
        System.out.println(content);
    }
}
