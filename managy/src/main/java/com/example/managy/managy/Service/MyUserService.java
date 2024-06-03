package com.example.managy.managy.Service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.managy.managy.Entity.MyUser;
import com.example.managy.managy.Repository.MyUserRepository;

@Service
public class MyUserService implements UserDetailsService {

  @Autowired
  MyUserRepository myUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    // TODO Auto-generated method stub
    Optional<MyUser> user = myUserRepository.findByUsername(username);
    if (user.isPresent()) {
      return User
        .builder()
        .username(user.get().getUsername())
        .password(user.get().getPassword())
        .roles(getRoles(user.get()))
        .build();
    } else {
      throw new UsernameNotFoundException(username);
    }
  }

  private String[] getRoles(MyUser myUser) {
    // TODO Auto-generated method stub
    if (myUser.getRole() == null) {
      return new String[] { "USER" };
    }
    return myUser.getRole().split(",");
  }

public MyUser save(MyUser user) {
    // TODO Auto-generated method stub
    return myUserRepository.save(user);
}
}
