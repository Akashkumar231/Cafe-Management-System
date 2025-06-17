package com.akashkumar.cafe.serviceImpl;

import com.akashkumar.cafe.JWT.CustomerUserDetailsService;
import com.akashkumar.cafe.JWT.JwtFilter;
import com.akashkumar.cafe.JWT.JwtUtil;
import com.akashkumar.cafe.POJO.User;
import com.akashkumar.cafe.constants.CafeConstants;
import com.akashkumar.cafe.dao.UserDao;
import com.akashkumar.cafe.service.UserService;
import com.akashkumar.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public ResponseEntity<String> singUp(Map<String, String> requestMap) {


        log.info("Inside signup {}" , requestMap);

       try {
           User user = userDao.findByEmailId(requestMap.get("email"));

           if (validateSignUpMap(requestMap)){

               if (Objects.isNull(user)){

                   userDao.save(getUserFromMap(requestMap));

                   return CafeUtils.getResponseEntity("Successfully Registered.",HttpStatus.OK);

               }else {
                   return CafeUtils.getResponseEntity("Email already exists.",HttpStatus.BAD_REQUEST);
               }


           }else{
               return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
           }
       }catch (Exception exception){
           exception.printStackTrace();
       }
return  CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }


    public boolean validateSignUpMap(Map<String, String> requestMap){
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login {} " ,requestMap);
        try{

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if (authentication.isAuthenticated()){
                if (customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+jwtUtil.generateToken(customerUserDetailsService.getUserDetails().getEmail(),customerUserDetailsService.getUserDetails().getRole())+"\"}",HttpStatus.OK);
                }else {
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception exception){
            log.error("{}",exception);
        }


 return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",HttpStatus.BAD_REQUEST);

    }

}
