package org.aa.truebloods;

import java.util.concurrent.Callable;

public class Validator {

    private boolean valid = true;
    private String message = "Stopping";

    public boolean valid() {
        return this.valid;
    }

    public String getMessage() {
        return this.message;
    }

    public Validator require(String message, Callable<Boolean> condition) {
        try {
            if (!valid) return this;
            this.valid = condition.call();
            if (!this.valid) {
                this.message = message;
            }
            return this;
        } catch (Exception ignored) { }
        return this;
    }

}
