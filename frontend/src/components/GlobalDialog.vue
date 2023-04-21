<template>
  <q-btn v-if="props.type === 'AdvancedSearch'" :label="props.label" @click="dialogDisplayHandler" label-color="primary" color="secondary"></q-btn>
  <q-btn v-else-if="props.type === 'AddFolder'" :label="props.label" @click="dialogDisplayHandler" color="accent"></q-btn>
  <q-icon v-else clickable name="bookmark_add" @click="dialogDisplayHandler"/>
  <q-dialog v-model="store.activeSearchForm" persistent transition-show="scale" transition-hide="scale" v-if="props.type === 'AdvancedSearch'">
    <q-card class="form-width" dark>
      <q-card-section class="bg-secondary advanced-search-form">
        <div class="row">
          <div class="col-6">
            <h5 class="q-ma-sm">Advanced Search</h5>
          </div>
          <div class="col-6" style="justify-content: end; display: flex">
            <q-btn flat round icon="close" @click="dialogCloseHandler"></q-btn>
          </div>
        </div>
        <q-form >
          <div class="row" :key="index" v-for="(line, index) in queryObjects">
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
      <q-card-actions class="bg-secondary" style="justify-content: center; padding-right: 25px; padding-bottom: 20px; padding-left: 25px;">
        <q-btn :label="dialogCloseLabel" class="bg-white full-width" text-color="accent" @click="dialogSearchHandler"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="store.activeBookmark" persistent transition-show="scale" transition-hide="scale" v-else-if="props.type === 'AddBookmark'">
    <q-card class="form-width" dark>
      <q-card-section class="bg-secondary">
        <div class="row">
          <div class="col-6">
            <h5 class="q-ma-sm">Add Bookmark</h5>
          </div>
          <div class="col-6" style="justify-content: end; display: flex">
            <q-btn flat round icon="close" @click="dialogCloseHandler"></q-btn>
          </div>
        </div>
        <q-form >
          <div class="row">
            <div class="col-12" style="padding: 10px">
              <q-select outlined dark label-color="white" :options="userStore.bookmarks.bookmarkFolders" v-model="fAddBookmarkOptions.folder" label="Bookmark Folder" />
            </div>
            <div class="col-12" style="padding: 10px">
              <q-input outlined dark label-color="white" v-model="fAddBookmarkOptions.bookmarkName" label="Bookmark Name"></q-input>
            </div>
          </div>
        </q-form>
      </q-card-section>
      <q-card-actions class="bg-secondary" style="justify-content: center; padding-right: 25px; padding-bottom: 20px; padding-left: 25px;">
        <q-btn :label="dialogCloseLabel" class="bg-white full-width" text-color="accent" @click="dialogSearchHandler"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="store.activeFolder" persistent transition-show="scale" transition-hide="scale" v-else-if="props.type === 'AddFolder'">
    <q-card class="form-width" dark>
      <q-card-section class="bg-secondary">
        <div class="row">
          <div class="col-6">
            <h5 class="q-ma-sm">Add Bookmark Folder</h5>
          </div>
          <div class="col-6" style="justify-content: end; display: flex">
            <q-btn flat round icon="close" @click="dialogCloseHandler"></q-btn>
          </div>
        </div>
        <q-form >
          <div class="row">
            <div class="col-12" style="padding: 10px">
              <q-input outlined dark label-color="white" v-model="fAddFolderOptions.folderName" label="Bookmark Folder"></q-input>
            </div>
          </div>
        </q-form>
      </q-card-section>
      <q-card-actions class="bg-secondary" style="justify-content: center; padding-right: 25px; padding-bottom: 20px; padding-left: 25px;">
        <q-btn :label="dialogCloseLabel" class="bg-white full-width" text-color="accent" @click="dialogSearchHandler"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script lang="ts" setup>
import { computed, inject } from 'vue';
import { useDialogStore } from 'src/stores/dialog-forms';
import { useSearchStore } from 'src/stores/search';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { SearchRecord } from 'src/api/models/search';
import { AxiosInstance } from 'axios';
import useUserStore from 'src/auth/userStore';
import { Bookmark } from 'src/api/models/bookmark';

const props = defineProps<{
  type: string
  label: string
  value: SearchRecord
}>()

const userapi: AxiosInstance = inject('userapi') as AxiosInstance;
const searchapi: AxiosInstance = inject('searchapi') as AxiosInstance;

const userStore = useUserStore();
const store = useDialogStore();
const searchStore = useSearchStore();
const router = useRouter();
const { queryLine } = storeToRefs(searchStore)
const { fAddBookmarkOptions, fAddFolderOptions } = storeToRefs(store);
const selectionOptions = [
  'AND',
  'OR',
  'NOT'
]

