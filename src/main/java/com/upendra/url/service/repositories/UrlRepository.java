package com.upendra.url.service.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upendra.url.service.entities.ShortUrl;

@Repository
public interface UrlRepository extends JpaRepository<ShortUrl, Long> {

	Optional<ShortUrl> findByShortCode(String shortCode);

	boolean existsByShortCode(String shortCode);

	Page<ShortUrl> findByUser_Id(Integer userId, Pageable pageable);

}
