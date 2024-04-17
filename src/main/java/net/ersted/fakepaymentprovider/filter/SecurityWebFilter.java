package net.ersted.fakepaymentprovider.filter;

import lombok.RequiredArgsConstructor;
import net.ersted.fakepaymentprovider.exception.AuthException;
import net.ersted.fakepaymentprovider.service.MerchantService;
import net.ersted.fakepaymentprovider.util.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class SecurityWebFilter implements WebFilter {
    private static final String AUTHORIZATION_PREFIX = "Base64 ";
    private static final String EXCEPTION_STATUS = HttpStatus.FORBIDDEN.toString();
    private static final String EXCEPTION_MESSAGE = "TOKEN_IS_NOT_VALID";
    private static final Function<String, Mono<String>> getBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(AUTHORIZATION_PREFIX.length()));

    private final MerchantService merchantService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<Map<String, String>> credentials = HeaderUtils.extractHttpHeader(exchange, HttpHeaders.AUTHORIZATION)
                .flatMap(getBearerValue)
                .flatMap(this::getCredentialsByToken);

        return Mono
                .empty()
                .then(credentials.flatMap(map -> verify(map.get("merchantId"), map.get("secretKey"))))
                .then(credentials.flatMap(map -> {
                    exchange.getAttributes().put("merchantId", map.get("merchantId"));
                    return Mono.empty().then();
                }))
                .then(chain.filter(exchange));
    }

    private Mono<Void> verify(String merchantId, String secretKey) {
        return Mono.empty()
                .then(merchantService.getActiveMerchantByMerchantId(merchantId)
                        .filter(merchant -> secretKey.equals(merchant.getSecretKey()))
                        .switchIfEmpty(Mono.error(new AuthException(EXCEPTION_STATUS, EXCEPTION_MESSAGE))))
                .then();
    }

    private Mono<Map<String, String>> getCredentialsByToken(String token) {
        byte[] decodeToken = Base64.getDecoder().decode(token);
        String stringToken = new String(decodeToken, StandardCharsets.UTF_8);
        String[] split = stringToken.split(":");

        if (split.length != 2) {
            throw new AuthException(EXCEPTION_STATUS, EXCEPTION_MESSAGE);
        }
        String merchantId = split[0];
        String secretKey = split[1];

        return Mono.just(Map.of("merchantId", merchantId, "secretKey", secretKey));
    }
}