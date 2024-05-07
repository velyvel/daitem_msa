package org.daitem_msa.msa_order.common.redisson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributionLock {
    // 락 설정할것 여기에 두기
    //key : 락의 이름
    String key();

    // 시간단위, MILLISECONDS, SECONDS, MINUTE..)
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    //락을 획득하기 위한 대기 시간
    long waitTime() default 5L;

    //락을 임대하는 시간
    long leaseTime() default 3L;
}