import http from 'k6/http';
import { sleep, check, group } from 'k6';
import { Counter } from 'k6/metrics';
import { uuidv4 } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';
import { SharedArray } from 'k6/data';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';
import exec from 'k6/execution';

// export const CounterError = new Counter('content_not_ok');
// console.log('@@ init context');
export const errorCounter = new Counter('http_status_is_5xx');

const accounts = new SharedArray('Test Accounts Data', () => {
    return JSON.parse(open('./data.json'));
});

const couponIds = new SharedArray('Test Coupon Data', () => {
    const ids = papaparse.parse(open('./coupon.csv'), {header: false}).data;
    ids.shift(); // 移除標頭
    return ids;
})

export const options = {
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
    // noCookiesReset: true,
    // vus: 5,
    // iterations: 50,
    // thresholds: {
    //     http_req_duration: ['p(95)<150', 'p(99)<200'],
    //     http_req_failed: ['rate<0.01'],
    // },

    // thresholds: {
    //     http_req_failed: [
    //         {
    //             threshold: 'rate<0.01',
    //             abortOnFail: true,
    //         }
    //     ],
    //     http_req_duration: [
    //         {
    //             threshold: 'p(99)<220',
    //             abortOnFail: true,
    //             delayAbortEval: '2s',
    //         },
    //         {
    //             threshold: 'p(95)<200',
    //             abortOnFail: true,
    //             delayAbortEval: '2s',
    //         }
    //     ],
    // }

    // thresholds: {
    //     // 命名為 new Counter 傳入名稱
    //     content_not_ok: [
    //         {
    //             threshold: 'count<10',
    //             abortOnFail: true
    //         },
    //     ]
    // }

    // thresholds: {
    //     // check 方法固定產生的指標，型別固定為 rate
    //     checks: [
    //         {
    //             threshold: 'rate>0.99',
    //             abortOnFail: true
    //         }
    //     ]
    // }

    // scenarios: {
    //     warmup: {
    //         executor: 'ramping-vus',
    //         startVus: 0,
    //         stages: [
    //             { target: 20, duration: '10s' }, // 逐步爬升到指定 VU
    //             { target: 20, duration: '20s' }, // 維持數量暖機
    //         ],
    //         gracefulRampDown: '5s',
    //         gracefulStop: '5s',
    //         tags: { phase: 'warmup' }, // phase 為自訂的 key 名稱
    //     },
    //     main_test: {
    //         executor: 'constant-vus',
    //         vus: 20,
    //         duration: '30s',
    //         startTime: '30s',
    //         gracefulStop: '5s',
    //         tags: { phase: 'main' }
    //     }
    // },

    // thresholds: {
    //     // 根據不同標籤設定不同 thresholds
    //     'http_req_failed{phase:warmup}': ['rate<0.01'],
    //     'http_req_failed{phase:main}': ['rate<0.01'],
    //     'http_req_duration{phase:main}': ['p(95)<150', 'p(99)<200'],
    // }

    // scenarios: {
    //     constant_arrive: {
    //         executor: 'constant-arrival-rate',
    //         duration: '1m',       // 場景持續時間，不含 gracefulStop
    //         timeUnit: '1s',       // 在這段時間內平均分散發出 rate 個迭代，預設 1s
    //         rate: 2,              // 每 1 秒 2 個 = 每 0.5 秒 1 個 = 2 次/秒，整場約 120 次
    //         preAllocatedVUs: 5,   // 迭代時間 × 每秒速率 + 緩衝；假設迭代約 2 秒：2 × 2 ≈ 4，再加緩衝
    //         maxVUs: 50, // 動態配置的 VU，有設定時若 preAllocatedVUs 的 vu 無法達到指定速率則會補上，但是本身也必須消耗資源，會干擾測試的準確度
    //     }
    // }

    // scenarios: {
    //     ramping_arrive: {
    //         executor: 'ramping-arrival-rate',
    //         startRate: 300, // 測試開始時每一個 timeunit 總共發出的 iterations 數
    //         timeUnit: '1m', // 在每個階段整場時段內平均分散發出 startRate、target 個迭代，預設 1s
    //         preAllocatedVUs: 20, // 以整場壓力最高的為主，target 600 = 10 次/秒; API iterations p99 = 921ms (會多算一點預分配 VU，其實反而比較保險)，10 * 0.921 * 2保險 = 20
    //         stages: [
    //             { target: 300, duration: '1m' },
    //             { target: 600, duration: '2m' },
    //             { target: 600, duration: '4m' },
    //             { target: 60, duration: '2m' },
    //         ],
    //         maxVUs: 50, // 動態配置的 VU，有設定時若 preAllocatedVUs 的 vu 無法達到指定速率則會補上，但是本身也必須消耗資源，會干擾測試的準確度
    //     }
    // }

    scenarios: {
        shared_iterations: {
            executor: 'shared-iterations',
            vus: accounts.length,
            iterations: couponIds.length,
        }
        // constant_vus: {
        //     executor: 'constant-vus',
        //     vus: 3,
        //     duration: '10s'
        // }
    },

    thresholds: {
        // http_req_failed: [
        //     { threshold: 'rate<0.01', abortOnFail: true }
        // ]
        // checks: [
        //     {threshold: 'rate>0.99', abortOnFail: true}
        // ]
        // http_status_is_5xx: [
        //     { threshold: 'count<5', abortOnFail: true }
        // ]
        // 'checks{check:status_is_200}': [
        //     {threshold: 'rate>0.99', abortOnFail: true}
        // ],
        // 'http_req_failed{scenario:shared_iterations}': [
        //     {threshold: 'rate<0.01', abortOnFail: true}
        // ],
        // 'http_req_failed{name:TestURL}': [
        //     {threshold: 'rate<0.01', abortOnFail: true}
        // ],
        // 'http_req_failed{name:TestURL,api:/status}': [
        //     {threshold: 'rate<0.01', abortOnFail: true}
        // ],
        // 'http_req_failed{group:::login and visit homepage}': ['rate<0.01'],
        // 'http_req_duration{group:::login and visit homepage}': ['p(95)<500', 'p(99)<1000'],
        // 'group_duration{group:::login and visit homepage}': ['p(95)<500', 'p(99)<1000']
    }
};

