import axios from 'axios';
import Cookies from "js-cookie";

const axiosInstance = axios.create();
 
//Request interceptor for API calls
axiosInstance.interceptors.request.use(
   config => {
    const accessToken = localStorage.jwtToken;
    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    } 
    // config.headers['Content-Type'] = 'application/json';
    return config;
  },
  error => {
    Promise.reject(error)
});

//const refresh = Cookies.get("refresh");


//request interceptor to add the auth token header to requests
// axios.interceptors.request.use(
//   (config) => {
//     const accessToken = localStorage.getItem("accessToken");
//     if (accessToken) {
//       config.headers["x-auth-token"] = accessToken;
//     }
//     return config;
//   },
//   (error) => {
//     Promise.reject(error);
//   }
// );
//response interceptor to refresh token on receiving token expired error
// let isRefreshing = false;
// let failedQueue = [];

// const processQueue = (error, token = null) => {
//     failedQueue.forEach(prom => {
//         if (error) {
//             prom.reject(error);
//         } else {
//             prom.resolve(token);
//         }
//     });

//     failedQueue = [];
// };

// axiosInstance.interceptors.response.use(
//     response => {
//         return response;
//     }, 
//     err  => {
//         const originalRequest = err.config;
//         const refresh = Cookies.get("refresh");
//         console.log("error" + refresh);
//         if (err.response.status === 401 && !originalRequest._retry) {
//             if (isRefreshing) {
//                 return new Promise(function(resolve, reject) {
//                     failedQueue.push({ resolve, reject });
//                 })
//                     .then(token => {
//                         originalRequest.headers['Authorization'] = 'Bearer ' + token;
//                         return axios(originalRequest);
//                     })
//                     .catch(err => {
//                         return Promise.reject(err);
//                     });
//             }

//             originalRequest._retry = true;
//             isRefreshing = true;

//             return new Promise(function(resolve, reject) {
//                 axios
//                     .post('/api/refresh', {refresh})
//                     .then(({ data }) => {
//                         axios.defaults.headers.common['Authorization'] = 'Bearer ' + data.refreshToken;
//                         originalRequest.headers['Authorization'] = 'Bearer ' + data.refreshToken;
//                         Cookies.set("refresh", data.refreshToken);
//                         processQueue(null, data.refreshToken);
//                         resolve(axios(originalRequest));
//                     })
//                     .catch(err => {
//                         processQueue(err, null);
//                         //store.dispatch(showMessage({ message: 'Expired Token' }));

//                         reject(err);
//                     })
//                     .then(() => {
//                         isRefreshing = false;
//                     });
//             }
//             );
//         }

//         return Promise.reject(err);
//     }
// );


let isTokenRefreshing = false;
let refreshSubscribers = [];

const onTokenRefreshed = (accessToken) => {
  refreshSubscribers.map((callback) => callback(accessToken));
};

const addRefreshSubscriber = (callback) => {
  refreshSubscribers.push(callback);
};

// Response interceptor for API calls
axiosInstance.interceptors.response.use((response) => {
  return response
}, async function (error) {
  console.log("response" + error)
  const {
    config,
    response: { status },
  } = error;
  const originalRequest = error.config;
  if (status === 401 ) {
    console.log('토큰 만료')
    if (!isTokenRefreshing) {
      // isTokenRefreshing이 false인 경우에만 token refresh 요청
      isTokenRefreshing = true;
      const refresh = Cookies.get("refresh");
      if(refresh) {
    // 토큰 갱신 서버통신
        const { data } = await axiosInstance.post(`/api/refresh`, { refresh, });
        //console.log("data = " + data )
        //console.log("access = " + data.accessToken )
        //console.log("refreshToken = " + data.refreshToken )
        localStorage.setItem("jwtToken", data.accessToken);
        Cookies.set("refresh", data.refreshToken);
        isTokenRefreshing = false;
        axios.defaults.headers.common.Authorization = `Bearer ${data.accessToken}`;
        onTokenRefreshed(data.accessToken);
      }
    }
        // token이 재발급 되는 동안의 요청은 refreshSubscribers에 저장
        const retryOriginalRequest = new Promise((resolve) => {
          addRefreshSubscriber((accessToken) => {
            originalRequest.headers.Authorization = "Bearer " + accessToken;
            resolve(axios(originalRequest));
          });
        });
        return retryOriginalRequest;
    }
    return Promise.reject(error);
});

export default axiosInstance;