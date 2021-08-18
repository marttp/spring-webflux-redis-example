package dev.tpcoder.springredisreactive.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import dev.tpcoder.springredisreactive.model.User;
import dev.tpcoder.springredisreactive.service.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private RedisService redisService;

    private User user;

    @BeforeEach
    void initTest() {
        user = new User();
        user.setId("1");
        user.setFirstName("firstName");
        user.setLastName("lastName");
    }
    @Test
    void readUser_success() {
        Mockito.when(redisService.get(anyString()))
                .thenReturn(Mono.just(user));
        StepVerifier.create(userController.readUser("1"))
                .assertNext(data -> {
                    Assertions.assertEquals(user.getId(), ((User) data).getId());
                    Assertions.assertEquals(user.getFirstName(), ((User) data).getFirstName());
                    Assertions.assertEquals(user.getLastName(), ((User) data).getLastName());
                })
                .verifyComplete();
    }

    @Test
    void saveUser_success() {
        Mockito.when(redisService.put(anyString(), any(User.class)))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(userController.saveUser("1", user))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void deleteUser_success() {
        Mockito.when(redisService.delete(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userController.deleteUser("1"))
                .verifyComplete();
    }
}
