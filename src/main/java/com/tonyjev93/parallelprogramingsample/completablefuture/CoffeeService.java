package com.tonyjev93.parallelprogramingsample.completablefuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoffeeService implements CoffeeUseCase {

    private final CoffeeRepository coffeeRepository;

    Executor executor = Executors.newFixedThreadPool(10);

    @Override
    public int getPrice(String name) {
        log.info("동기 호출 방식으로 가격 조회 시작");
        return coffeeRepository.getPriceByName(name);
    }

    @Override
    public Future<Integer> getPriceAsync(String name) {
        return null;
    }

    @Override
    public Future<Integer> getDiscountPriceAsync(Integer price) {
        return null;
    }
}
