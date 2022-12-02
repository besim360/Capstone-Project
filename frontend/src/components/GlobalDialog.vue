<template>
  <q-btn :label="props.label" @click="dialogDisplayHandler" class="bg-secondary" text-color="white"></q-btn>
  <q-dialog v-model="store.active" persistent transition-show="scale" transition-hide="scale">
    <q-card class="form-width">
      <q-card-section v-if="store.activeFType === 'AdvancedSearch'" class="bg-secondary advanced-search-form">
        <q-form class="row">
          <div class="col-12" style="padding: 10px">
            <q-input outlined dark label-color="white" label="Article Title"></q-input>
          </div>
          <div class="col-12" style="padding: 10px">
            <q-input outlined dark label-color="white" label="Subject Tags"></q-input>
          </div>
          <div v-for="author in authors" :key="author.id" class="row" style="width: 100%">
            <div class="col-5" style="padding: 10px">
              <q-input outlined dark label-color="white" label="Author First Name"></q-input>
            </div>
            <div class="col-2" style="padding: 10px">
              <q-input outlined dark label-color="white" label="MI"></q-input>
            </div>
            <div class="col-5" style="padding: 10px">
              <q-input outlined dark label-color="white" label="Author Last Name"></q-input>
            </div>
          </div>
          <div class="col-12" style="justify-content: end; display: flex;">
            <q-btn flat label="+ Add Author" dark text-color="info" @click="addAuthor"></q-btn>
          </div>
          <div class="col-12" style="padding: 10px">
            <q-input outlined dark label-color="white" label="DOI"></q-input>
          </div>
          <div class="col-6" style="padding: 10px">
            <q-input outlined dark label-color="white" label="Subject Tags"></q-input>
          </div>
          <div class="col-6" style="padding: 10px">
            <q-input outlined dark label-color="white" label="Subject Tags"></q-input>
          </div>
          <div class="col-12" style="padding: 10px">
            <q-input outlined dark label-color="white" label="Article Source"></q-input>
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
        <q-btn :label="dialogCloseLabel" class="bg-white full-width" text-color="accent" @click="dialogCloseHandler"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import { Author, useDialogStore } from 'src/stores/dialog-forms';
import { useRouter } from 'vue-router';
const props = defineProps<{
  type: string
  label: string
}>()

const store = useDialogStore();

const router = useRouter();
const setAuthor = {} as Author;
const authors = ref([setAuthor] as Array<Author>);

onMounted(() => {
  store.setAuthors(authors.value)
})

const dialogDisplayHandler = () => {
  loadFormType(props.type);
  store.showForm();
}

const addAuthor = () => {
  store.addAuthorToAdvancedSearch({} as Author);
  authors.value = store.fAdvSearchOptions.articleAuthors;
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
  router.push('/results');
}

</script>
<stlye lang="scss" scoped>
.advanced-search-form {
  min-height: 450px;
}

.form-width {
  min-width: 700px;
}
</stlye>
