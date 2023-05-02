<template>
  <q-page>
    <div class="header-actions q-mb-xl">
      <div class="header-container row">
        <div class="col-12">
          <h3>Bibliography</h3>
        </div>
        <div class="col-2 center-col">
          <q-select v-model="exportType" :options="exportTypes" label="Export Style" dense rounded :disable="noBibliographies || selectedBibliography == null" outlined></q-select>
        </div>
        <div class="col-3 offset-1 center-col">
          <q-select v-model="selectedBibliography" :options="userStore.bibliographies.bibliographies" label="Select Bibliography" dense rounded :disable="noBibliographies" outlined></q-select>
        </div>
        <div class="col-3 offset-1 center-col">
          <q-btn rounded label="Generate Reference List" class="bg-primary" text-color="white" size="md" :disable="noBibliographies  || selectedBibliography == null" @click="generateBibliography"></q-btn>
        </div>
        <div class="col-1 offset-1 center-col">
          <q-btn rounded label="Edit" class="bg-primary" text-color="white" size="md" :disable="noBibliographies  || selectedBibliography == null"  @click="edit"></q-btn>
        </div>
      </div>
    </div>
    <div class="content-body" id="conversion-element">
      <div class="row content-container">
        <div class="col-12 justify-center" v-if="noBibliographies">
          <section>
            <h4>Oops!</h4>
            <p> It looks like you have no bibliographies.</p>
            <p> Would you like to add one?</p>
            <q-btn rounded label="Create Bibliography" class="bg-primary" text-color="white" size="md" @click="createBib = true" />
            <q-dialog v-model="createBib" persistent >
              <q-card style="min-width: 350px;">
                <q-card-section>
                  <div class="text-h6">Bibliography Tag</div>
                </q-card-section>
                <q-card-section class="q-pt-none">
                  <q-input dense v-model="newBibLabel" autofocus @keyup.enter="createBib = false"></q-input>
                </q-card-section>
                <q-card-actions>
                  <q-btn flat label="Cancel" v-close-popup></q-btn>
                  <q-space></q-space>
                  <q-btn label="Create" class="bg-primary" text-color="white" size="md" @click="createBibliography" v-close-popup></q-btn>
                </q-card-actions>
              </q-card>
            </q-dialog>
          </section>
        </div>
        <div v-else style="width: 100%">
          <div :key="element.id" v-for="element in selectedBibliography?.citations" class="row">
            <section v-if="exportType.value === 'Chicago'" class="chicago-indention col-11">
              <div v-if="element.type === 'Book'">
                <p v-if="!element.fullString">
                  {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}</span>.
                  <span>{{element.date}}.</span>
                  <span v-if="element.chapter">
                    <span>{{" \"" + element.chapter}}."</span>
                    <span> In <i>{{element.title}},</i></span>
                  </span>
                  <span v-else><i>{{" " + element.title}},</i></span>
                  <span v-if="(element.editors)"> edited by
                    <span>
                      {{ element.editors }}
                    </span>
                  </span>
                  <span v-if="(element.translators)"> translated by
                    <span>
                      {{ element.translators }}
                    </span>
                  </span>
                  <span v-if="element.pages !== 'N/A'">{{element.pages}}.</span>
                  <span v-if="element.publicationLocale !== 'N/A' && element.publisher !=='N/A'">{{" " + element.publicationLocale }}: {{element.publisher}}</span>
                </p>
                <div v-else-if="editMode">
                  <q-input v-model="element.fullString" dense></q-input>
                </div>
                <p v-else>
                  {{ element.fullString }}
                </p>
              </div>
              <div v-else-if="element.type === 'Journal'">
                <p v-if="!element.fullString && !editMode">
                  {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}.</span>,
                  <span v-if="(element.contributers)">
                    <span>
                      {{ element.contributers }}
                    </span>
                  </span>
                  <span>{{element.date+". "}}</span>
                  <span><span v-if="element.chapter !== ''">"{{element.chapter}}."</span> In <i>{{element.title}},</i></span>

                  <span v-if="element.version !== ''">{{element.version}},</span>
                  <span v-if="element.version && element.version !== 'N/A'">{{element.number}} ({{element.date}}):</span>
                  <span><span v-if="element.pages!=='' && element.pages !== 'N/A'">{{" "+element.pages}}</span>.</span>
                  <span v-if="element.doi && element.doi !== 'N/A'">{{" "+element.doi}}.</span>
                </p>
                <div v-else-if="editMode" style="width: 100%">
                  <q-input v-model="element.fullString" dense></q-input>
                </div>
                <p v-else>
                  {{ element.fullString }}
                </p>
              </div>
              <div v-else-if="element.type === 'Web'">
                <p v-if="!element.fullString && !editMode">
                  <span>{{element.author.firstName}}.</span>
                  <span>{{" " + element.date+". "}}</span>
                  <span v-if="element.chapter">"{{element.chapter}}."</span>
                  <span>{{" \"" + element.title}}".</span>
                  <span>{{" Last Modified " + element.endYear}}.</span>
                  <span>{{" "+element.doi}}.</span>
                </p>
                <p v-else-if="editMode">
                  <q-input v-model="element.fullString" dense></q-input>
                </p>
                <p v-else>
                  {{ element.fullString }}
                </p>
              </div>
            </section>

            <section v-if="exportType.value === 'MLA'" class="col-11">
              <div v-if="element.type === 'Book'">
                <p v-if="!element.fullString && !editMode">
                  {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}</span>.

                  <span v-if="element.chapter">
                    <span>{{" \"" + element.chapter}}."</span>
                    <span><i>{{" " + element.title}},</i></span>
                  </span>
                  <span v-else><i>{{" " + element.title}},</i></span>
                  <span v-if="(element.editors)"> edited by
                    <span>
                      {{ element.editors }}
                    </span>
                  </span>
                  <span v-if="(element.translators)"> translated by
                    <span>
                      {{ element.translators }}
                    </span>
                  </span>
                  <span>{{" " + element.publisher}}, </span>
                  <span>{{" " + element.date + ". "}}</span>

                  <span>pp. {{element.pages}}.</span>
                </p>
                <div v-else-if="editMode" style="width: 100%">
                  <q-input v-model="element.fullString" dense></q-input>
                </div>
                <p v-else>
                  {{ element.fullString }}
                </p>
              </div>
              <div v-else-if="element.type === 'Journal'">
                <p v-if="!element.fullString && !editMode">
                  {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}.</span>,
                  <span v-if="(element.contributers)">
                    <span>
                      {{ element.contributers }}
                    </span>
                  </span>
                  <span>"{{element.chapter}}."<i>{{" "+element.title}},</i></span>
                  <span>{{" vol."+element.version}},</span>
                  <span v-if="element.version">{{" no. " + element.number}}, {{element.date}}.</span>
                  <span>{{" pp. "+element.pages}}.</span>
                  <span><i>{{(" "+ element.publisher)}}</i></span>
                  <span v-if="element.doi && element.doi !== 'N/A'">{{" "+element.doi}}.</span>
                </p>
                <div v-else-if="editMode" style="width: 100%">
                  <q-input v-model="element.fullString" dense></q-input>
                </div>
                <p v-else>
                  {{ element.fullString }}
                </p>
              </div>
              <div v-else-if="element.type === 'Web'">
                <p v-if="!element.fullString && !editMode">
                  <span>{{element.author.firstName}}.</span>
                  <span v-if="element.chapter">"{{element.chapter}}."</span>
                  <span><i>{{" " + element.title}},</i></span>
                  <span>{{" Last Modified " + element.endYear}}.</span>
                  <span>{{" "+element.doi}}.</span>
                </p>
                <div v-else-if="editMode" style="width: 100%">
                  <q-input v-model="element.fullString" dense></q-input>
                </div>
                <p v-else>
                  {{ element.fullString }}
                </p>
              </div>
            </section>
            <div class="col-1">
              <q-btn flat round icon="delete" class="q-mr-xs-md" v-if="editMode" @click="deleteCitation(element)"></q-btn>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="header-actions">
      <div class="header-container row">
        <div class="col-12">
          <q-btn rounded label="Create New Bibliography" class="bg-primary" text-color="white" size="md" @click="createBib = true" />
          <q-dialog v-model="createBib" persistent >
            <q-card style="min-width: 350px;">
              <q-card-section>
                <div class="text-h6">Bibliography Tag</div>
              </q-card-section>
              <q-card-section class="q-pt-none">
                <q-input dense v-model="newBibLabel" autofocus @keyup.enter="createBib = false"></q-input>
              </q-card-section>
              <q-card-actions>
                <q-btn flat label="Cancel" v-close-popup></q-btn>
                <q-space></q-space>
                <q-btn label="Create" class="bg-primary" text-color="white" size="md" @click="createBibliography" v-close-popup></q-btn>
              </q-card-actions>
            </q-card>
          </q-dialog>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { computed, inject, onMounted, ref } from 'vue';
