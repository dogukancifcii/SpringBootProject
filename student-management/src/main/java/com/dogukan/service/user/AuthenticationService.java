package com.dogukan.service.user;

import com.dogukan.payload.request.LoginRequest;
import com.dogukan.payload.response.authentication.AuthResponse;
import com.dogukan.repository.UserRepository;
import com.dogukan.security.jwt.JwtUtils;
import com.dogukan.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority) //burada string turune ceviren getAuthority methodu ile yapiyoruz
                .collect(Collectors.toSet());

        Optional<String> role = roles.stream().findFirst();

        //burda builder kullanimda binevi yarım nesne oluşturup yaptık. Çünkü aşağıda rolede değişiklik yapacağımız için. Aşağıdaki kullanım builderin farklı kullanımı oldu.
        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();
        authResponse.username(userDetails.getUsername());
        authResponse.token(token.substring(7));
        authResponse.name(userDetails.getName());
        authResponse.ssn(userDetails.getSsn());

        role.ifPresent(authResponse::role);

        return ResponseEntity.ok(authResponse.build());
    }
}
