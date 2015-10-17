package com.dawaaii.service.dao.jpa;

import com.dawaaii.service.auth.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 *
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

	AccessToken findByUserIdAndExpiresOnGreaterThan(Long userId, Date expiresOn);

	AccessToken findByToken(String token);

}
