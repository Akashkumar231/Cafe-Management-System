package com.akashkumar.cafe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    public ResponseEntity<String> singUp(Map<String,String> requestMap);
}
