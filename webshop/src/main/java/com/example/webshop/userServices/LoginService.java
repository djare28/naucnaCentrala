package com.example.webshop.userServices;

import com.example.webshop.model.Korisnik;
import com.example.webshop.common.security.auth.JwtAuthenticationRequest;

public interface LoginService {

    public Korisnik checkCredentials(JwtAuthenticationRequest request);
}
