import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
    scenarios: {
        constant_arrive: {
            executor: 'constant-arrival-rate',
            duration: '1h',
            timeUnit: '1s',
            rate: 10,
            preAllocatedVUs: 10,
            maxVUs: 20,
        }
    },
};

export default function () {
    const res = http.get('http://host.docker.internal:8078/api/v1/users/01a064c8-1b1c-7e28-b522-79c0469f10cf/todos');
    check(res, { 'status 200': (r) => r.status === 200 });
}