package com.example.webshop.newUser;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class ConfirmationEmailService implements JavaDelegate {
    @Autowired
    IdentityService identityService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String processInstanceId = execution.getProcessInstanceId();
        String address = (String)execution.getVariable("email");

        System.out.println(address);

        String link = "http://localhost:4200/activate/"+processInstanceId;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mail  = new MimeMessageHelper(mimeMessage,true);
        mail.setTo(address);
        mail.setText("Hello, you can confirm your registration by clicking on this link: "+link);
        mail.setSubject("Registration confirmation");
        mail.setFrom("naucna.centrala28@gmail.com");
        mail.setSentDate(new Date());

        javaMailSender.send(mimeMessage);
    }
}
