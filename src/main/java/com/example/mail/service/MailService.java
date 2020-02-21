package com.example.mail.service;

import com.example.mail.dto.Result;
import com.example.mail.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@Component
public class MailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送验证邮件
     * @param to
     * @param userName
     * @return
     */
    @Async
    public Result sendVerifyMail(String to, String userName){
        try{
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("这是来自XXX的验证邮件！");
            //产生验证码并保存在redis中
            String verifyCode= MD5.getMd5(new SimpleDateFormat().toLocalizedPattern());    //根据日期随机生成验证码
            redisTemplate.opsForValue().set(userName,verifyCode,600, TimeUnit.SECONDS);
            String url = "localhost:8080/mail/verify?username="+userName+"&verifycode="+verifyCode;
            //设置模板
            Context context = new Context();
            context.setVariable("username",userName);
            context.setVariable("url",url);//设置验证链接
            String emailContent = templateEngine.process("mailtemplate.html",context); //指定模板路径
            helper.setText(emailContent,true);//加入发送内容
            //发送
            javaMailSender.send(message);
            return new Result(true,url);
        } catch (MessagingException e) {
            return new Result(false,"send mail false.",-200);
        }
    }

    /**
     * 发送普通邮件
     * @param to
     * @param cc
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to,String cc,String subject,String content){
        SimpleMailMessage sMsg=new SimpleMailMessage();
        sMsg.setFrom(from);
        sMsg.setTo(to);
        sMsg.setCc(cc); //抄送人
        sMsg.setSubject(subject);
        sMsg.setText(content);
        javaMailSender.send(sMsg);
    }

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param file
     */
    public void sendAttachFileMail(String to, String subject, String content, File file){
        try{
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);//multipart message类型的邮件
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            helper.addAttachment(file.getName(),file);
            javaMailSender.send(message);
        }catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送带图片的邮件
     * @param to
     * @param subject
     * @param content
     * @param srcPath
     * @param resIds
     */
    public void sendMailWithImg(String to, String subject, String content,String[] srcPath,String[] resIds){
        if(srcPath.length!=resIds.length){
            System.out.println("发送失败！");
            return;
        }
        try{
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);//multipart message类型的邮件
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            for(int i=0;i<srcPath.length;i++){
                FileSystemResource res=new FileSystemResource(new File(srcPath[i]));
                helper.addInline(resIds[i],res);    //分别传入图片资源Id和路径
            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("发送失败");
        }
    }
}
