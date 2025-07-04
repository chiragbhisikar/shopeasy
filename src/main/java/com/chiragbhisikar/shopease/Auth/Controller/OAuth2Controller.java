package com.chiragbhisikar.shopease.Auth.Controller;

import com.chiragbhisikar.shopease.Auth.Config.JWTTokenHelper;
import com.chiragbhisikar.shopease.Auth.Service.OAuth2Service;
import com.chiragbhisikar.shopease.Model.Auth.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/oauth2")
@CrossOrigin("http://localhost:3000/")
public class OAuth2Controller {
    @Autowired
    OAuth2Service oAuth2Service;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @GetMapping("/success")
    public void callbackOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) throws IOException {

        String userName = oAuth2User.getAttribute("email");
        User user = oAuth2Service.getUser(userName);
        if (null == user) {
            user = oAuth2Service.createUser(oAuth2User, "google");
        }

        String token = jwtTokenHelper.generateToken(user);

        response.sendRedirect("http://localhost:3000/oauth2/callback?token=" + token);
    }
}

