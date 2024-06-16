package com.example.gateway_service.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final WebClient webClient;
    private final String errorApiUrl;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        String errorMessage;

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            status = (HttpStatus) responseStatusException.getStatusCode();
            errorMessage = responseStatusException.getReason();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = "Internal Server Error";
        }

        // 에러 정보를 공통 API로 포워딩
        return webClient.post()
                .uri(errorApiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ErrorDetails(status.value(), errorMessage))
                .retrieve()
                .bodyToMono(Void.class)
                .then(Mono.defer(() -> {
                    exchange.getResponse().setStatusCode(status);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    String errorResponse = String.format("{\"success\":false,\"response\":null,\"error\":{\"message\":\"Unauthorized\",\"status\":401}}", errorMessage);
                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory().wrap(errorResponse.getBytes())));
                }));
    }
    private static class ErrorDetails {
        private final int status;
        private final String message;

        public ErrorDetails(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
