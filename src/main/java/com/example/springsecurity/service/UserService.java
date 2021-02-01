package com.example.springsecurity.service;

import com.example.springsecurity.model.AuthUserDetail;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.PermissionRepo;
import com.example.springsecurity.repository.RoleRepo;
import com.example.springsecurity.repository.UserRepo;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userDetailsService")
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, PermissionRepo permissionRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByUsername(s);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username or password wrong"));

        UserDetails userDetails = new AuthUserDetail(userOptional.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }
}
