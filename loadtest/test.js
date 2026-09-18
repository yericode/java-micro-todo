import http from 'k6/http';
import { sleep } from 'k6';

function test() {
    console.log('@@ this is a test');
}
test();


export const options = {
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
    // vus: 5,
    iterations: 50,
    duration: '10s'
}

export default function() {
    http.get('http://host.docker.internal:8078/api/v1/users/01a064c8-1b1c-7e28-b522-79c0469f10cf/todos');
    sleep(1);
}