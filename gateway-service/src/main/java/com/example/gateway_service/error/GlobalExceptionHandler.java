package com.example.gateway_service.error;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        HttpStatusCode statusCode;
        String errorMessage;

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            status = (HttpStatus) responseStatusException.getStatusCode();
            errorMessage = responseStatusException.getReason();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error";
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorResponse = String.format("{\"success\":false,\"response\":null,\"error\":{\"message\":\"%s\",\"status\":%d}}", errorMessage, status.value());
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(errorResponse.getBytes())));
    }

}
