import { boot } from 'quasar/wrappers';
import AuthService from 'src/auth/AuthService';
import { Router } from 'src/router';

export default boot(async () => {
  await AuthService.Boot(Router);
})
