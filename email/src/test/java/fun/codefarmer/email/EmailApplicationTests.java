package fun.codefarmer.email;

import freemarker.template.Configuration;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import fun.codefarmer.email.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

@SpringBootTest
class EmailApplicationTests {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;

    @Test
    void contextLoads() {
        //普通邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("这是一封测试邮件");
        message.setFrom("405609791@qq.com");
        message.setTo("405609791@qq.com");
        message.setCc("987208623@qq.com");
        message.setBcc("405609791@qq.com");
        message.setSentDate(new Date());
        message.setText("这是测试邮件的正文");
        javaMailSender.send(message);

    }

    @Test
    public void test1() throws MessagingException {
        // 带附件邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("405609791@qq.com");
        helper.setTo("405609791@qq.com");
        helper.setCc("405609791@qq.com");
        helper.setBcc("405609791@qq.com");
        helper.setSentDate(new Date());
        helper.setText("这是测试邮件的正文");
        //1附件名称。2具体附件
        helper.addAttachment("cha.png",new File("F:\\vhr.sql"));
        javaMailSender.send(mimeMessage);

    }

    @Test
    public void test2() throws MessagingException, IOException, TemplateException {
        //使用 Freemarker 作邮件模板
        /**
         * 需要注意的是，虽然引入了 Freemarker 的自动化配置，但是我们在这里是直接 new Configuration
         * 来重新配置 Freemarker 的，所以 Freemarker 默认的配置这里不生效，因此，在填写模板位置时，值为 templates 。
         */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("这是一封测试邮件");
        // 发件人不能随便填写，必须与配置文件中的密码邮箱相匹配。
        helper.setFrom("405609791@qq.com");//发件人
        helper.setTo("405609791@qq.com");//收件人
        helper.setCc("987208623@qq.com");//抄送
        helper.setBcc("987208623@qq.com");//密送
        helper.setSentDate(new Date());
        //构建 Freemarker 的基本配置
        Configuration configuration = new Configuration(new Version("2.3.0"));
        // 配置模板位置
                            //EmailApplication 就是启动类
        ClassLoader loader = EmailApplication.class.getClassLoader();
        configuration.setClassLoaderForTemplateLoading(loader, "templates");
        //加载模板
        Template template = configuration.getTemplate("mail.ftl");
        User user = new User();
        user.setUsername("codefarmer");
        user.setNum(1);
        user.setSalary((double) 99999);
        StringWriter out = new StringWriter();
        //模板渲染，渲染的结果将被保存到 out 中 ，将out 中的 html 字符串发送即可
        template.process(user, out);
        helper.setText(out.toString(),true);
        javaMailSender.send(mimeMessage);
    }

    public void test3() throws MessagingException {
        //使用 Thymeleaf 作邮件模板
        /**
         * 推荐在 Spring Boot 中使用 Thymeleaf 来构建邮件模板。
         * 因为 Thymeleaf 的自动化配置提供了一个 TemplateEngine，
         * 通过 TemplateEngine 可以方便的将 Thymeleaf 模板渲染为 HTML ，同时，Thymeleaf 的自动化配置在这里是继续有效的
         */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("这是一封测试邮件");
        // 发件人不能随便填写，必须与配置文件中的密码邮箱相匹配。
        helper.setFrom("405609791@qq.com");//发件人
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
    }

    @Test
    public void test4() throws MessagingException {
        //带图片资源的图片
        //创建邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("405609791@qq.com");
        helper.setTo("405609791@qq.com");
        helper.setCc("405609791@qq.com");
        helper.setBcc("405609791@qq.com");
        helper.setSentDate(new Date());
        helper.setText("这是测试邮件的正文（带图片）,这是第一张图片：<img src='cid:p01'/>，这是第二张图片<img src='cid:p02'/>",true);
        helper.addInline("p01",new FileSystemResource(new File("F:\\p.jpg")));
        helper.addInline("p02",new FileSystemResource(new File("F:\\p.jpg")));
        javaMailSender.send(mimeMessage);
    }
}
