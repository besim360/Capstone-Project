import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/IndexPage.vue') }],
  },
  {
    path: '/search',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/SearchPage.vue')}]
  },
  {
    path: '/results',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/ResultsPage.vue')}]
  },
  {
    path: '/upload',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/UploadPage.vue')}]
  },
  {
    path: '/bibliography',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/BibliographyPage.vue')}]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
