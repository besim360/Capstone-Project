import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('layouts/MainLayout.vue'),
    meta: {
      isAuthenticated: false
    },
    children: [{ path: '', component: () => import('pages/IndexPage.vue') }],
  },
  {
    path: '/search',
    name: 'search',
    component: () => import('layouts/MainLayout.vue'),
    meta: {
      isAuthenticated: false
    },
    children: [{ path: '', component: () => import('pages/SearchPage.vue')}]
  },
  {
    path: '/results',
    name: 'results',
    component: () => import('layouts/MainLayout.vue'),
    meta: {
      isAuthenticated: false
    },
    children: [{ path: '', component: () => import('pages/ResultsPage.vue')}]
  },
  {
    path: '/upload',
    name: 'upload',
    component: () => import('layouts/MainLayout.vue'),
    meta: {
      isAuthenticated: true
    },
    children: [{ path: '', component: () => import('pages/UploadPage.vue')}],
  },
  {
    path: '/bibliography',
    name: 'bibliogrpahy',
    component: () => import('layouts/MainLayout.vue'),
    meta: {
      isAuthenticated: false
    },
    children: [{ path: '', component: () => import('pages/BibliographyPage.vue')}]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    name: 'error',
    meta: {
      isAuthenticated: false
    },
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
