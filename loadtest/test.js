import http from 'k6/http';
import { sleep, check } from 'k6';
import { Counter } from 'k6/metrics';

// export const CounterError = new Counter('content_not_ok');
export const options = {
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
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

    scenarios: {
        ramping_arrive: {
            executor: 'ramping-arrival-rate',
            startRate: 300, // 測試開始時每一個 timeunit 總共發出的 iterations 數
            timeUnit: '1m', // 在每個階段整場時段內平均分散發出 startRate、target 個迭代，預設 1s
            preAllocatedVUs: 20, // 以整場壓力最高的為主，target 600 = 10 次/秒; API iterations p99 = 921ms (會多算一點預分配 VU，其實反而比較保險)，10 * 0.921 * 2保險 = 20
            stages: [
                { target: 300, duration: '1m' },
                { target: 600, duration: '2m' },
                { target: 600, duration: '4m' },
                { target: 60, duration: '2m' },
            ],
            maxVUs: 50, // 動態配置的 VU，有設定時若 preAllocatedVUs 的 vu 無法達到指定速率則會補上，但是本身也必須消耗資源，會干擾測試的準確度
        }
    }
};

export default function () {
    const res = http.get('https://quickpizza.grafana.com/api/json?name=Bert');
    const result = check(res, {
        'is status 200': (r) => r.status === 200,
        'has body size': (r) => r.body.length > 0,
        'name is Bert': (r) => r.json('name') === 'Bert',
    });
    // console.log('Result: ', result);
    // CounterError.add(res.json('name') !== 'Bert'); // 使用 CounterError 變數來累加
    // sleep(2);
}