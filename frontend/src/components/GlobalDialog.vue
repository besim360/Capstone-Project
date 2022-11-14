<template>
  <q-btn :label="props.label" @click="dialogDisplayHandler"></q-btn>
  <q-dialog v-model="store.active" persistent transition-show="scale" transition-hide="scale">
    <q-card>
      <q-card-actions>
        <q-btn :label="dialogCloseLabel" v-close-popup/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>
<script lang="ts" setup>
import { computed } from 'vue';
import { useDialogStore } from 'src/stores/dialog-forms';
const props = defineProps<{
  type: string
  label: string
}>()

const store = useDialogStore();

const dialogDisplayHandler = () => {
  loadFormType(props.type);
  store.showForm();
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

</script>
<stlye lang="scss" scoped></stlye>
