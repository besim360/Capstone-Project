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
            <GlobalDialog type="AddBookmark" label="AddBookmark" :value="result" v-if="userStore.loggedIn"></GlobalDialog>
          </q-item-label>
          <span class="text-blue-7 text-h6" v-if="result.doi !== 'N/A'">
            <a :href="'https://www.doi.org/'+result.doi">{{ result.doi }}</a>
          </span>
          <p ellipsis no-wrap class="text-body2 no-margin"> {{ result.title }} </p>
          <p ellipsis no-wrap class="text-body2"> {{ result.sourceLong }} </p>
        </div>

        <div class=items-center style="align-items: center">
          <q-btn label="Add Citation" type="submit" color="primary" v-if="userStore.loggedIn" @click="openModel(result)"/>
          <q-dialog v-model="createCitation" persistent >
              <q-card style="mid-width:350px" v-if="noBibliographies">
                <q-card-section>
                  <div class="text-h6">No Bibliography Available!</div>
                </q-card-section>
                <q-card-section class="q-pt-none">
                  <q-input dense v-model="newBibLabel" autofocus></q-input>
                </q-card-section>
                <q-card-actions>
                  <q-btn flat label="Cancel" v-close-popup></q-btn>
                  <q-space></q-space>
                  <q-btn label="Create Bibliography" class="bg-primary" text-color="white" size="md" @click="createBibliography"></q-btn>
                </q-card-actions>
              </q-card>
              <q-card style="min-width: 400px; width: 1200px;" v-else>
                <q-card-section>
                  <div class="text-h6">Add Citation</div>
                </q-card-section>
                <q-card-section class="q-pt-none">
                  <q-select v-model="selectedBibliography" :options="userStore.bibliographies.bibliographies" label="Select Bibliography" dense outlined></q-select>
                </q-card-section>
                <q-card-section v-if="selectedBibliography != null">
                  <q-select v-model="sourceType" dense :options="[{label: 'Journal', id: 0},{label: 'Book', id: 1},{label: 'Web', id: 2}]" label="Select source type" @update:model-value="loadForm" outlined></q-select>
                  <div class="text-h7 q-mt-md">Primary Author</div>
                  <div class="row">
                    <div class="col-4">
                      <q-input v-model="sourceInfo.primaryAuthor.firstName" dense label="First Name"></q-input>
                    </div>
                    <div class="col-2 offset-1">
                      <q-input v-model="sourceInfo.primaryAuthor.middleInitial" dense label="M.I"></q-input>
                    </div>
                    <div class="col-4 offset-1">
                      <q-input v-model="sourceInfo.primaryAuthor.lastName" dense label="Last Name"></q-input>
                    </div>
                    <div class="col-12 q-mt-md">
                      <q-input v-model="sourceInfo.title" dense label="Title"></q-input>
                    </div>
                    <div class="col-5 q-mt-md">
                      <q-input v-model="sourceInfo.date" dense label="Publication Date"></q-input>
                    </div>
                    <div class="col-5 offset-2 q-mt-md">
                      <q-input v-model="sourceInfo.chapter" dense label="Chapter"></q-input>
                    </div>
                    <div class="col-12 q-mt-md">
                      <q-input v-model="sourceInfo.contributers" dense label="Contributors"></q-input>
                    </div>
                  </div>
                  <div class="row" v-if="sourceType !== null && sourceType.id === 0">
                    <div class="col-2 q-mt-md">
                      <q-input v-model="journalSourceInfo.version" dense label="Version"></q-input>
                    </div>
                    <div class="col-2 offset-1 q-mt-md">
                      <q-input v-model="journalSourceInfo.number" dense label="Number"></q-input>
                    </div>
                    <div class="col-3 offset-2 q-mt-md">
                      <q-input v-model="journalSourceInfo.pages" dense label="Pages"></q-input>
                    </div>
                    <div class="col-5 q-mt-md">
                      <q-input v-model="journalSourceInfo.publisher" dense label="Publisher"></q-input>
                    </div>
                    <div class="col-5 offset-2 q-mt-md">
                      <q-input v-model="journalSourceInfo.doi" dense label="DOI"></q-input>
                    </div>
                  </div>
                  <div class="row" v-if="sourceType !== null && sourceType.id === 1" >
                    <div class="col-12 q-mt-md">
                      <q-input v-model="bookSourceInfo.editors" dense label="Editors"></q-input>
                    </div>
                    <div class="col-12 q-mt-md">
                      <q-input v-model="bookSourceInfo.translators" dense label="Translators"></q-input>
                    </div>
                    <div class="col-4 q-mt-md">
                      <q-input v-model="bookSourceInfo.publisher" dense label="Publisher"></q-input>
                    </div>
                    <div class="col-2 offset-1 offset-1 q-mt-md">
                      <q-input v-model="bookSourceInfo.publicationLocale" dense label="Locale"></q-input>
                    </div>
                    <div class="col-4 offset-1 q-mt-md">
                      <q-input v-model="bookSourceInfo.pages" dense label="pages"></q-input>
                    </div>
                  </div>
                  <div class="row" v-if="sourceType !== null && sourceType.id === 2" >
                    <div class="col-3 q-mt-md">
                      <q-input v-model="webSourceInfo.lastModified" dense label="Last Modified"></q-input>
                    </div>
                    <div class="col-3 offset-1 offset-1 q-mt-md">
                      <q-input v-model="webSourceInfo.retrieved" dense label="Retrieved Date"></q-input>
                    </div>
                    <div class="col-4 offset-1 q-mt-md">
                      <q-input v-model="webSourceInfo.doi" dense label="DOI"></q-input>
                    </div>
                  </div>
                </q-card-section>
                <q-card-actions>
                  <q-btn flat label="Cancel" v-close-popup @click="clearBibliography"></q-btn>
                  <q-space></q-space>
                  <q-btn label="Create" class="bg-primary" text-color="white" :disable="noBibliography" size="md" @click="createNewCitation" v-close-popup></q-btn>
                </q-card-actions>
              </q-card>
            </q-dialog>
        </div>

      </div>

  <div class="q-pa-lg flex flex-center">
    <q-pagination
      v-model="page"
      :min="1"
      :max="Math.ceil(totalPages)"
      input
    />
  </div>

