package net.ersted.fakepaymentprovider.util;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class HeaderUtils {

    public static Mono<String> extractHttpHeader(ServerWebExchange exchange, String header) {
        return Mono.justOrEmpty(exchange.getRequest()
                .getHeaders()
                .getFirst(header));
    }
}
