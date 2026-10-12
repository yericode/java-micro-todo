docker run `
--rm `
--name k6 `
-v ./loadtest:/loadtest `
-w /loadtest `
grafana/k6 run --out json=result.json load.js