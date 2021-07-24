package com.order2david.shop.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order2david.exception.TokenRefreshException;
import com.order2david.shop.model.RefreshToken;
import com.order2david.shop.repository.RefreshTokenRepository;
import com.order2david.shop.repository.ShopRepository;



@Service
public class RefreshTokenService {
  @Value("${jwt.refresh-validity-in-seconds}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private ShopRepository shopRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long shopId) {
    List<RefreshToken> refreshOption = refreshTokenRepository.findByShopId(shopId);  
    if(refreshOption.size() > 0) {
    	refreshTokenRepository.deleteByShopId(shopId);
    }
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setShop(shopRepository.findById(shopId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByshopId(Long shopId) {
    return refreshTokenRepository.deleteByShop(shopRepository.findById(shopId).get());
  }

  @Transactional
  public void deleteByToken(String refreshToken) {
	refreshTokenRepository.deleteByToken(refreshToken);
	
  }
}