import html2pdf from 'html2pdf.js';
import {BibliographyElement, sampleBibliography} from '../api/models/bibliography_old'
import useUserStore from 'src/auth/userStore';
import { AxiosInstance } from 'axios';
import { Bibliography, CitationRecord } from 'src/api/models/bibliography';

const userapi: AxiosInstance = inject('userapi') as AxiosInstance;


const userStore = useUserStore()
type ExportType = {
  _id: number;
  name: string;
  label: string;
  value: string;
}

const defaultTypes: Array<ExportType> = [
  {_id: 0, name: 'Chicago/Turbian', label: 'Chicago', value: 'Chicago'},
  {_id: 1, name: 'Modern Langauge Association', label: 'MLA', value: 'MLA'},
  // {_id: 2, name: 'American Psychological Association', label: 'APA', value: 'APA'}
];

const exportType = ref(defaultTypes[0]);
const exportTypes = ref<Array<ExportType>>(defaultTypes);

const noBibliographies = computed(() => {
  return userStore.bibliographies.bibliographies.length === 0;
})

const editMode = ref(false);
const newBibLabel = ref('');
const createBib = ref(false);
const selectedBibliography = ref<Bibliography | null>(null)
// const samples = ref<Array<BibliographyElement>>(sampleBibliography)

onMounted( async () => {
  if(selectedBibliography.value === null){
    if(userStore.bibliographies.bibliographies[0] !== null) {
      selectedBibliography.value = userStore.bibliographies.bibliographies[0];
    }
  }
 })

