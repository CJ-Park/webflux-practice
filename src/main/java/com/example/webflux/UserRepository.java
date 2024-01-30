package com.example.webflux;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, String> {
    Mono<User> findByNickname(String nickname);
}
