package dev.tpcoder.springredisreactive.controller;

import dev.tpcoder.springredisreactive.model.User;
import dev.tpcoder.springredisreactive.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final RedisService redisService;

    @GetMapping("/{userId}")
    public Mono<Object> readUser(@PathVariable String userId) {
        return redisService.get(userId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Boolean> saveUser(@PathVariable String userId, @RequestBody User body) {
        return redisService.put(userId, body);
    }

    @DeleteMapping("/{userId}")
    public Mono<Void> deleteUser(@PathVariable String userId) {
        return redisService.delete(userId).then();
    }

}
