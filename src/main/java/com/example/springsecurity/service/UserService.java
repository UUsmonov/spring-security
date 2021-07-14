package com.example.springsecurity.service;

import com.example.springsecurity.helpers.Utils;
import com.example.springsecurity.model.*;
import com.example.springsecurity.repository.PermissionRepo;
import com.example.springsecurity.repository.RoleRepo;
import com.example.springsecurity.repository.UserRepo;
import com.example.springsecurity.repository.UserTokenRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service("userDetailsService")
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final UserTokenRepo userTokenRepo;

    public UserService(UserRepo userRepo,
                       RoleRepo roleRepo,
                       PermissionRepo permissionRepo,
                       UserTokenRepo userTokenRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
        this.userTokenRepo = userTokenRepo;
    }

    @PostConstruct
    public void addTestUser(){ // for test purpose
        Permission permission = new Permission(
                1L,
                "superAdmin"
        );
        permissionRepo.save(permission);
        Role role = new Role(
                1L,
                "ROLE_admin",
                List.of(permission)
        );
        role = roleRepo.save(role);
        User user = new User(
                "test",
                "test123",
                "123456789",
                true,
                true,
                true,
                true,
                List.of(role)
        );
        user = userRepo.save(user);
        UserToken userToken = new UserToken(
                "4db33ccf-b05f-484c-84ea-cff80bd72fc8-06cc8d72-1b83-4821-9c5d-e980fcf5b13e",
                user.getId()
        );
        userTokenRepo.save(userToken);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByUsername(s);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username or password wrong"));

        UserDetails userDetails = new AuthUserDetail(userOptional.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }

    public boolean validateToken(String accessToken, String verifyToken){
        return verifyToken.equals(Utils.hashSha3512WithGivenSalt(accessToken, "@D`!s*Ro-WAkD:mr@+3%T$"));
    }

    public UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        Optional<UserToken> tokenOptional = userTokenRepo.findByToken(token);
        tokenOptional.orElseThrow(() -> new UsernameNotFoundException("Invalid token"));

        UserDetails userDetails = new AuthUserDetail(tokenOptional.get().getUser());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }

    public ResponseEntity<?> getAll() {
        return null;
    }
}
