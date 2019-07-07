


System unit file example
------------------------

```bash
[Unit]
Description=Some
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


bootstrap
---------

```bash
adduser --system --no-create-home johndoe
mkdir /usr/local/share/simple-watchdog
cp simple-watchdog.jar /usr/local/share/simple-watchdog/
cp simple-watchdog.service /etc/systemd/system/
chown johndoe -R /usr/local/share/simple-watchdog
systemctl daemon-reload
systemctl start simple-watchdog
systemctl status simple-watchdog
```
