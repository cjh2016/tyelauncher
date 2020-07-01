package com.boll.tyelauncher.exception;

package com.toycloud.launcher.exception;

public class AppLockException extends RuntimeException {
    public AppLockException() {
    }

    public AppLockException(String message) {
        super(message);
    }

    public AppLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppLockException(Throwable cause) {
        super(cause);
    }
}