// export default function (data) {
    // const code = 503;
    // 預設 name tag 會解析所有參數 = https://quickpizza.grafana.com/api/status/201 202 203 ...
    // 可以使用 http.url string template 來修改 = https://quickpizza.grafana.com/api/status/${}
    // 可以直接指定 tags = TestURL
    // const res = http.get(http.url`https://quickpizza.grafana.com/api/status/${code}`, { tags: {name: 'TestURL', api: '/status'}});
    // check(res, { 'status_is_200': (r) => r.status === 200 });
    // errorCounter.add(res.status.toString().startsWith('5'));

    // let token;
    // group('login and visit homepage', function () {
    //     let res = http.get('https://quickpizza.grafana.com/api/json?token=123456');
    //     check(res, {
    //         'login status is 200': (r) => r.status === 200,
    //         'has token': (r) => r.json('token') !== null
    //     });

    //     token = res.json('token');
    //     res = http.get('https://quickpizza.grafana.com/api/status/200', { headers: { 'X-Access-Token': token } });
    //     check(res, {
    //         'homepage status is 200': (r) => r.status === 200
    //     });
    // })

    // const result = check(res, {
    //     'is status 200': (r) => r.status === 200,
    //     'has body size': (r) => r.body.length > 0,
    //     'name is Bert': (r) => r.json('name') === 'Bert',
    // });
    // console.log('Result: ', result);
    // CounterError.add(res.json('name') !== 'Bert'); // 使用 CounterError 變數來累加
    // sleep(2);

    // console.log('@@ data', data);
    // http.get('https://quickpizza.grafana.com/api/status/200', { headers: { 'X-Access-Token': data } });
    // sleep(0.5)
// }

// let token = "";
// export function test() {
//     let res;
//     if (!token) {
//         res = http.get(`https://quickpizza.grafana.com/api/json?token=${uuidv4()}`);
//         token = res.json('token');
//     }
//     console.log('@@ token', token);
//     res = http.get('https://quickpizza.grafana.com/api/status/200', { headers: { 'X-Access-Token': token } });
//     check(res, { 'status is 200': (r) => r.status === 200 });
//     sleep(0.5);
// }

// const accounts = new SharedArray('accounts', () => ['acc_1', 'acc_2', 'acc_3']);
// export default function () {
//     const requests = [];
//     for (let i = 0; i < 9; i++) {
//         const account = accounts[i % accounts.length];
//         requests.push(['GET', `https://quickpizza.grafana.com/api/json?token=${account}_${uuidv4().substr(-3)}`]);
//     }
//     const res = http.batch(requests);
//     const tokens = res.map(r => {
//         check(r, {'status is 200': (r) => r.status === 200});
//         return r.json('token');
//     })
//     console.log('@@token', tokens);
// }

// export function setup() {
//     const uuid = uuidv4();
//     const res = http.get(`https://quickpizza.grafana.com/api/json?token=${uuid}`)
//     return res.json('token');
// }

// export function teardown(data) {
//   console.log('@@ teardown data: ', data);
// }

// let isLogin = false;



export default function () {
    const account = accounts[exec.vu.idInTest - 1];
    const coupon = couponIds[exec.scenario.iterationInTest];
    console.log(`idInTest: ${exec.vu.idInTest}, iterationInTest: ${exec.scenario.iterationInTest}, account: ${JSON.stringify(account)}, coupon: ${coupon}`);

    // if (!isLogin) {
    //     const jar = http.cookieJar();
    //     jar.set('https://quickpizza.grafana.com/api/cookies', 'JSESSIONID', uuidv4().toString().substr(-6), {
    //         domain: 'quickpizza.grafana.com',
    //         path: '/api/cookies',
    //         secure: true,
    //         max_age: 5,
    //     });
    //     isLogin = true;
    // }

    // const res = http.get('https://quickpizza.grafana.com/api/cookies');
    // const cookies = res.json().cookies;
    // if (cookies.JSESSIONID) {
    //     console.log('@@ JSESSIONID=', cookies.JSESSIONID);
    // } else {
    //     console.log('@@ JSESSIONID is expiry');
    // }
}