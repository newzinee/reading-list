package com.treabear.readinglist.config;

import java.util.Optional;

import com.treabear.readinglist.domain.Reader;
import com.treabear.readinglist.repository.ReaderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityConfig
 * 자동 구성된 보안을 오버라이드하는 명시적 구성
 * 
 * 스프링 시큐리티는 JDBC 기반, LDAP 기반, 인메모리 사용자 저장소를 포함한 몇 가지 인증 옵션 제공
 * 여기서는 JPA를 이용한 DB 기반으로 사용자 인증
 * 
 *  @Profile 애너테이션은 production 프로파일을 런타임에서 활성화했을 때만 해당 구성을 적용한다. production 프로파일이 활성화 되지 않을 때는 이 설정을 무시하고, 오버라이드된 보안 구성이 없으므로 자동으로 구성된 보안 구성을 적용할 것이다. 
 */
@Profile("production")  
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").access("hasRole('READER')")   // READER 권한 필요
                .antMatchers("/**").permitAll()
            
            .and()
            
            .formLogin()
                .loginPage("/login")    // 로그인 폼 경로 설정
                .failureUrl("/login?error=true");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 사용자 인증을 위해 사용자 상세 정보를 설정하는 서비스
        auth.userDetailsService(new UserDetailsService(){
            // 제공된 사용자 이름으로 사용자의 상세 정보를 조회할 때 사용
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                // OR 2. encoding
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                Optional<Reader> reader = readerRepository.findById(username);
                if(reader.isPresent()) {
                    reader.get().setPassword(encoder.encode(reader.get().getPassword()));
                }
                System.out.println(reader.get());
                return reader.get();
            
                // return readerRepository.findOne(username);
            }
        });
        // .passwordEncoder(NoOpPasswordEncoder.getInstance());    // 1. deprecated 
    }

}