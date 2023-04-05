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
      <GlobalDialog type="AddFolder" label="Add Folder" :value="blank"></GlobalDialog>
    </q-item>
    <q-item class="bg-primary q-pa-xs">                                  <!--  Recent Searches -->
        <q-item-section avatar>
          <q-icon color="white" name="history" />
        </q-item-section>
        <q-item-section class="text-white text-subtitle1"> Recent Searches </q-item-section>
    </q-item>
    <div v-if="userStore.searchHistory.length > 0" style="height: 50%;">
      <div class="row" v-for="record in userStore.searchHistory" :key="record.id">
        <q-item style="padding-right: 10px; padding-left: 10px; flex: 1;">
          <!-- <q-icon color="black" name="saved_search" class="q-pa-sm"/> -->
          <q-btn color="blue" flat push @click="() => {historyClick(record)}" style="padding-left: 0; padding-right: 0; flex: 1;">{{ reduceQueryLength(record.query) }}</q-btn>
          <q-btn round flat icon="delete" color="blue" style="margin-left: auto;" @click="() => {deleteHistoryClick(record)}"></q-btn>
        </q-item>
      </div>
    </div>
    <div v-else style="margin-top: 20px;">
      <div class="row" style="justify-content: center;">
        No recent searches...
      </div>
    </div>
    </q-drawer>
    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import { AxiosInstance } from 'axios';
import { inject, ref } from 'vue';
import AuthService from 'src/auth/AuthService';
import useUserStore from 'src/auth/userStore';
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router';
import { HistoryRecord } from 'src/api/models/history';
import { useSearchStore } from 'src/stores/search';
import { SearchRecord } from 'src/api/models/search';
import GlobalDialog from 'src/components/GlobalDialog.vue';

const router = useRouter();
const userStore = useUserStore();
const searchStore = useSearchStore();

const blank = ref({} as SearchRecord)
const userapi: AxiosInstance = inject('userapi') as AxiosInstance;

const openDrawer = computed(() => {
  const isHome = router.currentRoute.value.name === 'home';
  const isIndex = router.currentRoute.value.name === 'index';
  return userStore.loggedIn && !isHome && !isIndex;
})

onMounted( async () => {
  if (userStore.loggedIn) {
    const userID = await AuthService.AuthWrapper.User.auth_id;
    const userHistory = await userapi.get('/history/'+userID)
    userStore.setSearchHistory(userHistory.data)
  }
})

const historyClick = (searchHistory: HistoryRecord) => {
  searchStore.clearQuery()
  searchStore.setResults(searchHistory.results)
  searchStore.updateQuery(0, searchHistory.query, 'All', 'NA')
  console.log('history click');
  router.push('/results');
}

const deleteHistoryClick = async (searchHistory: HistoryRecord) => {
  const userHistory = await userapi.delete(`/history/${userStore.user.auth_id}/${searchHistory.id}`)
  userStore.setSearchHistory(userHistory.data)
}

const reduceQueryLength = (queryText: string) => {
  let qtextshort
  if(queryText.length > 20){
    qtextshort = queryText.substring(0, 20)
    qtextshort = qtextshort + '...'
  } else {
    qtextshort = queryText
  }
  return qtextshort
}

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
