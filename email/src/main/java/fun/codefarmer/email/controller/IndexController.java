package fun.codefarmer.email.controller;

import fun.codefarmer.email.pojo.Email;
import fun.codefarmer.email.pojo.User;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class IndexController {
    private final static Logger log = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;

    @GetMapping("/index")
    public void index(Email mail) throws MessagingException {
        // mail 中获取要发送的数据
        //mail.getTitle();
        try{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("这是一封测试邮件");
        // 发件人不能随便填写，必须与配置文件中的密码邮箱相匹配。
        helper.setFrom(from);//发件人 "405609791@qq.com"
        helper.setTo("405609791@qq.com");//收件人
        helper.setCc("987208623@qq.com");//抄送
        helper.setBcc("987208623@qq.com");//密送
        helper.setSentDate(new Date());
        Context context = new Context();
        context.setVariable("username", "javaboy");
        context.setVariable("num","000001");
        context.setVariable("salary", "99999");
        String process = templateEngine.process("mail.html", context);
        helper.setText(process,true);
        javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("模板邮件发送失败->message:{}",e.getMessage());
        }
    }

}
