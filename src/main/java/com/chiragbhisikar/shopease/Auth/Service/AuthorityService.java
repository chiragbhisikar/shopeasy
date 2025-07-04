package com.chiragbhisikar.shopease.Auth.Service;


import com.chiragbhisikar.shopease.Auth.Repository.AuthorityRepository;
import com.chiragbhisikar.shopease.Model.Auth.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public List<Authority> getUserAuthority() {
        List<Authority> authorities = new ArrayList<>();
        Authority authority = authorityRepository.findAuthorityByRoleCode("ROLE_USER");
        authorities.add(authority);
        return authorities;
    }

    public Authority createAuthority(String role, String description) {
        Authority authority = Authority.builder().roleCode(role).roleDescription(description).build();
        return authorityRepository.save(authority);
    }
}
