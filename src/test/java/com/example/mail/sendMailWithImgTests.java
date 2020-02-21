package com.example.mail;

import com.example.mail.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class sendMailWithImgTests {
    @Autowired
    MailService mailService;
    @Test
    public void sendMailWithImg(){
        mailService.sendMailWithImg("1915368237@qq.com",
                "测试邮件主题（图片）","<div>hello,这是一封带图片资源的邮件："+
                "图1：<div><img src='cid:p01'/></div></div>",
                new String[]{"C:\\Users\\Admin\\Desktop\\watermark.jpg"},
                new String[]{"p01"});
    }
}
