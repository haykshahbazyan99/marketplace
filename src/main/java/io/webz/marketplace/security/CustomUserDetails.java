package io.webz.marketplace.security;

import io.webz.marketplace.models.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails  implements UserDetails {

    private final User user;

    public CustomUserDetails(User users) {
        this.user = users;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }


    @Override
    public String getUsername() {
        return this.user.getUsername();
    }


    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

}
