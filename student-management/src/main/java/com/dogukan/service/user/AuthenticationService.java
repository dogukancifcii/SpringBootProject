package com.dogukan.service.user;

import com.dogukan.entity.concretes.user.User;
import com.dogukan.exception.BadRequestException;
import com.dogukan.payload.messages.ErrorMessages;
import com.dogukan.payload.request.LoginRequest;
import com.dogukan.payload.request.UpdatePasswordRequest;
import com.dogukan.payload.response.authentication.AuthResponse;
import com.dogukan.repository.user.UserRepository;
import com.dogukan.security.jwt.JwtUtils;
import com.dogukan.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

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

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        String userName = (String) request.getAttribute("username");
        //request isteğinı yani şifre değiştirmek isteyen kişinin usernameini aldık.Kimin şifre değiştirmek istediğine böyle ulaştık

        User user = userRepository.findByUsernameEquals(userName);

        //eski sifre ve yeni sifre uyusuyormu kontrolü
        //passwordEncoder sayesinde hashcode  boyle kontrol edildi.
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorMessages.PASSWORD_NOT_MATCHED);
        }

        if (Boolean.TRUE.equals(user.getBuilt_in())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }
}
