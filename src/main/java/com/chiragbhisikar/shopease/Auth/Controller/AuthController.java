package com.chiragbhisikar.shopease.Auth.Controller;

import com.chiragbhisikar.shopease.Auth.Config.JWTTokenHelper;
import com.chiragbhisikar.shopease.Auth.Service.UserDetailService;
import com.chiragbhisikar.shopease.Auth.Service.UserService;
import com.chiragbhisikar.shopease.DTO.Auth.RegistrationRequestDTO;
import com.chiragbhisikar.shopease.DTO.Auth.RegistrationResponseDTO;
import com.chiragbhisikar.shopease.DTO.Auth.UserDetailDTO;
import com.chiragbhisikar.shopease.Exception.AlreadyExistsException;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Auth.User;
import com.chiragbhisikar.shopease.Request.LoginRequest;
import com.chiragbhisikar.shopease.Response.ApiResponse;
import com.chiragbhisikar.shopease.Response.JwtResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class AuthController {
    private final UserDetailService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTTokenHelper jwtTokenHelper;


    public AuthController(AuthenticationManager authenticationManager, UserService userService, JWTTokenHelper jwtTokenHelper, UserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Validated @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenHelper.generateToken(authentication);
            User user = (User) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(user.getId(), jwt);

            return ResponseEntity.ok(new ApiResponse("Login Success!", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequestDTO request) {
        try {
            RegistrationResponseDTO registrationResponse = userService.createUser(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User Created Successfully !", registrationResponse));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyCode(@RequestBody Map<String, String> map) {
        try {
            String userName = map.get("email");
            String code = map.get("code");
            Boolean isVerified = userService.verifyUser(userName, code);

            if (isVerified) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User Verified Successfully !", null));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Verification Code Is Incorrect !", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        if (null == user) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Token Is Expired Or Incorrect !", null));
        }

        UserDetailDTO userDetailsDto = UserDetailDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .addressList(user.getAddressList())
                .authorityList(user.getAuthorities().toArray()).build();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User Profile Gotten Successfully !", userDetailsDto));
    }
}