const outStrings = computed(() => {
  // selectedBibliography?.citations
  if(!selectedBibliography.value) {
    return []
  }

  let fullText: string[] = []
  let stringCitation = ""
  let citation: any;
  for(let i = 0; i < selectedBibliography.value.citations.length; i++) {
    citation = selectedBibliography.value.citations[i]
    console.log(citation)
    if(citation.type === 'Journal') {
      stringCitation = `${citation.author.lastName}, ${citation.author.firstName}, ${citation.author.middleInitial !== '' ? ' '+citation.author.middleInitial + '., ' : ''}${citation.contributors !== '' ? citation.contributors : ''} ${citation.date}. ${citation.chapter !== '' && citation.chapter !== 'N/A' ? citation.chapter + ' In' + citation.title : citation.title} ${citation.version !== '' && citation.version!=='N/A' ? citation.version+'. '+citation.number + ' ' +citation.date + ':' : ''} ${citation.pages !== '' && citation.pages !== 'N/A' ? citation.pages : ''}. ${citation.doi !== '' && citation.doi !== 'N/A' ? citation.doi : ''}`
      fullText.push(stringCitation)
    }

    if(citation.type === 'Book') {
      stringCitation = `${citation.author.lastName}, ${citation.author.firstName}${citation.author.middleInitial ? ' ' + citation.author.middleInitial : ''}.${citation.date}. ${citation.chapter != '' ? '"' + citation.chapter + ' In ': ''}${citation.title}, ${citation.editors !== '' ? 'edited by '+ citation.editors + ', ' : ''}${citation.translators !== '' ? 'translated by ' + citation.translators + ', ' : ''}${citation.pages !== '' && citation.pages !== 'N/A' ? citation.pages + ' ' : ''}${citation.publicationLocale !== 'N/A' && citation.publisher !== 'N/A' ? citation.publicationLocale + ': ' + citation.publisher : ''}`
      fullText.push(stringCitation)
    }

    if(citation.type === 'Web') {
      stringCitation = `${citation}`
    }
  }

  return fullText
})

