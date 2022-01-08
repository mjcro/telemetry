package com.github.mjcro.telemetry;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class TelemetryMetadataAdapter implements Telemetry {
    private final Telemetry real;
    private final Map<String, Object> metadata;

    /**
     * Constructs new telemetry with metadata replaced.
     *
     * @param original Original telemetry.
     * @param metadata Given metadata.
     * @return New telemetry with metadata adjusted.
     */
    static Telemetry of(Telemetry original, Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return original;
        }
        return original instanceof TelemetryMetadataAdapter
                ? new TelemetryMetadataAdapter(((TelemetryMetadataAdapter) original).real, metadata)
                : new TelemetryMetadataAdapter(original, metadata);
    }

    /**
     * Constructs new telemetry with metadata appended.
     *
     * @param original Original telemetry.
     * @param metadata Given metadata.
     * @return New telemetry with metadata adjusted.
     */
    static Telemetry append(Telemetry original, Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return original;
        }

        // Combining
        HashMap<String, Object> resulting = new HashMap<>(original.getMetadata());
        resulting.putAll(metadata);

        return of(original, resulting);
    }

    private TelemetryMetadataAdapter(Telemetry real, Map<String, Object> metadata) {
        this.real = real;
        this.metadata = Collections.unmodifiableMap(metadata);
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public String getRoute() {
        return real.getRoute();
    }

    @Override
    public Instant getCreatedAt() {
        return real.getCreatedAt();
    }

    @Override
    public Duration getDuration() {
        return real.getDuration();
    }

    @Override
    public Optional<Throwable> getError() {
        return real.getError();
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return real.getRequestHeaders();
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return real.getResponseHeaders();
    }

    @Override
    public InputStream getRequest() {
        return real.getRequest();
    }

    @Override
    public InputStream getResponse() {
        return real.getResponse();
    }

    @Override
    public Optional<Integer> getResponseCode() {
        return real.getResponseCode();
    }
}
