docker run `
--rm `
--name k6 `
-v ./loadtest:/loadtest `
-w /loadtest `
grafana/k6 run test.js