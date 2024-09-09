package com.dogukan.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
Bu sınıf, Spring Security'nin kimlik doğrulama ve yetkilendirme mekanizmasında kullanıcı bilgilerini ve rollerini taşımak için gereklidir.
Veritabanından alınan kullanıcı bilgilerini UserDetailsImpl sınıfı kullanarak Spring Security bağlamında kullanıma hazır hale getiririz.
 */
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String username;

    private String name;

    private Boolean isAdviser;

    @JsonIgnore
    private String password;

    private String ssn;

    // Kullanıcının sahip olduğu yetkiler (roller) için Collection
    //rolleri cevirmek icin GrantedAuthority turu
    private Collection<? extends GrantedAuthority> authorities;

    /*
    GRANTED AUTHORITY:
    GrantedAuthority Spring Security'de, bir kullanıcının sahip olduğu yetki veya rolü temsil eden bir arayüzdür.
    Bu arayüz, kullanıcının hangi işlemleri yapmaya veya hangi kaynaklara erişmeye yetkili olduğunu belirlemek için kullanılır.
     */


    // Kullanıcı bilgilerini alarak bir nesne oluşturur ve rollerini (yetkilerini) ayarlar
    public UserDetailsImpl(Long id, String username, String name, Boolean isAdviser, String password, String role, String ssn) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.isAdviser = isAdviser;
        this.password = password;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        //yukarida rol olarak security GrantedAuthority turunde istedigi icin bu donusumu yapmak zorunda kaldik
        this.authorities = grantedAuthorities;
        this.ssn = ssn;
    }




    // Kullanıcının sahip olduğu yetkileri döner.Rolü dondurduk
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Kullanıcının parolasını döner
    @Override
    public String getPassword() {
        return password;
    }

    // Kullanıcının kullanıcı adını döner
    @Override
    public String getUsername() {
        return username;
    }

    // Kullanıcının hesabının süresinin dolup dolmadığını belirtir (Bu durumda her zaman aktif)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Kullanıcının hesabının kilitli olup olmadığını belirtir (Bu durumda her zaman kilitsiz)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Kullanıcının kimlik bilgilerinin süresinin dolup dolmadığını belirtir (Bu durumda her zaman geçerli)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
