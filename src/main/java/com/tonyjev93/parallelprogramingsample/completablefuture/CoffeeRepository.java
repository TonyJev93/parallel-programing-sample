package com.tonyjev93.parallelprogramingsample.completablefuture;

import com.tonyjev93.parallelprogramingsample.util.ThreadUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CoffeeRepository {
    public static final int PRICE_OF_LATTE = 1100;
    public static final int PRICE_OF_MOCHA = 1300;
    public static final int PRICE_OF_AMERICANO = 900;
    public static final String COFFE_NAME_OF_LATTE = "latte";
    public static final String COFFE_NAME_OF_MOCHA = "mocha";
    public static final String COFFE_NAME_OF_AMERICANO = "americano";

    private static final int WAIT_TIME = 1000;

    private Map<String, Coffee> coffeeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        coffeeMap.put(COFFE_NAME_OF_LATTE, Coffee.builder().name(COFFE_NAME_OF_LATTE).price(PRICE_OF_LATTE).build());
        coffeeMap.put(COFFE_NAME_OF_MOCHA, Coffee.builder().name(COFFE_NAME_OF_MOCHA).price(PRICE_OF_MOCHA).build());
        coffeeMap.put(COFFE_NAME_OF_AMERICANO, Coffee.builder().name(COFFE_NAME_OF_AMERICANO).price(PRICE_OF_AMERICANO).build());
    }

    public int getPriceByName(String name) {
        ThreadUtils.sleep(WAIT_TIME);
        return coffeeMap.get(name).getPrice();
    }
}
