package com.example.sayapker.controller;
import com.example.sayapker.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private MailService mailService;

    @Autowired
    public EmailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/send-email")
    public String sendEmail() {
        mailService.sendSimpleMessage("recipient@example.com", "Subject of the email", "Body of the email.");
        return "Email sent successfully!";
    }
}