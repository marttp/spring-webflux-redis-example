package dev.tpcoder.springredisreactive.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import dev.tpcoder.springredisreactive.model.Item;
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
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private RedisService redisService;

    private Item item;

    @BeforeEach
    void initTest() {
        item = new Item();
        item.setId("1");
        item.setName("Bottle");
        item.setAmount(10L);
    }

    @Test
    void readItem_success() {
        Mockito.when(redisService.get(anyString()))
                .thenReturn(Mono.just(item));
        StepVerifier.create(itemController.readItem("1"))
                .assertNext(data -> {
                    Assertions.assertEquals(item.getId(), ((Item) data).getId());
                    Assertions.assertEquals(item.getName(), ((Item) data).getName());
                    Assertions.assertEquals(item.getAmount(), ((Item) data).getAmount());
                })
                .verifyComplete();
    }

    @Test
    void saveItemAmount_success() {
        Mockito.when(redisService.put(anyString(), anyLong()))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(itemController.saveItemAmount("1", item))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void saveItemAmount_withNullAmount_success() {
        Mockito.when(redisService.put(anyString(), anyLong()))
                .thenReturn(Mono.just(Boolean.TRUE));
        item.setAmount(null);
        StepVerifier.create(itemController.saveItemAmount("1", item))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void updateItem_success() {
        Mockito.when(redisService.increment(anyString(), anyLong()))
                .thenReturn(Mono.just(10L));
        StepVerifier.create(itemController.updateItem("1", item))
                .expectNext(10L)
                .verifyComplete();
    }

    @Test
    void updateItem_withNullAmount_success() {
        Mockito.when(redisService.increment(anyString(), anyLong()))
                .thenReturn(Mono.just(10L));
        item.setAmount(null);
        StepVerifier.create(itemController.updateItem("1", item))
                .expectNext(10L)
                .verifyComplete();
    }

    @Test
    void deleteItem_success() {
        Mockito.when(redisService.delete(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemController.deleteItem("1"))
                .verifyComplete();
    }
}
