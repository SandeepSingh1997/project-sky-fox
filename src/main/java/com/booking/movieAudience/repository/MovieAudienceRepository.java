package com.booking.movieAudience.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieAudienceRepository extends JpaRepository<MovieAudience, Long> {
}
