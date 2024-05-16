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

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributionAspect {

    private final RedissonClient redissonClient;
    private final TransactionAspect transactionAspect;
    @Around("@annotation(org.daitem_msa.msa_order.common.redisson.DistributionLock)&&args(targetId)")
    public Object doLock(ProceedingJoinPoint joinPoint, Long targetId) throws Throwable {

        //어노테이션 얻기
        DistributionLock annotation = getAnnotation(joinPoint);

        //락 이름이랑 요청보내기
        String lockName = getLockName(targetId, annotation);
        RLock lock = redissonClient.getLock(lockName);

        try{
            boolean isLock = lock.tryLock(annotation.waitTime(),annotation.leaseTime(), annotation.timeUnit());

            if(!isLock) {
                log.warn("Lock acquisition failed: {}", lockName);
                throw new IllegalArgumentException();
            }

            log.info("Lock acquired: {}", isLock);

            return transactionAspect.proceed(joinPoint);
        }
        finally {
            try{
                lock.unlock();
                log.info("Lock released: {}", lockName);
            }catch (IllegalMonitorStateException e) {
                log.warn("Lock released failed: {}", lockName);
            }
        }
    }

    private DistributionLock getAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(DistributionLock.class);
    }

    private String getLockName(Long targetId, DistributionLock annotation) {
        String lockNameFormat = "lock:%s:%s";
        String param = targetId.toString();
        return String.format(lockNameFormat, annotation.lockName(), param);
    }
}
