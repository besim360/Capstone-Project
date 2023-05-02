<template>
  <q-page class="row justify-center">
    <div class="col-7 off-center">
      <q-form  @submit="onSubmit">
        <div class="row">

        </div>
        <q-input outlined v-model="queryLine[0].queryText" label="Search For Articles">
          <template v-slot:append>
            <q-icon name="search" color="primary" />
          </template>
        </q-input>
        <GlobalDialog type="AdvancedSearch" label="Advanced Search" :value="blank"></GlobalDialog>
        <div class="row justify-center">
          <q-btn  class="col-2 q-mt-xl" label="Search" type="submit" color="primary"/>
        </div>
      </q-form>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { useSearchStore } from 'src/stores/search';
import GlobalDialog from 'src/components/GlobalDialog.vue';
import useUserStore from 'src/auth/userStore';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { inject, ref } from 'vue';
import { SearchRecord, SearchResults } from 'src/api/models/search';
import { AxiosInstance } from 'axios';
import AuthService from 'src/auth/AuthService';

const searchStore = useSearchStore();
const userStore = useUserStore();
const searchapi: AxiosInstance = inject('searchapi') as AxiosInstance;
const userapi: AxiosInstance = inject('userapi') as AxiosInstance;
const router = useRouter();
const blank = ref({} as SearchRecord)
const {queryLine} = storeToRefs(searchStore)


const onSubmit = async () => {
  const results = await searchapi.get(`article/search?text=${queryLine.value[0].queryText}&limit=100&fields=fullText`);
  searchStore.setResults(results.data);
  if (userStore.loggedIn) {

    const userID = await AuthService.AuthWrapper.User.auth_id;
    for (let i = 0; i < results.data.length; i++) {
      let result: SearchRecord = results.data[i]
      Object.keys(result).forEach(key => {
        const keyWithType = key as keyof typeof result;
        let value = result[keyWithType];
        if (value === null) {
          result = {...result, [keyWithType]: 'N/A'}
          results.data[i] = result
        }
      })
    }
    const historyData = {
      uid: userID,
      query: searchStore.queryLine[0].queryText,
      queryDate: new Date(),
      results: results.data
    }
    await userapi.post('/history/', historyData);
    const userHistory = await userapi.get('/history/'+userID)
    userStore.setSearchHistory(userHistory.data)
  }
  router.push('/results');
  searchStore.clearQueryText();
}

</script>

<style scoped lang="scss">

.off-center {
  margin-top: 15%;
}

</style>
