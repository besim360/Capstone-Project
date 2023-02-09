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
        <GlobalDialog type="AdvancedSearch" label="Advanced Search"></GlobalDialog>
        <div class="row justify-center">
          <q-btn  class="col-2 q-mt-xl" label="Search" type="submit" color="primary"/>
        </div>
      </q-form>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar';
import { useSearchStore } from 'src/stores/search';
import GlobalDialog from 'src/components/GlobalDialog.vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';

const $q = useQuasar();
const searchStore = useSearchStore();
const router = useRouter();

const {queryLine} = storeToRefs(searchStore)

const onSubmit = () => {
  $q.notify({
    color: 'secondary',
    textColor: 'bl-30',
    message: queryLine.value[0].queryText
  })
  router.push('/results');
  searchStore.clearQuery();
}

</script>

<style scoped lang="scss">

.off-center {
  margin-top: 15%;
}

</style>
