package com.github.mjcro.telemetry;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class TelemetryByteArray implements Telemetry {
    private final String route;
    private final Instant createdAt;
    private final Duration duration;
    private final Throwable error;
    private final Map<String, Object> metadata;
    private final Map<String, String> requestHeaders;
    private final Map<String, String> responseHeaders;
    private final Integer responseCode;
    private final byte[] request;
    private final byte[] response;

    TelemetryByteArray(
            String route,
            Instant createdAt,
            Duration duration,
            Throwable error,
            Map<String, Object> metadata,
            Map<String, String> requestHeaders,
            Map<String, String> responseHeaders,
            Integer responseCode,
            byte[] request,
            byte[] response
    ) {
        this.route = Objects.requireNonNull(route, "route");
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.duration = duration == null ? Duration.ZERO : duration;
        this.error = error; // Nullable
        this.metadata = metadata == null || metadata.isEmpty()
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(metadata);
        this.requestHeaders = requestHeaders == null || requestHeaders.isEmpty()
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(requestHeaders);
        this.responseHeaders = responseHeaders == null || responseHeaders.isEmpty()
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(responseHeaders);
        this.responseCode = responseCode; // Nullable
        this.request = request; // Nullable
        this.response = response; // Nullable
    }

    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public Optional<Throwable> getError() {
        return Optional.ofNullable(error);
    }

    @Override
    public InputStream getRequest() {
        return InputStreamUtil.toStream(request);
    }

    @Override
    public InputStream getResponse() {
        return InputStreamUtil.toStream(response);
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public Optional<Integer> getResponseCode() {
        return Optional.ofNullable(responseCode);
    }
}
