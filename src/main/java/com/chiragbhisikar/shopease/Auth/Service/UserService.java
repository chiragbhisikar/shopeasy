package com.chiragbhisikar.shopease.Auth.Service;

import com.chiragbhisikar.shopease.Auth.Repository.UserDetailRepository;
import com.chiragbhisikar.shopease.DTO.Auth.RegistrationRequestDTO;
import com.chiragbhisikar.shopease.DTO.Auth.RegistrationResponseDTO;
import com.chiragbhisikar.shopease.Exception.AlreadyExistsException;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;
    private final EmailService emailService;

    public RegistrationResponseDTO createUser(RegistrationRequestDTO request) {
        Optional<User> existing = userDetailRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new AlreadyExistsException("User Already Exist With " + request.getEmail() + " Email !");
        }


        try {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setProvider("manual");

            String code = this.generateCode();
            user.setVerificationCode(code);
            user.setAuthorities(authorityService.getUserAuthority());
            userDetailRepository.save(user);
            emailService.sendMail(user);


            return RegistrationResponseDTO.builder()
                    .code(200)
                    .message("User created!")
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServerErrorException(e.getMessage(), e.getCause());
        }
    }


    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public Boolean verifyUser(String email, String code) {
        User user = userDetailRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Exist With " + email + " Email "));

        if (user.getVerificationCode().equals(code)) {
            user.setEnabled(true);
            userDetailRepository.save(user);
            return true;
        }
        return false;
    }
}