const categoryOptions = [
  'Authors', 'Title', 'Source', 'StartYear', 'EndYear', 'DOI', 'All'
]

const queryObjects = computed(() => {
  let values = Object.values(queryLine.value)
  return values
})


const dialogDisplayHandler = () => {
  loadFormType(props.type);
  if(props.type === 'AdvancedSearch'){
    store.showSearchForm();
  } else if (props.type === 'AddBookmark'){
    store.showBookmarkForm();
  } else if (props.type === 'AddFolder'){
    store.showFolderForm();
  }

}

const addSearchField = () => {
  if(Object.keys(searchStore.queryLine).length <= 7){
    searchStore.addNewQueryLine()
  }
}

const clearSearchFields = () => {
  searchStore.clearQuery();
}

const addFolder = async (folderName: string) => {
  const data = {
    label: folderName,
    bookmarks: []
  }
  const userBookmarks = await userapi.post(`/bookmarks/folder/${userStore.user.auth_id}`, data)
  userStore.setBookmarks(userBookmarks.data)
}

const addBookmark = async (bookmarkFolder: Bookmark) => {
  const data = {
    label: store.fAddBookmarkOptions.record.title,
    title: store.fAddBookmarkOptions.record.title,
    authors: store.fAddBookmarkOptions.record.authors,
    sourceAbbrev: store.fAddBookmarkOptions.record.sourceAbbrev,
    sourceLong: store.fAddBookmarkOptions.record.sourceLong,
    volNum: store.fAddBookmarkOptions.record.volNum,
    date: store.fAddBookmarkOptions.record.date,
    startYear:store.fAddBookmarkOptions.record.startYear,
    endYear:store.fAddBookmarkOptions.record.endYear,
    pages:store.fAddBookmarkOptions.record.pages,
    subjects: store.fAddBookmarkOptions.record.subjects,
    doi: store.fAddBookmarkOptions.record.doi
  }
  const userBookmarks = await userapi.post(`/bookmarks/bookmark/folder/${bookmarkFolder.id}/${userStore.user.auth_id}`, data)
  userStore.setBookmarks(userBookmarks.data)
}

const performAdvancedSearch = async () => {
  const queryLines = Object.values(searchStore.queryLine)
  let fields = ''
  let query = ''
  let operators = ''
  for(let i = 0; i < queryLines.length; i++) {
    query = query + queryLines[i].queryText
    if(queryLines[i].category === 'All'){
      fields = fields + 'fullText'
    } else {
      fields = fields + queryLines[i].category.charAt(0).toLowerCase() + queryLines[i].category.slice(1)
    }

    if(queryLines[i].logic === 'NA'){
      operators = operators + 'AND'
    } else {
      operators = operators + queryLines[i].logic
    }

    if(i < queryLines.length - 1){
      query=query + ','
      fields=fields + ','
      operators=operators + ','
    }
  }

  const results = await searchapi.get(`article/bool?query=${query}&operators=${operators}&fields=${fields}&limit=100`)
  searchStore.setResults(results.data);
}

const loadFormType = (type: string) => {
  switch(type) {
    case 'AdvancedSearch': {
      store.clearAdvancedSearchForm();
      store.loadAdvancedSearch();
      break;
    }
    case 'AddBookmark': {
      store.clearAddBookmarkForm();
      store.loadAddBookmark();
      if(props.value &&  typeof props.value !== 'string'){
        store.setBookmark(props.value);
      }
      break;
    }
    case 'AddFolder': {
      store.clearAddFolderForm();
      store.loadAddFolder();
      break;
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
    searchStore.resetQuery();
    store.removeSearchForm();
  } else if (props.type === 'AddBookmark'){
    store.clearAddBookmarkForm();
    store.removeBookmarkForm();
  } else if (props.type === 'AddFolder'){
    store.clearAddFolderForm();
    store.removeFolderForm();
  }
}

const dialogSearchHandler = () => {
  if (props.type === 'AdvancedSearch'){
    performAdvancedSearch();
    store.clearAdvancedSearchForm();
    store.removeSearchForm();
    searchStore.clearQuery();
    router.push('/results');
  } else if (props.type === 'AddBookmark'){
    addBookmark(store.fAddBookmarkOptions.folder)
    store.clearAddBookmarkForm();
    store.removeBookmarkForm();
  } else if (props.type === 'AddFolder'){
    addFolder(store.fAddFolderOptions.folderName);
    store.clearAddFolderForm();
    store.removeFolderForm();
  }
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
