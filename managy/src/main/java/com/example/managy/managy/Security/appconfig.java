package com.example.managy.managy.Security;

import com.example.managy.managy.Service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class appconfig  {

  @Autowired
  private MyUserService myUserService;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(registry -> {
        registry.requestMatchers("/home", "/register/**","/css/**","/images/**").permitAll();
        registry.requestMatchers("/admin/**").hasRole("ADMIN");
        registry.requestMatchers("/user/**").hasRole("USER");
        registry.anyRequest().authenticated();
      })
      .logout(log ->
        log.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll()
      )
      .formLogin(httpSecurityFormLoginConfigurer -> {
        httpSecurityFormLoginConfigurer
          .loginPage("/login")
          .successHandler(new AuthenticationSuccessHandler())
          .permitAll();
      })
      .build();
  }

  // @Bean
  // public UserDetailsService userDetailsService() {
  //   UserDetails normalUser = User
  //     .builder()
  //     .username("vishvak")
  //     .password("$2a$12$r/5DRLCLkaoqbPgecI15dux9TSw9R0E6cWjwPIsTHe2YOrzGauY5G")
  //     .roles("USER")
  //     .build();
  //   UserDetails adminUser = User
  //     .builder()
  //     .username("shyam")
  //     .password("$2a$12$1geV4hlzu29Ej.coHr5MgexWoJfBI91sBwC1cgA3zkARr5nbcLhaK")
  //     .roles("ADMIN","USER")
  //     .build();

  //     return new InMemoryUserDetailsManager(normalUser, adminUser);
  // }

  @Bean
  public UserDetailsService userDetailsService() {
    return myUserService;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(myUserService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(16);
  }
}
