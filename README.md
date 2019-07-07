

How to integrate Systemd/Watchdog support


TL;DR
-----

Add dependency
```xml
        <dependency>
            <groupId>com.github.dpimkin.systemd</groupId>
            <artifactId>systemd-spring-boot-starter</artifactId>
            <version>0.0.1</version>
        </dependency>
```

Adjust watchdog period in application.properties (optional).
```properties
systemd.watchdog.delay=2000
```

Unit file example
-----------------
```properties
[Unit]
Description=simple watchdog
After=syslog.target

[Service]
WorkingDirectory=/usr/local/share/simple-watchdog
SyslogIdentifier=johndoe
ExecStart=/bin/bash -c "/path/to/jdk/bin/java -jar /usr/local/share/simple-watchdog/simple-watchdog.jar"
User=johndoe
Type=simple
WatchdogSec=30s
Restart=always

[Install]
WantedBy=multi-user.target
```