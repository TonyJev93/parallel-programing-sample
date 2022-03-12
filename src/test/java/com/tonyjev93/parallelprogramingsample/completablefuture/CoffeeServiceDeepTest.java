package com.tonyjev93.parallelprogramingsample.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.tonyjev93.parallelprogramingsample.completablefuture.CoffeeRepository.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CoffeeRepository.class, CoffeeService.class, TaskConfig.class})
@Slf4j
class CoffeeServiceDeepTest {
    @Autowired
    private CoffeeService coffeeService;

    @DisplayName("(중급)가격 조회 : 병렬 조회 및 조합(combine - 순차적 실행 X)")
    @Test
    public void test1() throws Exception {
        // given
        Integer lattePrice = PRICE_OF_LATTE;
        Integer mochaPrice = PRICE_OF_MOCHA;
        Integer expectedPrice = lattePrice + mochaPrice;

        // when
        CompletableFuture<Integer> futureA = coffeeService.getPriceSupplyAsync(COFFE_NAME_OF_LATTE);
        CompletableFuture<Integer> futureB = coffeeService.getPriceSupplyAsync(COFFE_NAME_OF_MOCHA);

        // thenCombine : 병렬 실행을 통한 조합, 순차적 실행 X & 2개의 CompletableFuture 조합(3개 이상은 allOf 사용)
        Integer resultPrice = futureA.thenCombine(futureB, Integer::sum).join();

        // then
        assertEquals(expectedPrice, resultPrice);
    }


    @DisplayName("(중급)가격 조회 : 병렬 조회 및 조합(compose - 순차적 실행 O)")
    @Test
    public void test2() throws Exception {
        // given
        Integer lattePrice = PRICE_OF_LATTE;
        double discountRate = 0.9;
        Integer expectedPrice = (int) (lattePrice * discountRate);

        // when
        CompletableFuture<Integer> futureA = coffeeService.getPriceSupplyAsync(COFFE_NAME_OF_LATTE);

        // thenCompose : 병렬 실행을 통한 조합, 순차적 실행 O
        Integer resultPrice = futureA.thenCompose(result -> coffeeService.getDiscountPriceAsync(result)).join();

        // then
        assertEquals(expectedPrice, resultPrice);
    }

    @DisplayName("(중급)가격 조회 : 병렬 조회 및 조합(allOf - 3개 이상의 CompletableFuture 조합)")
    @Test
    public void test3() throws Exception {
        // given
        Integer lattePrice = PRICE_OF_LATTE;
        Integer mochaPrice = PRICE_OF_MOCHA;
        Integer americanoPrice = PRICE_OF_AMERICANO;

        Integer expectedPrice = lattePrice + mochaPrice + americanoPrice;


        // when
        CompletableFuture<Integer> futureA = coffeeService.getPriceSupplyAsync(COFFE_NAME_OF_LATTE);
        CompletableFuture<Integer> futureB = coffeeService.getPriceSupplyAsync(COFFE_NAME_OF_MOCHA);
        CompletableFuture<Integer> futureC = coffeeService.getPriceSupplyAsync(COFFE_NAME_OF_AMERICANO);

        List<CompletableFuture<Integer>> completableFutureList = Arrays.asList(futureA, futureB, futureC);

        // allOf : 병렬 실행을 통한 조합, 순차적 실행 X & 3개 이상의 CompletableFuture 조합, Future 가 모두 완료되면 CompletableFuture<Void> 를 반환 ( <-> anyOf : Future 중 하나라도 완료되면 수행 )
        Integer resultPrice = CompletableFuture.allOf(futureA, futureB, futureC)
                // thenApply : allOf 로 부터 받은 CompletableFuture<Void> 를 사용하지는 않지만, 무언가 데이터를 반환해야 함. 여기서 반환되어야 하는 값은 각 Future 로 부터 받은 커피 가격의 목록이다. (return CompletableFuture<List<Integer>>)
                .thenApply(Void -> completableFutureList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                // join : thenApply 의 응답으로 받은 CompletableFuture<List<Integer>> 을 Get 하기 위해 사용
                .join()
                .stream().reduce(0, Integer::sum); // reduce : stream 을 순차적으로 누적하여 연산

        // then
        assertEquals(expectedPrice, resultPrice);
    }


}