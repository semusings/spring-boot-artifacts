package io.github.bhuwanupadhyay.tx.outbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxEvent;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
class OutboxUtils {

    public static Optional<String> toJsonString(ObjectMapper objectMapper, OutboxEvent event) {
        try {
            return event == null ? Optional.empty() : Optional.of(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

}
