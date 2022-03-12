package com.tonyjev93.parallelprogramingsample.completablefuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
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
    public CompletableFuture<Integer> getPriceAsync(String name) {
        log.info("비동기 호출 방식으로 가격 조회 시작");

        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            log.info("새로운 쓰레드로 작업 시작");
            Integer price = coffeeRepository.getPriceByName(name);
            log.info("새로운 쓰레드로 작업 완료");
            future.complete(price);
        }).start();

        return future;
    }

    @Override
    public CompletableFuture<Integer> getPriceSupplyAsync(String name) {
        log.info("비동기 호출 방식으로 가격 조회 시작");

        // executor 파라미터 추가 전 : Thread = ForkJoinPool.commonPool-worker-XX 사용
        // 일반적으로 commonPool 을 사용하는 방법은 바람직하지 않음.
        // executor 파라미터 추가를 통해 commonPool 을 사용하지 않고 별도의 쓰레드 풀을 사용.
        return CompletableFuture.supplyAsync(() -> {
                    log.info("supplyAsync");
                    return coffeeRepository.getPriceByName(name);
                },
                executor);
    }

    @Override
    public Future<Integer> getDiscountPriceAsync(Integer price) {
        return null;
    }
}
