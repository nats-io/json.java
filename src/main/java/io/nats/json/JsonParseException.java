package io.nats.json;

import java.io.IOException;

/**
 * The exception thrown when the {@code JsonParser} cannot parse
 */
public class JsonParseException extends IOException {
    /**
     * Constructs an {@code JsonParseException} with the specified detail message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public JsonParseException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code JsonParseException} with the specified detail message
     * and cause.
     *
     * <p> Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated into this exception's detail
     * message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     */
    public JsonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code JsonParseException} with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for IO exceptions that are little more
     * than wrappers for other throwables.
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     */
    public JsonParseException(Throwable cause) {
        super(cause);
    }
}
