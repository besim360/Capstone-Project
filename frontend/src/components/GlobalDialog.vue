<template>
  <q-btn :label="props.label" @click="dialogDisplayHandler" label-color="primary" color="secondary"></q-btn>
  <q-dialog v-model="store.active" persistent transition-show="scale" transition-hide="scale">
    <q-card class="form-width" dark>
      <q-card-section v-if="store.activeFType === 'AdvancedSearch'" class="bg-secondary advanced-search-form">
        <div class="row">
          <div class="col-6">
            <h5 class="q-ma-sm">Advanced Search</h5>
          </div>
          <div class="col-6" style="justify-content: end; display: flex">
            <q-btn flat round icon="close" @click="dialogCloseHandler"></q-btn>
          </div>
        </div>
        <q-form >
          <div class="row" :key="line" v-for="line  in queryLine">
            <div class="col-3" v-if="line.logic !='NA'" style="padding: 10px">
              <q-select outlined dark label-color="white" v-model="line.logic" :options="selectionOptions"></q-select>
            </div>
            <div class="col-3" style="padding: 10px">
              <q-select outlined dark label-color="white" v-model="line.category" :options="categoryOptions"></q-select>
            </div>
            <div :class="line.logic != 'NA' ? 'col-6' : 'col-9'" style="padding: 10px">
              <q-input outlined dark label-color="white" v-model="line.queryText"></q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-6" style="justify-content: start; display: flex;">
              <q-btn label="Clear" flat dark text-color="warning" @click="clearSearchFields"></q-btn>
            </div>
            <div class="col-6" style="justify-content: end; display: flex;">
              <q-btn flat label="+ Add Line" dark text-color="info" @click="addSearchField"></q-btn>
            </div>
          </div>
        </q-form>
      </q-card-section>
      <q-card-section v-else-if="store.activeFType === 'AddFolder'">
        {{store.fAddFolderOptions.folderName}}
      </q-card-section>
      <q-card-section v-else-if="store.activeFType === 'AddBookmark'">
        {{store.fAddBookmarkOptions.folderName}}
      </q-card-section>
      <q-card-actions class="bg-secondary" style="justify-content: center; padding-right: 25px; padding-bottom: 20px; padding-left: 25px;">
        <q-btn :label="dialogCloseLabel" class="bg-white full-width" text-color="accent" @click="dialogSearchHandler"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script lang="ts" setup>
import { computed } from 'vue';
import { useDialogStore } from 'src/stores/dialog-forms';
import { useSearchStore } from 'src/stores/search';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';

const props = defineProps<{
  type: string
  label: string
}>()

const store = useDialogStore();
const searchStore = useSearchStore();
const router = useRouter();
const { queryLine } = storeToRefs(searchStore)

const selectionOptions = [
  'AND',
  'OR',
  'NOT'
]

const categoryOptions = [
  'Subject', 'Author', 'Title', 'Source', 'Year', 'DOI', 'Date Range', 'All'
]

const dialogDisplayHandler = () => {
  loadFormType(props.type);
  store.showForm();
}

const addSearchField = () => {
  searchStore.addNewQueryLine()
}

const clearSearchFields = () => {
  searchStore.clearQuery();
}


const loadFormType = (type: string) => {
  switch(type) {
    case 'AdvancedSearch': {
      store.clearAdvancedSearchForm();
      store.loadAdvancedSearch();
    }
  }
};

const dialogCloseLabel = computed(() => {
  if (props.type === 'AdvancedSearch'){
    return 'Search'
  } else if (props.type === 'AddFolder') {
    return 'Add Folder'
  } else if (props.type === 'AddBookmark') {
    return 'Add Bookmark'
  } else {
    return ''
  }
})

const dialogCloseHandler = () => {
  if (props.type === 'AdvancedSearch'){
    store.clearAdvancedSearchForm();
  }
  store.removeForm();
  searchStore.resetQuery();
}

const dialogSearchHandler = () => {
  if (props.type === 'AdvancedSearch'){
    store.clearAdvancedSearchForm();
  }
  store.removeForm();
  router.push('/results');
  searchStore.clearQuery();
}

</script>
<stlye lang="scss" scoped>
.advanced-search-form {
  min-height: 200px;
}

.form-width {
  min-width: 900px;
}
</stlye>
