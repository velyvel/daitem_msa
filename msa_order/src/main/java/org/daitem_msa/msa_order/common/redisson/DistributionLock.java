package org.daitem_msa.msa_order.common.redisson;

import org.springframework.context.annotation.Description;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Description("동시성 처리를 위한 설정파일")
public @interface DistributionLock {

    String lockName();
    long waitTime() default 5L;
    long leaseTime() default 3L;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
