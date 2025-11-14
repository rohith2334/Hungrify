package com.app.hungrify.main.util;


import com.app.hungrify.common.security.services.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CommonUtils {

    //    get current user id
    public Long getUserId(){
        Long user_id= ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return user_id;
    }

    public String getEmail(){
        String email= ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        return email;
    }

    public String getRole(){
        String role= ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().toString();
        System.out.println("roleeee:"+role);
        return role;
    }

    public Date getCurrentDate() {
        return new Date();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}