package com.github.dpimkin.systemd.spring.boot.autoconfigure;

@FunctionalInterface
public interface SystemdLibrary {
    void notify(State state);

    enum State {
        OPERATIONAL("READY=1"), HEARTBEAT("WATCHDOG=1"), STOPPING("STOPPING=1");

        private final String value;

        State(String value) {
            this.value = value;
        }

        String value() {
            return value;
        }
    }
}
