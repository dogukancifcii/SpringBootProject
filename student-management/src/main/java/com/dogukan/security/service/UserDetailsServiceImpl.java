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
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //repositoryden Optional olarak donderseydik user kontrolunu if yerine optional ile kontrol ederdik
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
        throw new UsernameNotFoundException("User " + username + "not found");
    }
}
