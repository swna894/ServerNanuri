package com.order2david.signin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order2david.shop.model.Roles;
import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;
import com.order2david.signin.payload.UserDto;
import com.order2david.signin.util.SecurityUtil;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Shop signup(UserDto userDto) {
        if (shopRepository.findOneWithRolesByEmail(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        //빌더 패턴의 장점
        Roles authority = Roles.builder()
                .roleName("ROLE_USER")
                .build();
        
        Shop shop = new Shop();
        shop.setPassword(passwordEncoder.encode(userDto.getPassword()));
        shop.setRoles(Collections.singleton(authority));
        
        return shopRepository.save(shop);
    }

    @Transactional(readOnly = true)
    public Optional<Shop> getUserWithAuthorities(String email) {
        return shopRepository.findOneWithRolesByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Shop> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(shopRepository::findOneWithRolesByEmail);
    }
}