const generateBibliography = () => {
  html2pdf(document.getElementById('conversion-element'), {
    margin: [0, -1],
  	filename: 'sample.pdf',
  });
}

const edit = async () => {
  editMode.value = !editMode.value
  if(editMode.value) {
    if(!selectedBibliography.value) {
      return
    }
    let citation: any;

    for(let i = 0; i < outStrings.value.length; i++ ) {
      citation = selectedBibliography.value.citations[i]
      if(citation.type === 'Journal' && !citation.fullString) {
        selectedBibliography.value.citations[i].fullString = `${citation.author.lastName}, ${citation.author.firstName}, ${citation.author.middleInitial !== '' ? ' '+citation.author.middleInitial + '., ' : ''}${citation.contributors !== '' ? citation.contributors : ''} ${citation.date}. ${citation.chapter !== '' && citation.chapter !== 'N/A' ? citation.chapter + ' In' + citation.title : citation.title} ${citation.version !== '' && citation.version!=='N/A' ? citation.version+'. '+citation.number + ' ' +citation.date + ':' : ''} ${citation.pages !== '' && citation.pages !== 'N/A' ? citation.pages : ''}. ${citation.doi !== '' && citation.doi !== 'N/A' ? citation.doi : ''}`;
      }
      if(citation.type === 'Book' && !citation.fullString) {
        selectedBibliography.value.citations[i].fullString = `${citation}`
      }
      if(citation.type === 'Web') {
        stringCitation = `${citation.author.firstName ? citation.author.firstName : 'No Author'}. ${citation.chapter !== '' ? citation.chapter + '. ' : ''}${citation.title}, Last Modified ${citation.endYear}. ${citation.doi !== '' && citation.doi !== 'N/A' ? citation.doi + '.' : ''}`
      }
    }
  } else {
    await handleLargeDispatch()
    const newBib = await userapi.get('/bibliographies/'+userStore.user.auth_id)
    userStore.setBibliographies(newBib.data)
  }
}

const deleteCitation = async (citation: any) => {
  if(editMode.value) {
    if(!selectedBibliography.value) {
      return
    }
    await userapi.delete(`/bibliographies/bibliography/${citation.id == undefined ? citation._id : citation.id}/${selectedBibliography.value.id == undefined ? selectedBibliography.value._id : selectedBibliography.value.id}/${userStore.user.auth_id}`)
    selectedBibliography.value.citations = selectedBibliography.value.citations.filter((x) => x.id !== citation._id && x.id !== citation.id)
    editMode.value = false
  }

  const newBib = await userapi.get('/bibliographies/'+userStore.user.auth_id)
  userStore.setBibliographies(newBib.data)
}

const handleLargeDispatch = async () => {
  if(!selectedBibliography.value) {
      return
  }
  for(let i = 0; i < outStrings.value.length; i++) {
    userapi.put(`/bibliographies/bibliography/citation/${selectedBibliography.value.citations[i].id == undefined ? selectedBibliography.value.citations[i]._id : selectedBibliography.value.citations[i].id}/${selectedBibliography.value.id == undefined ? selectedBibliography.value._id : selectedBibliography.value.id}/${userStore.user.auth_id}`, selectedBibliography.value.citations[i])
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
</script>

<style scoped lang="scss">
.header-container {
  width: 50%;
}

.content-body {
  display: flex;
  justify-content: center;
  align-items: center;
}

.content-container {
  width: 50%;
  display: flex;
  flex-direction: start;
  align-items: center;
}

.header-actions {
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 20px;
}

.chicago-indention {
  text-indent: -36px;
  padding-left: 36px;
}

.justify-center {
  justify-content: center;
  display: flex;
}
</style>
