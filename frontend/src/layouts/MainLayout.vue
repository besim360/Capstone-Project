<template>
  <q-layout view="hHh LpR fFf">

    <q-header reveal elevated class="bl-30 text-dark">
      <div class="color-strip"></div>
      <q-toolbar class="rem-pad">
        <div class="bg-accent logo-bg" @click="goHome">
          <img src="../assets/Icons/branding-iconography/wsu-icon.svg" class="center-aligned">
        </div>
        <q-toolbar-title>
          WSU Technical Communication Search
        </q-toolbar-title>
        <q-space />
        <q-tabs shrink indicator-color="primary">
          <q-route-tab to="/search" label="Search" exact/>
          <q-route-tab to="/bibliography" label="Bibliography" exact  v-if="userStore.loggedIn"/>
          <q-route-tab to="/upload" label="Upload" exact v-if="userStore.loggedIn && isAdmin"/>
          <q-route-tab to="/results" label="Results" exact/>
        </q-tabs>
        <q-btn v-if="userStore.loggedIn" @click="logoutHandler" flat>Logout</q-btn>
        <q-btn v-else @click="loginHandler" flat>Sign In</q-btn>
      </q-toolbar>
    </q-header>

     <q-drawer v-model="openDrawer" side="left" overlay bordered persistent class="bl-30">    <!--    #Fix color later -->

      <q-item class="bg-primary q-pa-xs">                                       <!--  Bookmarks -->
        <q-item-section avatar>
          <q-icon color="white" name="bookmark" />
        </q-item-section>

        <q-item-section class="text-white text-subtitle1"> Bookmarks </q-item-section>
      </q-item>


      <div class="row" v-for="n in 1" :key="n" style="justify-content: center;">

      <div style="max-width: 370px;  max-height: 350px;">
        <q-list>
          <q-expansion-item
            class="text-subtitle2 text-black"
            expand-separator
            icon="folder"
            label="Business and management"
          >
            <q-card>

                <div class="row" v-for="n in 2" :key="n" style="justify-content: center;">
                  <q-item class="">

                    <q-icon color="black" name="folder" class="q-pa-sm"/>
                    <q-item-section avatar class="text-blue text-subtitle2"> The Case for "Living" Models </q-item-section>

                  </q-item>
                </div>

            </q-card>
          </q-expansion-item>


          <q-expansion-item
            class="text-subtitle2 text-black"
            expand-separator
            icon="folder"
            label="Technology"
          >
          </q-expansion-item>
        </q-list>
      </div>
    </div>

    <q-item class="bg-secondary" style="justify-content: center;">        <!--  Add New Folder -->
      <q-btn color="accent" label="Add Folder" />
    </q-item>

    <q-item class="bg-primary q-pa-xs">                                  <!--  Recent Searches -->
        <q-item-section avatar>
          <q-icon color="white" name="history" />
        </q-item-section>
        <q-item-section class="text-white text-subtitle1"> Recent Searches </q-item-section>
    </q-item>

    <div class="row" v-for="n in 1" :key="n" style="justify-content: center;">
      <q-item>
        <q-icon color="black" name="saved_search" class="q-pa-sm"/>
        <q-item-section avatar class="text-blue text-subtitle2"> The Case for "Living" Models </q-item-section>
      </q-item>

    </div>




    </q-drawer>




    <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script setup lang="ts">
import AuthService from 'src/auth/AuthService';
import useUserStore from 'src/auth/userStore';
import { computed } from 'vue'
import { useRouter } from 'vue-router';

const router = useRouter();
const userStore = useUserStore();
const openDrawer = computed(() => {
  const isHome = router.currentRoute.value.name === 'home';
  return userStore.loggedIn && !isHome;
})

const isAdmin = computed(() => {
  return AuthService.AuthWrapper.HasRole('RealmAdmin');
})

const loginHandler = async () => {
  await AuthService.AuthWrapper.Login('/');
  router.push('/search')
}
const logoutHandler = async () => {
  await AuthService.AuthWrapper.Logout();
  router.push('/')
}
const goHome = () => {
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
