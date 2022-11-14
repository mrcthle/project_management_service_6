package de.szut.lf8_project.helpers;

import lombok.AllArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
public class JwtToken {
    
    private String token;
    private LocalTime tokenTime;
    private Long expireTime;
    
    public boolean isExpired() {
        return LocalTime.now().isAfter(tokenTime.plusSeconds(expireTime));
    }

    public String getToken() {
        return token;
    }
}
