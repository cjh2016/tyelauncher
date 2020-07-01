package com.boll.tyelauncher.exception;

package com.toycloud.launcher.exception;

public class UpdateContentProviderException extends RuntimeException {
    public UpdateContentProviderException() {
    }

    public UpdateContentProviderException(String message) {
        super(message);
    }

    public UpdateContentProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateContentProviderException(Throwable cause) {
        super(cause);
    }
}