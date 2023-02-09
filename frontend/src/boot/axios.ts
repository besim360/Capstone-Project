import { boot } from 'quasar/wrappers';
import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
  }
}

async function attachToken(config: AxiosRequestConfig) {
  const token = await updateToken();
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

// Be careful when using SSR for cross-request state pollution
// due to creating a Singleton instance here;
// If any client changes this (global) instance, it might be a
// good idea to move this instance creation inside of the
// "export default () => {}" function below (which runs individually
// for each client)
const UserApi = axios.create({ baseURL: 'https://api.example.com' });
UserApi.interceptors.request.use(attachToken)

const SearchApi = axios.create({ baseURL: 'https://api.example.com' });
SearchApi.interceptors.request.use(attachToken)

export default boot(({ app }) => {
  // for use inside Vue files (Options API) through this.$axios and this.$api

  app.config.globalProperties.$axios = axios;
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  app.config.globalProperties.$userapi = UserApi;
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
  app.config.globalProperties.$searchapi = SearchApi;
});

export { UserApi };
