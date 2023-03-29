import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('layouts/MainLayout.vue'),

    meta: {
      anonymous: true
    },

    children: [
      {
        path: '',
        name: 'index',
        component: () => import('pages/IndexPage.vue'),
        meta: { anonymous: true }
      },
      {
        path: '/search',
        name: 'search',
        component: () => import('pages/SearchPage.vue'),
        meta: { anonymous: true }
      },
      {
        path: '/results',
        name: 'results',
        component: () => import('pages/ResultsPage.vue'),
        meta: { anonymous: true }
      },
      {
        path: '/upload',
        name: 'upload',
        component: () => import('pages/UploadPage.vue'),
        meta: { anonymous: false, role: 'RealmAdmin' }
      },
      {
        path: '/bibliography',
        name: 'bibliogrpahy',
        component: () => import('pages/BibliographyPage.vue'),
        meta: { anonymous: false }
      },

    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    name: 'error',
    meta: {
      anonymous: true
    },
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
