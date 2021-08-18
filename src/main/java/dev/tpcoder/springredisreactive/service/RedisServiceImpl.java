package dev.tpcoder.springredisreactive.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final ReactiveRedisOperations<String, Object> redisOperations;

    @SuppressWarnings("unchecked")
    @Override
    public <T> Mono<T> get(String key, ParameterizedTypeReference<T> type) {
        Flux<String> keys = redisOperations.keys(key);
        return Mono.from(keys.flatMap(redisOperations.opsForValue()::get)
                .flatMap(d -> Mono.just((T) d)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Mono<T> get(String key, Class<T> type) {
        Flux<String> keys = redisOperations.keys(key);
        return Mono.from(keys.flatMap(redisOperations.opsForValue()::get)
                .flatMap(d -> Mono.just((T) d)));
    }

    @Override
    public Mono<Object> get(String key) {
        Flux<String> keys = redisOperations.keys(key);
        return Mono.from(keys.flatMap(redisOperations.opsForValue()::get));
    }

    @Override
    public Mono<Object> getAndSet(String key, Object data) {
        Flux<String> keys = redisOperations.keys(key);
        return Mono.from(keys.flatMap(k -> redisOperations.opsForValue().getAndSet(k, data)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Mono<T> getAndSet(String key, Object data, ParameterizedTypeReference<T> type) {
        Flux<String> keys = redisOperations.keys(key);
        return Mono.from(keys.flatMap(k -> redisOperations.opsForValue().getAndSet(k, data)))
                .flatMap(d -> Mono.just((T) d));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Mono<T> getAndSet(String key, Object data, Class<T> type) {
        Flux<String> keys = redisOperations.keys(key);
        return Mono.from(keys.flatMap(k -> redisOperations.opsForValue().getAndSet(k, data)))
                .flatMap(d -> Mono.just((T) d));
    }

    @Override
    public Mono<Boolean> put(String key, Object data) {
        return redisOperations.opsForValue().set(key, data);
    }

    @Override
    public Mono<Void> delete(String key) {
        return redisOperations.delete(key).then();
    }

    @Override
    public Mono<Void> delete(List<String> keyList) {
        return redisOperations.delete(Flux.fromIterable(keyList)).then();
    }

    @Override
    public Mono<Boolean> hasKey(String key) {
        return redisOperations.hasKey(key);
    }

    @Override
    public Mono<Long> size(String key) {
        return redisOperations.opsForValue().size(key);
    }

    @Override
    public Mono<Duration> getExpire(String key) {
        return redisOperations.getExpire(key);
    }

    @Override
    public Mono<Boolean> expire(String key, Duration timeout) {
        return redisOperations.expire(key, timeout);
    }

    @Override
    public Mono<Boolean> expireAt(String key, Instant instant) {
        return redisOperations.expireAt(key, instant);
    }

    @Override
    public Mono<Boolean> persist(String key) {
        return redisOperations.persist(key);
    }

    @Override
    public Mono<Long> increment(String key) {
        return redisOperations.opsForValue().increment(key);
    }

    @Override
    public Mono<Long> increment(String key, long time) {
        return redisOperations.opsForValue().increment(key, time);
    }

    @Override
    public Mono<Long> decrement(String key) {
        return redisOperations.opsForValue().decrement(key);
    }

    @Override
    public Mono<Long> decrement(String key, long time) {
        return redisOperations.opsForValue().decrement(key, time);
    }
}
