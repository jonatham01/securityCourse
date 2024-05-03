package com.jwtservice.entity;

import com.jwtservice.util.Role;
import com.jwtservice.util.RolePermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "auth-user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if(role==null) return null;

        if(role.getPermissions()==null) return null;

        List<SimpleGrantedAuthority> authorities = role.getPermissions().stream()
                .map(Enum::name)//.map(each-> each.name())
                .map(SimpleGrantedAuthority::new)////.map(each-> new SimpleGrantedAuthority(each))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
