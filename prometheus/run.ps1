docker run -d '
--name prometheus '
-p 9090:9090 '
-v prometheus:/etc/prometheus '
prom/prometheus:v3.13.1 --config.file=/etc/prometheus/prometheus.yml --storage.tsdb.path=/prometheus --web.enable-lifecycle