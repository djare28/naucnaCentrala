package com.example.webshop.userServices;

import com.example.webshop.model.Korisnik;
import com.example.webshop.newUser.AuthorityRepository;
import com.example.webshop.newUser.KorisnikRepository;
import com.example.webshop.common.security.auth.JwtAuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements  LoginService{

    @Autowired
    AuthorityRepository authorityRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    KorisnikRepository userRepository;


    @Override
    public Korisnik checkCredentials(JwtAuthenticationRequest request) {
        Korisnik user=userRepository.findOneByUsername(request.getUsername());
        if(user!=null && user.getAktiviran()==true){
            if(passwordEncoder().matches(request.getPassword(),user.getPassword())){
                return user;
            }
        }
        return null;
    }
}