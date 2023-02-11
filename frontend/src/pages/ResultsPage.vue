<template>

    <div class="row justify-center q-ma-lg">
      <div class="col-6 off-center">

        <q-form  @submit="onSubmit">
          <q-input outlined v-model="queryLine[0].queryText" label="Search For Articles">
            <template v-slot:append>
              <q-icon name="search" color="primary" />
            </template>
          </q-input>
          <GlobalDialog type="AdvancedSearch" label="Advanced Search"></GlobalDialog>
        </q-form>

      </div>
    </div>

  <!-- <q-page class="col items-center justify-evenly"> -->
    <!-- <q-list padding class="rounded-borders" style="max-width: 950px"> -->

    <!-- <div style="justify-content: space-evenly"> -->

      <!-- <q-item v-for="n in 10" :key="n" style="max-width: 50%; justify-content: center;"> -->
      <div class="row" v-for="n in 7" :key="n" style="justify-content: center;">

        <div class="col-5">
          <q-item-label class="text-h6" lines="1">
            <span class="text-weight-medium"> Creelman, Valerie </span>
            <q-icon clickable name="bookmark_add"/>
          </q-item-label>
          <span class="text-blue-7 text-h6"> 10.1177/1080569912443081 </span>
          <p ellipsis no-wrap class="text-body2"> Negotiating Ethos: An Army Corps of Engineers Resource Manager Persuades a Commmunity to Protect a Recreational Lake Area </p>
        </div>

        <div class=items-center style="align-items: center">
          <q-btn label="Add Citation" type="submit" color="primary"/>
        </div>

      </div>
      <!-- </q-item> -->

    <!-- </q-list> -->

  <div class="q-pa-lg flex flex-center">
    <q-pagination
      v-model="current"
      :max="5"
      direction-links
    />
  </div>


  <!-- </q-page> -->

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

<style scoped lang="scss"></style>
