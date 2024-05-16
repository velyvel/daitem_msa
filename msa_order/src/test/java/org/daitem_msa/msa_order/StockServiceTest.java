//package org.daitem_msa.msa_order;
//import org.daitem_msa.msa_order.entity.Items;
//import org.daitem_msa.msa_order.repository.ItemsRepository;
//import org.daitem_msa.msa_order.service.NewOrderService;
//import org.daitem_msa.msa_order.service.OrderService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.UUID;
//import java.util.concurrent.*;
//import java.util.function.Consumer;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//public class StockServiceTest {
//
//
//    @Autowired
//    NewOrderService newOrderService;
//    @Autowired
//    ItemsRepository itemsRepository;
//    private final Integer CONCURRENT_COUNT = 10;
//    private Long itemId = null;
//
//    @BeforeEach
//    public void setUp() {
//        System.out.println("1000개 상품 생성");
//        Items items = Items.create(1000);
//        Items saved = itemsRepository.saveAndFlush(items);
//        itemId = saved.getItemId();
//    }
//
//    @AfterEach
//    public void after() {
//        itemsRepository.deleteById(itemId);
//    }
//
//    private void test(Consumer<Void> action) throws InterruptedException {
//        int original = itemsRepository.findById(itemId).orElseThrow().getStock();
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(CONCURRENT_COUNT);
//
//        for (int i = 0; i < CONCURRENT_COUNT; i++) {
//            executorService.submit(() -> {
//                try {
//                    action.accept(null);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        Items items = itemsRepository.findById(itemId).orElseThrow();
//        assertEquals(original - CONCURRENT_COUNT, items.getStock());
//    }
//
////    @Test
////    @DisplayName("1000명, 동시에 접속")
////    public void testWithoutDistribution() throws InterruptedException {
////        test((_no) -> newOrderService.orderRequestForTest2(itemId, 1));
////    }
////
////    @Test
////    @DisplayName("1000명, 동시에 접속 with redisson")
////    public void testWithDistribution() throws InterruptedException {
////        test((_no) -> newOrderService.redissonOrderRequestForTest2(itemId, 1));
////    }
//
//}
