<!-- eslint-disable vue/no-parsing-error -->
<template>
  <q-page class="row items-center justify-evenly">
    <q-card class="form-width" dark>
      <q-card-section class="bg-secondary" >
        <q-form @submit="onSubmit">
          <div class="row">
            <div class="col-12" style="padding-top: 10px; padding-bottom: 10px;">
              <q-input outlined dark label-color="white" v-model="payload.title" label="Article Title"></q-input>
            </div>
            <div class="col-12" style="padding-top: 10px; padding-bottom: 10px;">
              <q-input outlined dark label-color="white" v-model="payload.authors" label="Article Authors"></q-input>
            </div>
            <div class="col-12" style="padding-top: 10px; padding-bottom: 10px;">
              <q-input outlined dark label-color="white" v-model="payload.sourceLong" label="Article Source Long"></q-input>
            </div>
            <div class="col-12" style="padding-top: 10px; padding-bottom: 10px;">
              <q-input outlined dark label-color="white" v-model="payload.sourceAbbrev" label="Article Source Abbreviated"></q-input>
            </div>
            <div class="col-12" style="padding-top: 10px; padding-bottom: 10px;">
              <q-input outlined dark label-color="white" v-model="payload.pages" label="Pages"></q-input>
            </div>
          </div>
          <q-uploader
            style="width: 100%"
            label="Upload File"
            v-model="file"
            :multiple="false"
            dark
          ></q-uploader>
          <q-btn type="submit" label="Upload" class="bg-white full-width" text-color="accent" style="margin-top: 20px"></q-btn>
        </q-form>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import { AxiosInstance } from 'axios';
import AuthService from 'src/auth/AuthService';
import useUserStore from 'src/auth/userStore';
import { inject, onMounted, ref } from 'vue';
const searchapi: AxiosInstance = inject('searchapi') as AxiosInstance;

const placeholder = ref('')
const token = ref('')
const file = ref('')
const userStore = useUserStore()
const payload = ref({
  title: "",
  authors: "",
  sourceAbbrev: "",
  sourceLong: "",
  pages: "",
})


onMounted( async () => {
  if (userStore.loggedIn) {
    const tokenVal = await AuthService.AuthWrapper.GetAuthToken();
    token.value = tokenVal
  }
})

const onSubmit = () => {
  // const payloadBody = payload.value
  let formData = new FormData()

  if(file.value !== null){
    // searchapi.defaults.headers.post['Content-Type'] = 'multipart/form-data'
    // searchapi.defaults.params = {

    // }
    formData.append('file', file.value)
    searchapi.post('/admin/addArticle', formData )
    // searchapi.defaults.headers.post['Content-Type'] = 'application/json'
  }


  console.log('posted')
  // searchapi.post()
}

</script>

<style scoped lang="scss"></style>
