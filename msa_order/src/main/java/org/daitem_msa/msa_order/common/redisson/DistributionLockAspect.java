package org.daitem_msa.msa_order.common.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributionLockAspect {

    private static final String REDISSON_KEY_PREFIX = "LOCK";
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(org.daitem_msa.msa_order.common.redisson.DistributionLock)")
    public void lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributionLock distributionLock = method.getAnnotation(DistributionLock.class);
        String key = REDISSON_KEY_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributionLock.key());
        RLock lock = redissonClient.getLock(key);  // (1)

        try {
            boolean available = lock.tryLock(distributionLock.waitTime(), distributionLock.leaseTime(), distributionLock.timeUnit());  // (2)
            if (!available) {
                log.info("Lock 획득을 못했어요" , key);
                return;
            }
            log.info("락 걸리고 로직 시작합니다");
            aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            log.info("에러" + e.getMessage());
            throw e;
        } finally {
            log.info("락 해제");
            lock.unlock();
        }
    }
}
