//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.23 at 02:20:55 PM CEST 
//


package rs.ac.uns.naucnacentrala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;


@Entity
@Data
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", unique = false, nullable = false)
    private String password;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "prezime", unique = false, nullable = false)
    private String prezime;

    @Column(name = "ime", unique = false, nullable = false)
    private String ime;

    @Column(name = "email", unique = true, nullable = false)
    private String email;


    private String grad;

    private String titula;

    private String drzava;


    @Column(name = "last_password_reset_date")
    private Timestamp lastSifraResetDate;

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "user_naucneoblasti",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "naucnaoblast_id", referencedColumnName = "id"))
    private List<NaucnaOblast> naucneOblasti=new ArrayList<>();

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    @ManyToMany(mappedBy = "urednici",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    private Set<Casopis> uredjujemCasopise=new HashSet<>();

    @ManyToMany(mappedBy = "recezenti",cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    private List<Casopis> recenziramCasopise=new ArrayList<Casopis>();



    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

}