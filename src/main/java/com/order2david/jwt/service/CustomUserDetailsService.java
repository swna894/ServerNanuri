package com.order2david.jwt.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.order2david.shop.model.Shop;
import com.order2david.shop.repository.ShopRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
   private final ShopRepository shopRepository;

   public CustomUserDetailsService(ShopRepository shopRepository) {
      this.shopRepository = shopRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) {
      return shopRepository.findOneWithRolesByEmail(email)
         .map(shop -> createUser(email, shop))
         .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(String email, Shop shop) {
     // if (!user.isActivated()) {
     //   throw new RuntimeException(email + " -> 활성화되어 있지 않습니다.");
     // }
      List<GrantedAuthority> grantedAuthorities = shop.getRoles().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getRoleName()))
              .collect(Collectors.toList());
      return new org.springframework.security.core.userdetails.User(shop.getEmail(),
              shop.getPassword(),
              grantedAuthorities);
   }
}
