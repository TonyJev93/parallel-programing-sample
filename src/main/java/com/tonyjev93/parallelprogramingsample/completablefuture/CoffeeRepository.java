package com.tonyjev93.parallelprogramingsample.completablefuture;

import com.tonyjev93.parallelprogramingsample.util.ThreadUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CoffeeRepository {
    private static final int WAIT_TIME = 1000;
    private Map<String, Coffee> coffeeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        coffeeMap.put("latte", Coffee.builder().name("latte").price(1100).build());
        coffeeMap.put("mocha", Coffee.builder().name("mocha").price(1300).build());
        coffeeMap.put("americano", Coffee.builder().name("americano").price(900).build());
    }

    public int getPriceByName(String name) {
        ThreadUtils.sleep(WAIT_TIME);
        return coffeeMap.get(name).getPrice();
    }
}
