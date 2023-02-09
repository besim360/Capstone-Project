import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';


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

export default class UserClient {
  private static instance: UserClient = new UserClient();

  api!: AxiosInstance;

  static getInstance(): UserClient {
    return UserClient.instance;
  }

  static getApi(): AxiosInstance {
    return UserClient.instance.api
  }

  private constructor() {
    const instance = axios.create({
      baseURL: USER_API_BASE_URL
    })
  }
}
