package rs.ac.uns.naucnacentrala.userServices;

import rs.ac.uns.naucnacentrala.model.Korisnik;
import rs.ac.uns.naucnacentrala.common.security.auth.JwtAuthenticationRequest;

public interface LoginService {

    public Korisnik checkCredentials(JwtAuthenticationRequest request);
}
