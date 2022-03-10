package com.tonyjev93.parallelprogramingsample.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CoffeeRepository.class, CoffeeService.class})
@Slf4j
class CoffeeServiceTest {
    @Autowired
    private CoffeeService coffeeService;

    @DisplayName("가격 조회 동기 블록킹 호출 테스트")
    @Test
    public void test() throws Exception {
        // given
        String coffeeName = "latte";
        int expectedPrice = 1100;

        // when
        int resultPrice = coffeeService.getPrice(coffeeName);
        log.info("최종 가격 전달 받음");

        //then
        assertEquals(expectedPrice, resultPrice);
    }
}