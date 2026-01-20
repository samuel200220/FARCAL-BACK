package com.example.Farcal_Back.repository;

import com.example.Farcal_Back.model.Contribution;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ContributionRepository
        extends ReactiveCrudRepository<Contribution, UUID> {

    @Query("""
        SELECT * FROM contributions
        WHERE user_id = :userId
        ORDER BY timestamp DESC
    """)
    Flux<Contribution> findByUserId(UUID userId);
}
