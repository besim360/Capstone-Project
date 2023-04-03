<template>

    <div class="row justify-center q-ma-lg">
      <div class="col-6 off-center">

        <q-form  @submit="onSubmit">
          <q-input outlined v-model="queryLine[0].queryText" label="Search For Articles">
            <template v-slot:append>
              <q-icon name="search" color="primary" />
            </template>
          </q-input>
          <GlobalDialog type="AdvancedSearch" label="Advanced Search" :value="blank"></GlobalDialog>
        </q-form>

      </div>
    </div>
      <div class="row" v-for="result in getDataPartition" :key="result.id" style="justify-content: center;">
        <div class="col-5">
          <q-item-label class="text-h6" lines="1">
            <span class="text-weight-medium"> {{ result.authors }} </span>
            <GlobalDialog type="AddBookmark" label="AddBookmark" :value="result"></GlobalDialog>
          </q-item-label>
          <span class="text-blue-7 text-h6"> {{ result.doi }} </span>
          <p ellipsis no-wrap class="text-body2"> {{ result.sourceLong }} </p>
        </div>

        <div class=items-center style="align-items: center">
          <q-btn label="Add Citation" type="submit" color="primary"/>
        </div>

      </div>

  <div class="q-pa-lg flex flex-center">
    <q-pagination
      v-model="page"
      :min="1"
      :max="Math.ceil(totalResults / totalPages)"
      input
    />
  </div>

</template>


<script setup lang="ts">
import { useQuasar } from 'quasar';
import { useSearchStore } from 'src/stores/search';
import GlobalDialog from 'src/components/GlobalDialog.vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { SearchRecord } from 'src/api/models/search';

const $q = useQuasar();
const searchStore = useSearchStore();
const {queryLine} = storeToRefs(searchStore)

// const linkMax = ref(5);
const page = ref(1)
const resultsPerPage = ref(10)
const blank = ref({} as SearchRecord)

const getDataPartition = computed(() => {
  return searchStore.results.slice((page.value-1)*totalPages.value,(page.value - 1)*totalPages.value+totalPages.value)
})
const totalPages = computed(() => {

  return Math.ceil(totalResults.value / resultsPerPage.value)
})

const totalResults = computed(() => {
  let totalResultsRet: number;
  if (searchStore.results.length === 0){
    totalResultsRet = 1
  } else {
    totalResultsRet = searchStore.results.length
  }
  return totalResultsRet;
})

const onSubmit = () => {
  $q.notify({
    color: 'secondary',
    textColor: 'bl-30',
    message: queryLine.value[0].queryText
  })
  searchStore.clearQuery();
}
</script>

<style scoped lang="scss"></style>
