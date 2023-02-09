<template>
  <q-page>
    <div class="header-actions">
      <div class="header-container row">
        <div class="col-12">
          <h3>Welcome!</h3>
        </div>
        <div class="col-6 center-col">
          <q-btn label="Login" class="bg-primary" text-color="white" size="lg"></q-btn>
        </div>
        <div class="col-6 center-col">
          <q-btn label="Search as Guest" class="bg-secondary" text-color="white" size="lg" @click="guestSearchHandler"></q-btn>
        </div>
      </div>
    </div>
    <div class="ribbon bg-bl-40">
      <div class="ribbon-content">
        Welcome to the free WSU Technical Communication Search service, brought to you by Washington State University and Murphy Editing and Writing Services.
        We invite you to enrich your research by finding material dating from 2000 onward.
        The database is continually refreshed, so be sure to check back frequently.
      </div>
    </div>
    <div class="ribbon ribbon-reverse bg-bl-70">
      <div class="ribbon-content">
        You have available several thousand citations of non-book resources of interest to technical communicators, all searchable in multiple ways.
        Our focus lies on high-quality research, relevance to the needs of researchers in our field, original contribution, and permanent value.
        We have no interest in collecting everything that comes to our attention. We generally do not include such things as journal editors'
        introductions to their authors, recaps of what other articles have printed, daily news items, miscellaneous web pages, or retracted articles.
      </div>
    </div>
    <div class="ribbon bg-bl-40">
      <div class="ribbon-content">
        You will find here articles published in the major research journals and websites in the field.
        We attempt to include all relevant articles, for example, from IEEE Transactions on Professional Communication,
        Information Design Journal, Journal of Business and Technical Communication,
        Journal of Technical Writing and Communication, NNgroup.com (Jakob Nielsen and Donald Norman), Rhetoric Review,
        Technical Communication, Technical Communication Quarterly, and Written Communication. <a href="">See our complete list of sources.</a>
      </div>
    </div>
    <div class="ribbon bg-primary feedback-ribbon row">
      <div class="row" style="width: 50% !important">
        <div class="col-12" style="padding-bottom: 25px">
          <h6 style="color: white;">Overall, how happy are you with the software?</h6>
        </div>
        <div class="col-1 offset-1 happiness-indicator">
          <span class="i-span" value="1">1</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="2">2</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="3">3</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="4">4</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="5">5</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="6">6</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="7">7</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="8">8</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="9">9</span>
        </div>
        <div class="col-1 happiness-indicator">
          <span class="i-span" value="10">10</span>
        </div>
        <div class="col-6" style="padding-bottom: 25px">
          <h6 style="color: white;">Send us your comments and suggestions</h6>
          <q-input v-model="email" label="Email" label-color="white" color="white" filled dark/>
        </div>
        <div class="col-12" style="padding-bottom: 25px">
          <q-input v-model="suggestion" label="Comments and suggestions" filled type="textarea" label-color="white" color="white" dark/>
        </div>
        <div class="col-12" style="display: flex; justify-content: end;">
          <q-btn label="Send Feedback" text-color="white" class="bg-secondary" ></q-btn>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import {useRouter } from 'vue-router';
const router = useRouter();
const email = ref('');
const suggestion = ref('');

const guestSearchHandler = () => {
  router.push('/search')
}
</script>

<style lang="scss" scoped>
.bg-bl-30 {
  background-color: $bl-30
}

.bg-bl-40 {
  background-color: $bl-40
}

.bg-bl-70 {
  background-color: $bl-70
}

.ribbon {
  height: 150px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.ribbon-reverse {
  height: 150px;
  width: 100%;
  color: $bl-30;
  display: flex;
  justify-content: center;
  align-items: center;
}

.center-col {
  justify-content: center;
  display: flex;
}

.ribbon-content {
  width: 50%;
  text-align: start;
}

.header-actions {
  height: 200px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 20px;
  background-color: $bl-30;
}
.header-container {
  width: 50%;
}
.feedback-ribbon {
  height: 800px !important;
}

.happiness-indicator {
  height: 80px;
  background-color: $bl-70;
  border-style: solid;
  border-color: $bl-30;
  border-width: 2px;
  text-align: center;
  line-height: 80px;
  .i-span {
    font-size: x-large;
    display: inline-block;
    vertical-align: middle;
    line-height: normal;
    color: $bl-30;
  }
}

.active {
  background-color: $bl-60;
}
</style>
