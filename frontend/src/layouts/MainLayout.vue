<template>
  <q-layout view="hHh LpR fFf">

    <q-header reveal elevated class="bl-30 text-dark pl-0">
      <div class="color-strip"></div>
      <q-toolbar>
        <div class="bg-accent logo-bg">
          <img src="../assets/Icons/branding-iconography/wsu-icon.svg" class="center-aligned">
        </div>
        <q-toolbar-title>

          Bibliography Application
        </q-toolbar-title>
        <q-space />
        <q-tabs v-model="tab" shrink>
          <q-tab name="search" label="Search" />
          <q-tab name="bibliography" label="Bibliography" />
          <q-tab name="upload" label="Upload" />
        </q-tabs>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" side="left" overlay bordered>
      drawer content
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script>
import { ref } from 'vue'

export default {
  setup () {
    const leftDrawerOpen = ref(true)

    return {
      leftDrawerOpen,
    }
  }
}
</script>

<style lang="scss" scoped>
.bl-30 {
  background-color: $bl-30
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
