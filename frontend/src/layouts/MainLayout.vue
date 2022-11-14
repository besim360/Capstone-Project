<template>
  <q-layout view="hHh LpR fFf">

    <q-header reveal elevated class="bl-30 text-dark">
      <div class="color-strip"></div>
      <q-toolbar class="rem-pad">
        <div class="bg-accent logo-bg">
          <img src="../assets/Icons/branding-iconography/wsu-icon.svg" class="center-aligned">
        </div>
        <q-toolbar-title>
          WSU Technical Communication Search
        </q-toolbar-title>
        <q-space />
        <q-tabs shrink>
          <q-route-tab to="/search" label="Search" exact/>
          <q-route-tab to="/bibliography" label="Bibliography" exact/>
          <q-route-tab to="/upload" label="Upload" exact v-if="loggedIn"/>
          <q-route-tab to="/results" label="Results" exact/>
        </q-tabs>
        <q-btn v-if="loggedIn" @click="logoutHandler">Logout</q-btn>
        <q-btn v-else @click="loginHandler">Sign In</q-btn>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="openDrawer" side="left" overlay bordered persistent>
      drawer content
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router';
const route = useRoute();
const router = useRouter()
const openDrawer = computed(() => {
  return loggedIn.value && !route.matched.some(({ name }) => name === 'home')
})
const loggedIn = ref(true);
const loginHandler = () => {
  loggedIn.value = true;
  router.push('/search')
}
const logoutHandler = () => {
  loggedIn.value = false;
  router.push('/')
}
</script>

<style lang="scss" scoped>
.bl-30 {
  background-color: $bl-30
}

.rem-pad {
  padding: 0 !important;
}
.color-strip {
  background-color: $primary;
  width: 100%;
  height: 8px;
}

.logo-bg {
  width: 56px;
  height: 56px;
  text-align: center;
}

.center-aligned {
  position: relative;
  top: 50%;
  -ms-transform: translateY(-50%);
  -webkit-transform: translateY(-50%);
  transform: translateY(-50%);
}
</style>
