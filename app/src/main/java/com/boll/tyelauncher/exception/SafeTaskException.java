package com.boll.tyelauncher.exception;


public class SafeTaskException extends RuntimeException {
    public SafeTaskException() {
    }

    public SafeTaskException(String message) {
        super(message);
    }

    public SafeTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public SafeTaskException(Throwable cause) {
        super(cause);
    }
}
