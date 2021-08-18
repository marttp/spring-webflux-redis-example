package dev.tpcoder.springredisreactive.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import dev.tpcoder.springredisreactive.model.Item;
import dev.tpcoder.springredisreactive.model.User;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RedisServiceTest {

    @InjectMocks
    private RedisServiceImpl redisService;

    @Mock
    private ReactiveRedisOperations<String, Object> reactiveRedisOperations;

    @Mock
    private ReactiveValueOperations<String, Object> reactiveValueOperations;

    private User user;
    private Item item;

    @BeforeEach
    void initTest() {
        user = new User();
        user.setId("user1").setFirstName("firstName").setLastName("lastName");
        item = new Item();
        item.setId("item1").setName("food").setAmount(0L);
        Mockito.when(reactiveRedisOperations.opsForValue())
                .thenReturn(reactiveValueOperations);
    }

    @Test
    void getByKey_shouldReturnExpectedResult() {
        Mockito.when(reactiveRedisOperations.keys(anyString()))
                .thenReturn(Flux.just("user1"));
        Mockito.when(reactiveRedisOperations.opsForValue().get(anyString()))
                .thenReturn(Mono.just(user));
        StepVerifier.create(redisService.get("user1"))
                .expectSubscription()
                .assertNext(result -> {
                    User data = (User) result;
                    Assertions.assertEquals("firstName", data.getFirstName());
                    Assertions.assertEquals("lastName", data.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void getByKey_shouldReturnClassExpectedResult() {
        Mockito.when(reactiveRedisOperations.keys(anyString()))
                .thenReturn(Flux.just("user1"));
        Mockito.when(reactiveRedisOperations.opsForValue().get(anyString()))
                .thenReturn(Mono.just(user));
        StepVerifier.create(redisService.get("user1", User.class))
                .expectSubscription()
                .assertNext(data -> {
                    Assertions.assertEquals("firstName", data.getFirstName());
                    Assertions.assertEquals("lastName", data.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void getByKey_shouldReturnItemClassResult() {
        Mockito.when(reactiveRedisOperations.keys(anyString()))
                .thenReturn(Flux.just("item1"));
        Mockito.when(reactiveRedisOperations.opsForValue().get(anyString()))
                .thenReturn(Mono.just(item));
        StepVerifier.create(redisService.get("item1", Item.class))
                .expectSubscription()
                .assertNext(data -> {
                    Assertions.assertEquals("item1", data.getId());
                    Assertions.assertEquals("food", data.getName());
                    Assertions.assertEquals(0L, data.getAmount());
                })
                .verifyComplete();
    }

    @Test
    void getByKey_shouldReturnExpectedListResult() {
        Mockito.when(reactiveRedisOperations.keys(anyString()))
                .thenReturn(Flux.just("users"));
        Mockito.when(reactiveRedisOperations.opsForValue().get(anyString()))
                .thenReturn(Mono.just(List.of(user, user)));
        StepVerifier.create(redisService.get("users", new ParameterizedTypeReference<List<User>>() {
                }))
                .expectSubscription()
                .assertNext(data -> {
                    Assertions.assertEquals(2, data.size());
                })
                .verifyComplete();
    }

    @Test
    void getAndSet_shouldReturnExpectedResult() {
        Mockito.when(reactiveRedisOperations.keys(anyString()))
                .thenReturn(Flux.just("user1"));
        Mockito.when(reactiveRedisOperations.opsForValue().getAndSet(anyString(), any()))
                .thenReturn(Mono.just(user));
        User updatedUser = new User();
        updatedUser.setId("user1")
                .setFirstName("firstName")
                .setLastName("lastName");
        StepVerifier.create(redisService.getAndSet("user1", updatedUser))
                .expectSubscription()
                .assertNext(result -> {
                    User data = (User) result;
                    Assertions.assertEquals("firstName", data.getFirstName());
                    Assertions.assertEquals("lastName", data.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void getAndSet_whenExpectedClass_shouldReturnExpectedClassResult() {
        Mockito.when(reactiveRedisOperations.keys(anyString()))
                .thenReturn(Flux.just("user1"));
        Mockito.when(reactiveRedisOperations.opsForValue().getAndSet(anyString(), any()))
                .thenReturn(Mono.just(user));
        User updatedUser = new User();
        updatedUser.setId("user1")
                .setFirstName("firstName")
                .setLastName("lastName");
        StepVerifier.create(redisService.getAndSet("user1", updatedUser, User.class))
                .expectSubscription()
                .assertNext(data -> {
                    Assertions.assertEquals("firstName", data.getFirstName());
                    Assertions.assertEquals("lastName", data.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void getAndSet_whenExpectedClassList_shouldReturnExpectedListResult() {
        User updatedUser = new User();
        updatedUser.setId("user1")
                .setFirstName("firstName")
                .setLastName("lastName");
        List<User> users = List.of(updatedUser);
        Mockito.when(reactiveRedisOperations.keys(anyString()))
                .thenReturn(Flux.just("1"));
        Mockito.when(reactiveRedisOperations.opsForValue().getAndSet(anyString(), any()))
                .thenReturn(Mono.just(users));
        StepVerifier.create(redisService.getAndSet("1", users, new ParameterizedTypeReference<List<User>>() {
                }))
                .expectSubscription()
                .assertNext(data -> {
                    Assertions.assertEquals(1, data.size());
                })
                .verifyComplete();
    }

    @Test
    void givenPutOperation_whenPutData_shouldReturnSuccess() {
        Mockito.when(reactiveRedisOperations.opsForValue().set(anyString(), any()))
                .thenReturn(Mono.just(true));
        StepVerifier.create(redisService.put(user.getId(), user))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void givenDeleteOperation_whenGivenKey_shouldSuccess() {
        Mockito.when(reactiveRedisOperations.delete(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(redisService.delete(user.getId()))
                .verifyComplete();
    }

    @Test
    void givenDeleteOperation_whenGivenKeyList_shouldSuccess() {
        Mockito.when(reactiveRedisOperations.delete(any(Flux.class)))
                .thenReturn(Mono.just(2L));
        StepVerifier.create(redisService.delete(List.of(user.getId(), user.getId())))
                .verifyComplete();
    }

    @Test
    void givenCheckKeyOperation_whenGivenKey_shouldReturnTrue() {
        Mockito.when(reactiveRedisOperations.hasKey(anyString()))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(redisService.hasKey("1"))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void givenCheckSizeOperation_whenGivenKey_shouldReturnSuccess() {
        Mockito.when(reactiveRedisOperations.opsForValue().size(anyString()))
                .thenReturn(Mono.just(1L));
        StepVerifier.create(redisService.size("1"))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void checkExpireOperation_whenGivenKey_shouldReturnDuration() {
        Duration oneHours = Duration.ofHours(1);
        Mockito.when(reactiveRedisOperations.getExpire(anyString()))
                .thenReturn(Mono.just(oneHours));
        StepVerifier.create(redisService.getExpire("1"))
                .assertNext(duration -> {
                    Assertions.assertEquals(Duration.ofHours(1).getSeconds(), duration.getSeconds());
                })
                .verifyComplete();
    }

    @Test
    void setExpireOperation_whenGivenKeyAndExpireTime_shouldReturnTrue() {
        Duration oneHours = Duration.ofHours(1);
        Mockito.when(reactiveRedisOperations.expire(anyString(), any(Duration.class)))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(redisService.expire("1", oneHours))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void setExpireAtOperation_whenGivenKeyAndInstant_shouldReturnTrue() {
        Instant inst = Instant.parse("2020-08-01T10:37:30.00Z");
        Mockito.when(reactiveRedisOperations.expireAt(anyString(), any(Instant.class)))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(redisService.expireAt("1", inst))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void givenPersistOperation_whenGivenKey_shouldReturnTrue() {
        Mockito.when(reactiveRedisOperations.persist(anyString()))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(redisService.persist("1"))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void givenIncrementOperation_whenGivenKey_shouldReturnSuccess() {
        Mockito.when(reactiveRedisOperations.opsForValue().increment(anyString()))
                .thenReturn(Mono.just(1L));
        StepVerifier.create(redisService.increment("1"))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void givenIncrementOperation_whenGivenKeyWithAmount_shouldReturnSuccess() {
        Mockito.when(reactiveRedisOperations.opsForValue().increment(anyString(), anyLong()))
                .thenReturn(Mono.just(5L));
        StepVerifier.create(redisService.increment("1", 4L))
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    void givenDecrementOperation_whenGivenKey_shouldReturnSuccess() {
        Mockito.when(reactiveRedisOperations.opsForValue().decrement(anyString()))
                .thenReturn(Mono.just(0L));
        StepVerifier.create(redisService.decrement("1"))
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void givenDecrementOperation_whenGivenKeyWithAmount_shouldReturnSuccess() {
        Mockito.when(reactiveRedisOperations.opsForValue().decrement(anyString(), anyLong()))
                .thenReturn(Mono.just(2L));
        StepVerifier.create(redisService.decrement("1", 2L))
                .expectNext(2L)
                .verifyComplete();
    }
}
