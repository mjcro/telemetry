package com.github.mjcro.telemetry;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Telemetry {
    /**
     * Constructs full telemetry instance using given data.
     *
     * @param route           Route name, mandatory.
     * @param duration        Duration, optional.
     * @param error           Error, optional.
     * @param metadata        Metadata, optional.
     * @param requestHeaders  Request headers, optional.
     * @param responseHeaders Response headers, optional.
     * @param responseCode    Response code, optional.
     * @param request         Request body, optional.
     * @param response        Response body, optional.
     * @return Telemetry instance.
     */
    static Telemetry full(
            String route,
            Duration duration,
            Throwable error,
            Map<String, Object> metadata,
            Map<String, String> requestHeaders,
            Map<String, String> responseHeaders,
            Integer responseCode,
            byte[] request,
            byte[] response
    ) {
        return new TelemetryByteArray(
                route,
                null,
                duration,
                error,
                metadata,
                requestHeaders,
                responseHeaders,
                responseCode,
                request,
                response
        );
    }

    /**
     * Constructs request-only telemetry.
     *
     * @param route          Route name, mandatory.
     * @param error          Error, optional.
     * @param metadata       Metadata, optional.
     * @param requestHeaders Request headers, optional.
     * @param request        Request body, optional.
     * @return Telemetry instance.
     */
    static Telemetry request(
            String route,
            Throwable error,
            Map<String, Object> metadata,
            Map<String, String> requestHeaders,
            byte[] request
    ) {
        return full(route, null, error, metadata, requestHeaders, null, null, request, null);
    }

    /**
     * @return Request route.
     */
    String getRoute();

    /**
     * @return Telemetry creation time.
     */
    Instant getCreatedAt();

    /**
     * @return Request duration.
     */
    Duration getDuration();

    /**
     * @return Error, if present.
     */
    Optional<Throwable> getError();

    /**
     * @return Telemetry metadata, if present.
     */
    Map<String, Object> getMetadata();

    /**
     * @return Request headers.
     */
    Map<String, String> getRequestHeaders();

    /**
     * @return Response headers.
     */
    Map<String, String> getResponseHeaders();

    /**
     * @return Request body.
     */
    InputStream getRequest();

    /**
     * @return Response body.
     */
    InputStream getResponse();

    /**
     * @return Response code (mostly HTTP response code).
     */
    Optional<Integer> getResponseCode();

    /**
     * Fetches single request header by its name.
     *
     * @param name request header name.
     * @return Header value, if present.
     */
    default Optional<String> getRequestHeader(String name) {
        return Optional.ofNullable(getRequestHeaders().get(name.toLowerCase(Locale.ROOT)))
                .filter($value -> !$value.isEmpty());
    }

    /**
     * Fetches single response header by its name.
     *
     * @param name request header name.
     * @return Header value, if present.
     */
    default Optional<String> getResponseHeader(String name) {
        return Optional.ofNullable(getResponseHeaders().get(name.toLowerCase(Locale.ROOT)))
                .filter($value -> !$value.isEmpty());
    }

    /**
     * @return Request body as string.
     */
    default String getRequestString() {
        return InputStreamUtil.toString(getRequest());
    }

    /**
     * @return Response body as string.
     */
    default String getResponseString() {
        return InputStreamUtil.toString(getResponse());
    }

    /**
     * Maps telemetry instance.
     *
     * @param mapping Mapping function.
     * @return Mapped telemetry.
     */
    default Telemetry map(Function<Telemetry, Telemetry> mapping) {
        return mapping == null ? this : mapping.apply(this);
    }

    /**
     * Returns new telemetry instance with supplied error.
     *
     * @param error Supplied error.
     * @return Telemetry.
     */
    default Telemetry withError(Throwable error) {
        return TelemetryErrorHolderAdapter.of(this, error);
    }

    /**
     * Appends metadata to telemetry.
     *
     * @param append Metadata to append.
     * @return New telemetry instance containing appended metadata.
     */
    default Telemetry withMetadata(Map<String, Object> append) {
        return TelemetryMetadataAdapter.append(this, append);
    }

    /**
     * Appends metadata to telemetry.
     *
     * @param key   Metadata key.
     * @param value Metadata value.
     * @return New telemetry instance containing appended metadata.
     */
    default Telemetry withMetadata(String key, Object value) {
        return withMetadata(Collections.singletonMap(key, value));
    }

    /**
     * Sends this telemetry to given consumer.
     *
     * @param consumer Consumer to send data to.
     */
    default void sendTo(Consumer<? super Telemetry> consumer) {
        if (consumer != null) {
            consumer.accept(this);
        }
    }
}
