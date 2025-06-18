package com.akashkumar.cafe.service;

import com.akashkumar.cafe.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    public ResponseEntity<String> singUp(Map<String,String> requestMap);

    public ResponseEntity<String> login(Map<String,String> requestMap);

    public ResponseEntity<List<UserWrapper>> getAllUser();

    public ResponseEntity<String> update(Map<String,String> requestMap);
    
}
