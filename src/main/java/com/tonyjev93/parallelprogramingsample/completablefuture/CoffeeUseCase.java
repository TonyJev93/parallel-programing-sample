package com.tonyjev93.parallelprogramingsample.completablefuture;

import java.util.concurrent.Future;

public interface CoffeeUseCase {
    int getPrice(String name);  // 동기

    Future<Integer> getPriceAsync(String name); // 비동기

    Future<Integer> getDiscountPriceAsync(Integer price); // 비동기
}
