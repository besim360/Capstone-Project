import { Router as vueRouter } from 'vue-router';

import AuthWrapper from './AuthWrapper';
import KeycloakWrapper from './KeycloakWrapper';
import { AxiosRequestConfig } from 'axios';

export default class AuthService {
  // Set the auth wrapper to an instance of the Qiksar Keycloak wrapper
  static authWrapper: AuthWrapper = new KeycloakWrapper();
  static router: vueRouter;

  static async attachCors(config: AxiosRequestConfig) {
    return {
      ...config,
      headers: {
        ...config.headers,
        'Access-Control-Allow-Origin': '*',
      }
    }
  }

  static get AuthWrapper(): AuthWrapper {
    return AuthService.authWrapper;
  }

  static get Router(): vueRouter {
    return AuthService.router;
  }

  static async Boot(router: vueRouter): Promise<void> {
    AuthService.router = router;
    await AuthService.AuthWrapper.Init(router);
  }
}
