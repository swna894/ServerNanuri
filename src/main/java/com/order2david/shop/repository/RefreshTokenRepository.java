package com.order2david.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.order2david.shop.model.RefreshToken;
import com.order2david.shop.model.Shop;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByShop(Shop shop);
  
  void deleteByToken(String refreshToken);
}
