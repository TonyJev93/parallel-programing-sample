package com.tonyjev93.parallelprogramingsample.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CoffeeRepository.class, CoffeeService.class, TaskConfig.class})
@Slf4j
class CoffeeServiceDeepTest {
    Executor executor = Executors.newFixedThreadPool(10);

    @Autowired
    private CoffeeService coffeeService;

    @DisplayName("(중급)가격 조회 : 병렬 조회 및 조합(combine - 순차적 실행 X)")
    @Test
    public void test1() throws Exception {
        // given
        Integer lattePrice = 1100;
        Integer mochaPrice = 1300;
        Integer expectedPrice = lattePrice + mochaPrice;

        // when
        CompletableFuture<Integer> futureA = coffeeService.getPriceSupplyAsync("latte");
        CompletableFuture<Integer> futureB = coffeeService.getPriceSupplyAsync("mocha");

        // thenCombine : 병렬 실행을 통한 조합, 순차적 실행 X
        Integer resultPrice = futureA.thenCombine(futureB, Integer::sum).join();

        // then
        assertEquals(expectedPrice, resultPrice);
    }
}