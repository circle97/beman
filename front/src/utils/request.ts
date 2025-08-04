import axios, { AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
});

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 可以在这里添加认证token
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    return response.data;
  },
  (error: AxiosError) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default service;