</template>


<script setup lang="ts">
import { useSearchStore } from 'src/stores/search';
import useUserStore from 'src/auth/userStore';
import GlobalDialog from 'src/components/GlobalDialog.vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { computed, inject, ref } from 'vue';
import { SearchRecord } from 'src/api/models/search';
import { AxiosInstance } from 'axios';
import AuthService from 'src/auth/AuthService';

const searchStore = useSearchStore();
const userStore = useUserStore();
const router = useRouter();
const {queryLine} = storeToRefs(searchStore)
const userapi: AxiosInstance = inject('userapi') as AxiosInstance;
const searchapi: AxiosInstance = inject('searchapi') as AxiosInstance;

type SourceType = {
  id: number;
  label: string;
}

// const linkMax = ref(5);
const page = ref(1)
const resultsPerPage = ref(10)
const blank = ref({} as SearchRecord)
const createCitation = ref(false)
const newBibLabel = ref('');
const selectedCitation = ref<SearchRecord | null>(null);
const selectedBibliography = ref<any>(null);
const sourceType = ref<null | SourceType >(null)


const blankCitation = {
  title: '',
  authors: '',
  volNum: '',
  date: '',
  startYear: '',
  endYear: '',
  pages: '',
  doi: '',
  author: { firstName: '', middleInitial: '', lastName: '' },
  contributors: '',
  version: '',
  number: '',
  publicationlocale: '',
  format: '',
  accessDate: '',
  publisher: '',
  type: '',
  chapter: '',
  editors: '',
  translators: '',
  fullString: '',
}

const initialSourceInfo = {
  primaryAuthor: {
    firstName: '',
    middleInitial: '',
    lastName: '',
  },
  date: '',
  chapter: '',
  contributers: '',
  title: ''
}
const sourceInfo = ref(initialSourceInfo)

const initialJournalSource = {
  version: '',
  number: '',
  pages: '',
  publisher: '',
  doi: ''
}
const journalSourceInfo = ref(initialJournalSource)

const initialWebSource = {
  lastModified: '',
  retrieved: '',
  doi: ''
}
const webSourceInfo = ref(initialWebSource)

const initialBookSource = {
  pages: '',
  editors: '',
  translators: '',
  publisher: '',
  publicationLocale: ''
}
const bookSourceInfo = ref(initialBookSource)


