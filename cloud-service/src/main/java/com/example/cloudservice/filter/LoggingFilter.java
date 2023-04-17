package com.example.cloudservice.filter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j //for print log
@Component /**커스텀 필터의 일종임, 단지 로그를 찍기 위한 필터이므로 이름을 Logging 이라고 지은 것*/
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() { super(Config.class); }

    @Override
    public GatewayFilter apply(Config config) {
        /**
         * 위쪽 주석부분은 람다 표현식으로 바로 리턴한 것이고 이것은 람다를 사용하지 않고 리턴한 것임,
         * 따라서 인스턴스부터 만들어주어야 하고 GatewayFilter는 인터페이스이기 때문에 직접 인스턴스를 생성할 수는 없고, OrderedGatewayFilter() 를 이용하여 인스턴스를 만들어주어야함
         * */
        GatewayFilter filter = new OrderedGatewayFilter(((exchange, chain) -> { /** exchange: request와 response 객체를 얻기 위함 */
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("logging filter: request id -> {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("logging pre filter start: request id -> {}", request.getId());
                log.info("logging pre filter Requested URI = {}",request.getURI());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()){
                    //Mono 객체: 웹플럭스(스프링5)에서 지원하는 기능으로 비동기방식의 서버에서 단일값을 전달할 때 모노타입으로 전달
                    log.info("logging post filter end: response code -> {}", response.getStatusCode());
                }
            }));
        }), Ordered.LOWEST_PRECEDENCE); //HIGHEST_PRECEDENCE 는 적용할 필터가 여러개일 때 어느것이 먼저 실행될지 우선순위를 부여함
        return filter;
    }
    // setter getter 함수 생성 (isPreLogger(), isPostLogger() 등)
    @Getter @Setter
    public static class Config{
        // 여기에 configuration 이 있다면 삽입
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
