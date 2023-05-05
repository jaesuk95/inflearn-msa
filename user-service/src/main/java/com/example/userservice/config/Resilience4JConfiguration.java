package com.example.userservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfiguration {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {

        // the circuitBreakerConfig and timeLimiterConfig objects
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(4)    // circuitBreaker 를 열지 결장하는 failure rate threshold percentage. default value: 50
                .waitDurationInOpenState(Duration.ofMillis(1000))   // circuitBreaker 를 open 한 상태를 유지하는 지속 기간을 의미 즉, 1초 동안 전달하지 않겠다는 뜻이다, 이 기간에 half-open 상태
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)  // circuit 다시 closed 된 상태에서 지금까지 계속 호출했던 결과값을 저장하기 위해서 count or time based
                .slidingWindowSize(2) // means that the Circuit Breaker will track the success/failure of the last two calls to the protected resource.
                .build();
        // SlidingWindowType.COUNT_BASED refer to slidingWindowSize

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4)) // 예를 들어, 연동된 마이크로서비스가 여기 설정한 시간보다 늦는다면 오류로 인식된다. 그러므로 circuitBreaker 는 open 상태로 할 수 있다. default 1 초
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }
    // slidingWindowType :
    // Configures the type of the sliding window which is used to record the outcome of calls when the CircuitBreaker is closed.
    // Sliding window can either be count-based or time-based.
    //
    // If the sliding window is COUNT_BASED, the last slidingWindowSize calls are recorded and aggregated.
    // If the sliding window is TIME_BASED, the calls of the last slidingWindowSize seconds recorded and aggregated.
}
