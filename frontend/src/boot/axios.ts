import { boot } from 'quasar/wrappers';
import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';
import AuthService from 'src/auth/AuthService';

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}

async function attachToken(config: AxiosRequestConfig) {
  const token = await AuthService.AuthWrapper.GetAuthToken();
  return {
    ...config,
    headers: {
      ...config.headers,
      common: {
        Authorization: `Bearer ${token}`
      }
    }
  }
}
// Remove this when production build
async function attachCors(config: AxiosRequestConfig) {
  return {
    ...config,
    headers: {
      ...config.headers,
      'Access-Control-Allow-Origin': '*',
    }
  }

  // return {
  //   ...config,
  //   headers: {
  //     ...config.headers,
  //     'Access-Control-Allow-Origin': '*',
  //   }
  //   }
  // }
}

export default boot(({ app }) => {
  const UserApi = axios.create({ baseURL: 'http://localhost:8000' });
  UserApi.interceptors.request.use(attachToken)
  const SearchApi = axios.create({ baseURL: 'http://localhost:9001' });
  UserApi.interceptors.request.use(attachToken)
  SearchApi.interceptors.request.use(attachCors)
  // for use inside Vue files (Options API) through this.$axios and this.$api

  app.config.globalProperties.$axios = axios;
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file
  app.provide('axios', app.config.globalProperties.$axios);
  // ^ ^ ^ used to inject into a script see main layout for example

  app.config.globalProperties.$userapi = UserApi;
  app.provide('userapi', app.config.globalProperties.$userapi);
  app.config.globalProperties.$searchapi = SearchApi;
  app.provide('searchapi', app.config.globalProperties.$searchapi);
});

export {axios}



