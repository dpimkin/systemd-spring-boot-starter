package com.github.dpimkin.systemd.spring.boot.autoconfigure;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.dpimkin.systemd.spring.boot.autoconfigure.SystemdLibrary.State.HEARTBEAT;
import static com.github.dpimkin.systemd.spring.boot.autoconfigure.SystemdLibrary.State.OPERATIONAL;
import static com.github.dpimkin.systemd.spring.boot.autoconfigure.SystemdLibrary.State.STOPPING;

@Slf4j
@SuppressWarnings("unused")
final class LifecycleManager {
    private final SystemdLibrary aSystemdService;
    private final AtomicBoolean contextStarted = new AtomicBoolean(false);
    private final AtomicBoolean contextShutdown = new AtomicBoolean(false);

    LifecycleManager(SystemdLibrary aSystemdService) {
        this.aSystemdService = aSystemdService;
    }

    @EventListener
    void handleContextRefreshed(ContextRefreshedEvent event) {
        if (isApplicationStarted()) {
            log.trace("handleContextRefreshed");
            aSystemdService.notify(OPERATIONAL);
        }
    }

    @EventListener
    void handleContextClosed(ContextClosedEvent event) {
        if (isApplicationStopping()) {
            log.trace("handleContextClosed");
            aSystemdService.notify(STOPPING);
        }
    }

    @Scheduled(fixedDelayString = "${systemd.watchdog.delay:1000}")
    void heartbeat() {
        if (isApplicationRunning()) {
            log.trace("scheduledHeartbeat");
            aSystemdService.notify(HEARTBEAT);
        }

    }

    private boolean isApplicationStarted() {
        return contextStarted.compareAndSet(false, true);
    }

    private boolean isApplicationStopping() {
        return contextShutdown.compareAndSet(false, true);
    }

    private boolean isApplicationRunning() {
        return contextStarted.get() && !contextShutdown.get();
    }
}
