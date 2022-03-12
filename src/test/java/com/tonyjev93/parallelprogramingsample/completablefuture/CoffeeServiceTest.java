package com.tonyjev93.parallelprogramingsample.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CoffeeRepository.class, CoffeeService.class, TaskConfig.class})
@Slf4j
class CoffeeServiceTest {
    public static final String COFFEE_NAME = "latte";

    Executor executor = Executors.newFixedThreadPool(10);

    @Autowired
    private CoffeeService coffeeService;

    @DisplayName("가격 조회 : 동기, 블록킹 호출 테스트")
    @Test
    public void test() throws Exception {
        // given
        int expectedPrice = 1100;

        // when
        int resultPrice = coffeeService.getPrice(COFFEE_NAME);
        log.info("최종 가격 전달 받음");

        //then
        assertEquals(expectedPrice, resultPrice);
    }

    @DisplayName("가격 조회 : 비동기, 블록킹 호출 테스트")
    @Test
    public void test2() throws Exception {
        // given
        int expectedPrice = 1100;

        // when
        CompletableFuture<Integer> future = coffeeService.getPriceAsync(COFFEE_NAME);
        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능");
        int resultPrice = future.join(); // 블록킹
        log.info("최종 가격 전달 받음");

        //then
        assertEquals(expectedPrice, resultPrice);

    }

    @DisplayName("가격 조회 : 비동기, 블록킹 호출 테스트(supplyAsync)")
    @Test
    public void test3() throws Exception {
        // given
        int expectedPrice = 1100;

        // when
        CompletableFuture<Integer> future = coffeeService.getPriceSupplyAsync(COFFEE_NAME);

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능");
        int resultPrice = future.join(); // 블록킹
        log.info("최종 가격 전달 받음");

        //then
        assertEquals(expectedPrice, resultPrice);

    }

    @DisplayName("가격 조회 : 비동기 호출, 콜백 반환없음 테스트")
    @Test
    public void test4() throws Exception {
        // given
        Integer expectedPrice = 1100;

        // when
        // thenAccept : callback 함수(non-blocking 적용), CompletableFuture<Void> 반환
        // CompletableFuture 에서 콜백함수를 실행하기 때문에 join, get 을 통해 최종 연산된 데이터 조회할 필요가 없음
        CompletableFuture<Void> future = coffeeService.getPriceSupplyAsync(COFFEE_NAME)
                .thenAccept(p -> {
                    log.info("콜백, 가격은 " + p + "원, 하지만 데이터를 반환하지는 않음");

                    // then
                    assertEquals(expectedPrice, p);
                });

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능, 논블록킹");

        // then
        assertNull(future.join()); // 결과를 기다리기 위해 블록킹 수행 (안하면 결과를 못기다리고 테스트가 종료 됨)
    }

    @DisplayName("가격 조회 : 비동기 호출, 콜백 반환 테스트")
    @Test
    public void test5() throws Exception {
        // given
        int additionalPrice = 100;
        Integer expectedPrice = 1100 + additionalPrice;

        // when
        CompletableFuture<Void> future = coffeeService.getPriceSupplyAsync(COFFEE_NAME)
                .thenApply(p -> {
                    log.info("같은 쓰레드로 동작");
                    return p + 100;
                })
                .thenAccept(p -> {
                    log.info("콜백, 가격은 " + p + "원, 하지만 데이터를 반환하지는 않음");

                    // then
                    assertEquals(expectedPrice, p);
                });

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능, 논블록킹");

        // then
        assertNull(future.join()); // 결과를 기다리기 위해 블록킹 수행 (안하면 결과를 못기다리고 테스트가 종료 됨)
    }

    @DisplayName("가격 조회 : 비동기 호출, 콜백 반환 테스트(다른 쓰레드)")
    @Test
    public void test6() throws Exception {
        // given
        int additionalPrice = 100;
        Integer expectedPrice = 1100 + additionalPrice;

        // when
        // + Async : 다른 쓰레드에서 동작
        CompletableFuture<Void> future = coffeeService.getPriceSupplyAsync(COFFEE_NAME)
                .thenApplyAsync(p -> {
                    log.info("다른 쓰레드로 동작");
                    return p + 100;
                }, executor)
                .thenAcceptAsync(p -> {
                    log.info("콜백, 가격은 " + p + "원, 하지만 데이터를 반환하지는 않음");

                    // then
                    assertEquals(expectedPrice, p);
                }, executor);

        log.info("아직 최종 데이터를 전달 받지는 않았지만, 다른 작업 수행 가능, 논블록킹");

        // then
        assertNull(future.join()); // 결과를 기다리기 위해 블록킹 수행 (안하면 결과를 못기다리고 테스트가 종료 됨)
    }
}