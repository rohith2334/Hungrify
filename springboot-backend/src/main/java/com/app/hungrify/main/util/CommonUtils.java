package com.app.hungrify.main.util;


import com.app.hungrify.common.security.services.UserDetailsImpl;
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
}