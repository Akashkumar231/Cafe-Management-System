package com.akashkumar.cafe.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to , String subject, String text , List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("********8");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list!=null && list.size()>0)
        message.setCc(getCcArray(list));
        emailSender.send(message);
    }

    private String [] getCcArray(List<String> list){
        String [] ccArray = new String[list.size()];
        for (int i = 0 ; i<ccArray.length ; i++){
            ccArray[i]=list.get(i);
        }
        return ccArray;
    }

}
