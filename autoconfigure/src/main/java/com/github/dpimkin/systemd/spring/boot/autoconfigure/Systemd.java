package com.github.dpimkin.systemd.spring.boot.autoconfigure;

import com.sun.jna.Library;

@FunctionalInterface
public interface Systemd extends Library {
    int sd_notify(int unset_environment, String state);
}
