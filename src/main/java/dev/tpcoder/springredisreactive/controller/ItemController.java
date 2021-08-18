package dev.tpcoder.springredisreactive.controller;

import dev.tpcoder.springredisreactive.model.Item;
import dev.tpcoder.springredisreactive.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final RedisService redisService;

    @GetMapping("/{itemId}")
    public Mono<Object> readItem(@PathVariable String itemId) {
        return Mono.from(redisService.get(itemId));
    }

    @PostMapping("/{itemId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Boolean> saveItemAmount(@PathVariable String itemId, @RequestBody Item body) {
        if (ObjectUtils.isEmpty(body.getAmount())) {
            body.setAmount(0L);
        }
        return redisService.put(itemId, body.getAmount());
    }

    @PutMapping("/{itemId}")
    public Mono<Long> updateItem(@PathVariable String itemId, @RequestBody Item body) {
        if (ObjectUtils.isEmpty(body.getAmount())) {
            body.setAmount(0L);
        }
        return redisService.increment(itemId, body.getAmount().intValue());
    }

    @DeleteMapping("/{itemId}")
    public Mono<Void> deleteItem(@PathVariable String itemId) {
        return redisService.delete(itemId).then();
    }
}
