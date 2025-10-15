package com.appointment.booking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import org.springframework.security.core.authority.AuthorityUtils;


@Configuration

public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();


    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails Professor = User.withUsername("Professor@iu.edu").password("profpass").roles("PROFESSOR").build();

        UserDetails student1 = User.withUsername("student1@iu.edu").password("student1").roles("STUDENT").build();
        UserDetails student2 = User.withUsername("student2@iu.edu").password("student2").roles("STUDENT").build();
        UserDetails student3 = User.withUsername("student3@iu.edu").password("student3").roles("STUDENT").build();
        UserDetails student4 = User.withUsername("student4@iu.edu").password("student4").roles("STUDENT").build();
        UserDetails student5 = User.withUsername("student5@iu.edu").password("student5").roles("STUDENT").build();
        UserDetails student6 = User.withUsername("student6@iu.edu").password("student6").roles("STUDENT").build();
        UserDetails student7 = User.withUsername("student7@iu.edu").password("student7").roles("STUDENT").build();
        UserDetails student8 = User.withUsername("student8@iu.edu").password("student8").roles("STUDENT").build();
        
        UserDetails ta1 = User.withUsername("ta1@iu.edu").password("tapass").roles("TA").build();
        UserDetails ta2 = User.withUsername("ta2@iu.edu").password("tapass").roles("TA").build();

        return new InMemoryUserDetailsManager(Professor, student1, student2, student3, student4, student5, student6, student7, student8, ta1, ta2);



        }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
            .requestMatchers("/professor-dashboard").hasRole("PROFESSOR")
            .requestMatchers("/student-dashboard").hasRole("STUDENT")
            .requestMatchers("/ta-dashboard").hasRole("TA")
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(myAuthenticationSuccessHandler())
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();

        return http.build();

    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return (request, response, authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            if (roles.contains("ROLE_PROFESSOR")) { response.sendRedirect("/professor-dashboard"); }
            else if (roles.contains("ROLE_STUDENT")) { response.sendRedirect("/student-dashboard"); }
            else if (roles.contains("ROLE_TA")) { response.sendRedirect("/ta-dashboard"); }
            else { response.sendRedirect("/default"); }
            
        };
    }

    
    
}
