package com.github.mjcro.telemetry;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class TelemetryErrorHolderAdapter implements Telemetry {
    private final Telemetry real;
    private final Throwable error;

    /**
     * Constructs new telemetry instance with given error.
     *
     * @param telemetry Original telemetry.
     * @param error     Given error.
     * @return Telemetry with adjusted error information.
     */
    static Telemetry of(Telemetry telemetry, Throwable error) {
        return new TelemetryErrorHolderAdapter(
                telemetry instanceof TelemetryErrorHolderAdapter
                        ? ((TelemetryErrorHolderAdapter) telemetry).real
                        : telemetry,
                error
        );
    }

    private TelemetryErrorHolderAdapter(Telemetry real, Throwable error) {
        this.real = Objects.requireNonNull(real, "real");
        this.error = Objects.requireNonNull(error, "error");
    }

    @Override
    public Optional<Throwable> getError() {
        return Optional.of(error);
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
    public Map<String, Object> getMetadata() {
        return real.getMetadata();
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
