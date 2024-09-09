package com.dogukan.security.service;


import com.dogukan.entity.concretes.user.User;
import com.dogukan.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

/*
UserDetailsServiceImpl sınıfı, Spring Security tarafından kullanıcı bilgilerini yüklemek için kullanılan bir servis sınıfıdır.
Kullanıcı adı (username) ile bir kullanıcıyı bulur ve Spring Security'ye UserDetails türünde bir nesne döner.
Bu, kullanıcı kimlik doğrulama sürecinin bir parçasıdır.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    // Kullanıcı verilerine erişmek için kullanılan repository
    private final UserRepository userRepository;


    // Kullanıcı adını alarak kullanıcı bilgilerini yükleyen metod
    /*
    Bu metod, Spring Security'nin kullanıcıyı doğrulamak ve kimlik doğrulama işlemlerini yürütmek için ihtiyaç
    duyduğu kullanıcı bilgilerini almasını sağlar. Kullanıcının doğrulanması için gerekli olan bilgileri
    (username, password, authorities gibi) UserDetails nesnesi olarak döner.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //repositoryden Optional olarak donderseydik user kontrolunu if yerine optional ile kontrol ederdik
        // Kullanıcıyı veritabanından kullanıcı adına göre bulur
        User user = userRepository.findByUsernameEquals(username);

        if (user != null) {
            return new UserDetailsImpl(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    false,
                    user.getPassword(),
                    user.getUserRole().getRoleType().name(),
                    user.getSsn()
            );
        }
        throw new UsernameNotFoundException("User " + username + " not found");
    }
}
