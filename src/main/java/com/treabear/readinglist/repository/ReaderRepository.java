package com.treabear.readinglist.repository;

import com.treabear.readinglist.domain.Reader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * ReaderRepository
 */
public interface ReaderRepository extends JpaRepository<Reader, String> {

}
