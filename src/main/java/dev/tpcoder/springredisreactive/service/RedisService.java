package dev.tpcoder.springredisreactive.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

public interface RedisService {

    // CRUD on Redis
    Mono<Boolean> put(String key, Object data);

    Mono<Object> get(String key);

    <T> Mono<T> get(String key, ParameterizedTypeReference<T> type);

    <T> Mono<T> get(String key, Class<T> type);

    Mono<Object> getAndSet(String key, Object data);

    <T> Mono<T> getAndSet(String key, Object data, ParameterizedTypeReference<T> type);

    <T> Mono<T> getAndSet(String key, Object data, Class<T> type);

    Mono<Void> delete(String key);

    Mono<Void> delete(List<String> keyList);

    // Expired operation
    Mono<Duration> getExpire(String key);

    // Life-time of key
    Mono<Boolean> expire(String key, Duration timeout);

    // Specific timestamp
    Mono<Boolean> expireAt(String key, Instant instant);

    // Remove the expiration from given key
    Mono<Boolean> persist(String key);

    // Other operation
    Mono<Long> increment(String key);

    Mono<Long> increment(String key, long time);

    Mono<Long> decrement(String key);

    Mono<Long> decrement(String key, long time);

    Mono<Boolean> hasKey(String key);

    Mono<Long> size(String key);

}
