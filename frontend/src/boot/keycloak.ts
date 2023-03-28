import { boot } from 'quasar/wrappers'
import VueKeyCloak from '@dsb-norge/vue-keycloak-js';
import axios from 'axios';

export default boot(async({app}) => {
  async function tokenInterceptor() {
    axios.interceptors.request.use(config => {
      config.headers.Authorization = `Bearer ${app.config.globalProperties.$keycloak.token}`
      return config
    }, error => {
      return Promise.reject(error)
    })
  }

  return new Promise(resolve => {
    app.use(VueKeyCloak, {
      init: {
        onLoad: '',
        flow: 'standard',
        pkceMethod: 'S256',
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
        checkLoginIframe: false
      },
      config: {
        url: 'http://localhost:8080/',
        realm: 'WSU-TCSA',
        clientId: 'app-vue'
      },
      onReady: (keycloak) => {
        tokenInterceptor()
        resolve()
      }
    })
  })
})
