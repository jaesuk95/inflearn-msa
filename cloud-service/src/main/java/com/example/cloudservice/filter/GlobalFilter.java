package com.example.cloudservice.filter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage: -> {}", config.getBaseMessage());


            if(config.isPreLogger()){
                log.info("Global Filter Start: request id -> {}", request.getId());
            }
            //custom Post Filter(chain)
            //비동기 방식에서 사용 -> Mono (단일값)
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
            {

                if(config.isPostLogger()){
                    log.info("Global Filter end: response code -> {}", response.getStatusCode());
                }
            }));
        };
    }

    @Data //getter setter
    public static class Config{
        //Put the configuration properties
        private String baseMessage;
        private boolean preLogger;  // boolean은 is~ 로 자동 생성
        private boolean postLogger;



    }
}
