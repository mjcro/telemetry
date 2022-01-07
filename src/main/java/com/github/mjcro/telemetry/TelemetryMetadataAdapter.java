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

    static Telemetry of(Telemetry real, Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return real;
        }
        return real instanceof TelemetryMetadataAdapter
                ? new TelemetryMetadataAdapter(((TelemetryMetadataAdapter) real).real, metadata)
                : new TelemetryMetadataAdapter(real, metadata);
    }

    static Telemetry append(Telemetry real, Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return real;
        }

        // Combining
        HashMap<String, Object> resulting = new HashMap<>(real.getMetadata());
        resulting.putAll(metadata);

        return of(real, resulting);
    }

    private TelemetryMetadataAdapter(Telemetry real, Map<String, Object> metadata) {
        this.real = real;
        this.metadata = Collections.unmodifiableMap(metadata);
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
    public Map<String, Object> getMetadata() {
        return metadata;
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
