package com.app.hungrify.common.security.services;

//import com.app.demo.common.repository.UserRepository;
import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Users user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

    return UserDetailsImpl.build(user);
  }

  @Transactional
  public UserDetails loadUserByUser(String userName) throws UsernameNotFoundException {
    Users user = userRepository.findByUsername(userName)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userName: " + userName));

    return UserDetailsImpl.build(user);
  }

}