const getDataPartition = computed(() => {
  return searchStore.results.slice((page.value-1)*(totalResults.value/totalPages.value),(page.value - 1)*(totalResults.value/totalPages.value)+(totalResults.value/totalPages.value))
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

const noBibliographies = computed(() => {
  if(userStore.bibliographies.bibliographies) {
    return userStore.bibliographies.bibliographies.length === 0;
  }
  return userStore.bibliographies.bibliographies
})

const noBibliography = computed(() => {
  return selectedBibliography.value == null;
})

const bibId = computed(() => {
  if(selectedBibliography.value._id){
    return selectedBibliography.value._id
  }
  if(selectedBibliography.value.id){
    return selectedBibliography.value.id
  }
  return ''
})

const clearBibliography = () => {
  selectedBibliography.value = null;
  sourceType.value=null
  sourceInfo.value = initialSourceInfo;
  bookSourceInfo.value = initialBookSource;
  journalSourceInfo.value = initialJournalSource;
  webSourceInfo.value = initialWebSource;
}

const openModel = (model: SearchRecord) => {
  createCitation.value = true;
  selectedCitation.value = model
}

const createNewCitation = async () => {
  let citation = blankCitation

  if(selectedCitation.value && sourceType.value) {
    citation.type = sourceType.value.label
    citation.authors = selectedCitation.value.authors
    citation.author = sourceInfo.value.primaryAuthor
    citation.contributors = sourceInfo.value.contributers
    citation.chapter = sourceInfo.value.chapter
    citation.date = sourceInfo.value.date
    citation.title = sourceInfo.value.title
    if(sourceType.value.id === 0) {
      citation.version = journalSourceInfo.value.version
      citation.number = journalSourceInfo.value.number
      citation.pages = journalSourceInfo.value.pages
      citation.doi = journalSourceInfo.value.doi
      citation.publisher = journalSourceInfo.value.publisher
    }

    if(sourceType.value.id === 1) {
      citation.pages = bookSourceInfo.value.pages
      citation.editors = bookSourceInfo.value.editors
      citation.publicationlocale = bookSourceInfo.value.publicationLocale
      citation.publisher = bookSourceInfo.value.publisher
      citation.translators = bookSourceInfo.value.translators
    }

    if(sourceType.value.id === 2) {
      citation.endYear = webSourceInfo.value.retrieved
      citation.doi = webSourceInfo.value.doi
      citation.startYear = webSourceInfo.value.lastModified
    }
  }
  if(selectedBibliography.value && bibId){
    const retCitation = await userapi.post('/bibliographies/bibliography/citation/'+bibId.value+'/'+userStore.user.auth_id, citation)
    if(retCitation.status === 200){
      const retBibliography = await userapi.get('/bibliographies/'+userStore.user.auth_id)
      userStore.setBibliographies(retBibliography.data)
    }
  }

}

const loadForm = () => {
  if(!sourceType.value){
    return
  }
  if(!selectedCitation.value){
    return
  }

  if(sourceType.value.id === 0){
    sourceInfo.value.contributers = selectedCitation.value.authors
    journalSourceInfo.value.doi = selectedCitation.value.doi
    journalSourceInfo.value.number = selectedCitation.value.volNum
    journalSourceInfo.value.pages = selectedCitation.value.pages
    sourceInfo.value.title = selectedCitation.value.title
    sourceInfo.value.date = selectedCitation.value.date + selectedCitation.value.endYear
    return
  }

  if(sourceType.value.id === 1) {
    sourceInfo.value.title = selectedCitation.value.title
    sourceInfo.value.date = selectedCitation.value.date + selectedCitation.value.endYear
    bookSourceInfo.value.pages = selectedCitation.value.pages
    sourceInfo.value.contributers = selectedCitation.value.authors
    return
  }

  if(sourceType.value.id === 2) {
    sourceInfo.value.title = selectedCitation.value.title
    sourceInfo.value.date = selectedCitation.value.date + selectedCitation.value.endYear
    webSourceInfo.value.doi = selectedCitation.value.doi
    sourceInfo.value.contributers = selectedCitation.value.authors
    webSourceInfo.value.retrieved = new Date().getDate().toLocaleString()
    return
  }
}

const createBibliography = async () => {
  if (userStore.loggedIn) {

  const bibliography = {
    label: newBibLabel.value,
    citations: []
  }
  const retBibliography = await userapi.post('/bibliographies/bibliography/'+userStore.user.auth_id, bibliography);
  userStore.setBibliographies(retBibliography.data)
  }
}

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

<style scoped lang="scss"></style>
