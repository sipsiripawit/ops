package com.yipintsoi.authservice.security.oauth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    @SuppressWarnings("unchecked")
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        // Extract username from JWT
        String username = jwt.getSubject();
        
        // Extract roles from JWT
        List<String> roles = extractRoles(jwt);
        
        // Convert roles to authorities
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        // Create user principal
        User user = new User(username, "", authorities);
        
        // Create authentication token
        return new UsernamePasswordAuthenticationToken(user, jwt, authorities);
    }
    
    @SuppressWarnings("unchecked")
    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        
        // Check if the JWT contains roles
        if (claims.containsKey("roles")) {
            return (List<String>) claims.get("roles");
        }
        
        return Collections.emptyList();
    }
}