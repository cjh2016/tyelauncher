package com.boll.tyelauncher.exception;

package com.toycloud.launcher.exception;

public class LoadRQImageException extends RuntimeException {
    public LoadRQImageException() {
    }

    public LoadRQImageException(String message) {
        super(message);
    }

    public LoadRQImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadRQImageException(Throwable cause) {
        super(cause);
    }
}
