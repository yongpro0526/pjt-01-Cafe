package com.miniproject.cafe.Service;

import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class CustomAdminUser extends User {

    private final String storeName;

    public CustomAdminUser(String username,
                           String password,
                           String storeName,
                           Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }
}
