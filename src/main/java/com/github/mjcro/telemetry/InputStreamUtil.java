package com.github.mjcro.telemetry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class InputStreamUtil {
    static final InputStream EMPTY_STREAM = new InputStream() {
        @Override
        public int read() {
            return -1;
        }
    };

    static InputStream toStream(byte[] bytes) {
        return bytes == null || bytes.length == 0
                ? EMPTY_STREAM
                : new ByteArrayInputStream(bytes);
    }

    /**
     * Reads all input stream data to byte array.
     *
     * @param stream Source input stream.
     * @return Produced bytes.
     */
    static byte[] toBytes(InputStream stream) {
        if (stream == null) {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = stream.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            return result.toByteArray();
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    /**
     * Reads all input stream data to UTF8 string.
     *
     * @param stream Source input stream.
     * @return Produced string.
     */
    static String toString(InputStream stream) {
        return new String(toBytes(stream), StandardCharsets.UTF_8);
    }

    private InputStreamUtil() {
    }
}
