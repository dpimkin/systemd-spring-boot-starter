package com.github.dpimkin.systemd.spring.boot.autoconfigure;

import com.sun.jna.Native;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Slf4j
@SuppressWarnings("unused")
public class SystemdConfig {
    private static final String SYSTEMD_SO = "systemd";

    @Bean
    Systemd provideSystemd() {
        try {
            return (Systemd) Native.loadLibrary(SYSTEMD_SO, Systemd.class);
        } catch (UnsatisfiedLinkError ex) {
            log.warn("Unable to load following library: {}", SYSTEMD_SO);
            return (unset_environment, state) -> 0;
        }
    }

    @Bean
    SystemdLibrary provideSystemdService(Systemd systemd) {
        return (state) -> {
            if (log.isTraceEnabled()) {
                log.trace("notifying {}", state.value());
            }
            systemd.sd_notify(0, state.value());
        };
    }

    @Bean
    LifecycleManager provideLifecycleManager(SystemdLibrary aSystemdService) {
        return new LifecycleManager(aSystemdService);
    }
}
