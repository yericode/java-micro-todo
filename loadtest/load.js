import http from 'k6/http';
import { check } from 'k6';

export const options = {
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
    stages: [
        { duration: '2m', target: 20 },   // 爬升
        { duration: '10m', target: 20 },  // 穩態，這段才是有效數據
        { duration: '1m', target: 0 },
    ],
    // thresholds: {
    //     http_req_duration: ['p(95)<500', 'p(99)<1000'],
    //     http_req_failed: ['rate<0.01'],
    // },
};

export default function () {
    const res = http.get('http://172.23.150.81:8078/api/v1/users/01a064c8-1b1c-7e28-b522-79c0469f10cf/todos');
    check(res, { 'status 200': (r) => r.status === 200 });
}