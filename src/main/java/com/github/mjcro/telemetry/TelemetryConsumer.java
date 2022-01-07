package com.github.mjcro.telemetry;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Represents telemetry consumer
 */
public interface TelemetryConsumer extends Consumer<Telemetry> {
    /**
     * Constructs telemetry consumer that will broadcast telemetry data to all
     * given consumers.
     *
     * @param consumers Consumers to broadcast.
     * @return Telemetry consumer.
     */
    static TelemetryConsumer broadcaster(Consumer<Telemetry>... consumers) {
        return new TelemetryConsumerBroadcaster(Arrays.asList(consumers));
    }
}
