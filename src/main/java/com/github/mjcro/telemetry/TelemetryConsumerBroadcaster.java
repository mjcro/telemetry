package com.github.mjcro.telemetry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TelemetryConsumerBroadcaster implements TelemetryConsumer {
    private final ArrayList<Consumer<Telemetry>> consumers;

    TelemetryConsumerBroadcaster(Collection<? extends Consumer<Telemetry>> consumers) {
        this.consumers = consumers.stream()
                .filter(Objects::nonNull)
                .flatMap(consumer -> {
                    if (consumer instanceof TelemetryConsumerBroadcaster) {
                        // Unfolding nested broadcasters
                        return ((TelemetryConsumerBroadcaster) consumer).consumers.stream();
                    }
                    return Stream.of(consumer);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void accept(Telemetry telemetry) {
        if (telemetry != null) {
            RuntimeException outerError = null;
            for (Consumer<Telemetry> consumer : consumers) {
                try {
                    consumer.accept(telemetry);
                } catch (RuntimeException error) {
                    outerError = error;
                }
            }
            if (outerError != null) {
                throw outerError;
            }
        }
    }
}
