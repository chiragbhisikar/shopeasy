package com.chiragbhisikar.shopease.Auth.Config;

import java.util.List;

public class SecureUrl {
    public static final List<String> SECURED_URLS = List.of(
// swagger
            "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
            "/api/products",
            "/api/category",
//            auth
            "/oauth2/success",
            "/api/user/login",
            "/api/user/register",
            "/api/user/verify",
            "/oauth2/success",
//  categories
            "/api/categories",
            "/api/categories/{categoryId}",
            "/api/categories/byName"
    );

}


