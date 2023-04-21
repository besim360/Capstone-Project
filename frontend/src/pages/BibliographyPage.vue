<template>
  <q-page>
    <div class="header-actions">
      <div class="header-container row">
        <div class="col-12">
          <h3>Bibliography</h3>
        </div>
        <div class="col-4 center-col">
          <q-select v-model="exportType" :options="exportTypes" label="Export Style" dense rounded outlined></q-select>
        </div>
        <div class="col-4 offset-4 center-col">
          <q-btn rounded label="Generate Reference List" class="bg-primary" text-color="white" size="md" @click="generateBibliography"></q-btn>
        </div>
      </div>
    </div>
    <div class="content-body" id="conversion-element">
      <div class="row content-container">
        <div :key="element._id" v-for="element in samples" class="col-12">
          <section v-if="exportType.value === 'Chicago'" class="chicago-indention">
            <div v-if="element.details.type === 'Book'">
              <p>
                {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}</span>.
                <span>{{element.details.date.getFullYear()}}.</span>
                <span v-if="element.details.chapter">
                  <span>{{" \"" + element.details.chapter}}."</span>
                  <span> In <i>{{element.titleJBW.title}},</i></span>
                </span>
                <span v-else><i>{{" " + element.titleJBW.title}},</i></span>
                <span v-if="(element.details.editors)"> edited by
                  <span :key="editor._id" v-for="editor in element.details.editors">{{editor.firstName}}
                    <span v-if="editor.middleInitial">{{editor.middleInitial}},</span>
                    {{editor.lastName}},
                  </span>
                </span>
                <span v-if="(element.details.translators)"> translated by
                  <span :key="translator._id" v-for="translator in element.details.translators">{{translator.firstName}}
                    <span v-if="translator.middleInitial">{{translator.middleInitial}},</span>
                    {{translator.lastName}},
                  </span>
                </span>
                <span>{{element.details.pages}}.</span>
                <span>{{" " + element.details.publicationlocale}}: {{element.details.publisher}}</span>
              </p>
            </div>
            <div v-else-if="element.details.type === 'Journal'">
              <p>
                {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}.</span>,
                <span :key="contrib._id" v-for="contrib in element.otherContribs.contributers">{{contrib.firstName}}, <span v-if="contrib.middleInitial">{{" "+ contrib.middleInitial}}.</span>{{contrib.lastName}},</span>
                <span>{{element.details.date.getFullYear()+". "}}</span>
                <span>"{{element.details.chapter}}." In <i>{{element.titleJBW.title}},</i></span>

                <span>{{element.details.version}},</span>
                <span v-if="element.details.version">{{element.details.number}} ({{(element.details.date.toLocaleString('en-US', {month: 'long'}))}}):</span>
                <span>{{" "+element.details.pages}}.</span>
                <span v-if="element.details.urlDOI">{{" "+element.details.urlDOI}}.</span>
              </p>
            </div>
            <div v-else-if="element.details.type === 'Webpage'">
              <p>
                <span>{{element.author.name}}.</span>
                <span>{{" " + element.details.date.getFullYear()+". "}}</span>
                <span v-if="element.details.chapter">"{{element.details.chapter}}."</span>
                <span>{{" \"" + element.titleJBW.title}}".</span>
                <span>{{" Last Modified " + element.details.accessDate.toLocaleString('en-US', {month: 'long', day: '2-digit', year: 'numeric'})}}.</span>
                <span>{{" "+element.details.urlDOI}}.</span>
              </p>
            </div>
          </section>
          <section v-if="exportType.value === 'MLA'">
            <div v-if="element.details.type === 'Book'">
              <p>
                {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}</span>.

                <span v-if="element.details.chapter">
                  <span>{{" \"" + element.details.chapter}}."</span>
                  <span><i>{{" " + element.titleJBW.title}},</i></span>
                </span>
                <span v-else><i>{{" " + element.titleJBW.title}},</i></span>
                <span v-if="(element.details.editors)"> edited by
                  <span :key="editor._id" v-for="editor in element.details.editors">{{editor.firstName}}
                    <span v-if="editor.middleInitial">{{editor.middleInitial}},</span>
                    {{editor.lastName}} et al.,
                  </span>
                </span>
                <span v-if="(element.details.translators)"> translated by
                  <span :key="translator._id" v-for="translator in element.details.translators">{{translator.firstName}}
                    <span v-if="translator.middleInitial">{{translator.middleInitial}},</span>
                    {{translator.lastName}} et al.,
                  </span>
                </span>
                <span>{{" " + element.details.publisher}}, </span>
                <span>{{" " + element.details.date.getFullYear() + ". "}}</span>

                <span>pp. {{element.details.pages}}.</span>
              </p>
            </div>
            <div v-else-if="element.details.type === 'Journal'">
              <p>
                {{element.author.lastName}}, {{element.author.firstName}}<span v-if="element.author.middleInitial">{{" " + element.author.middleInitial}}.</span>,
                <span :key="contrib._id" v-for="contrib in element.otherContribs.contributers">{{contrib.firstName}}, <span v-if="contrib.middleInitial">{{" "+ contrib.middleInitial}}.</span>{{contrib.lastName}},</span>
                <span>"{{element.details.chapter}}."<i>{{" "+element.titleJBW.title}},</i></span>
                <span>{{" vol."+element.details.version}},</span>
                <span v-if="element.details.version">{{" no. " + element.details.number}}, {{(element.details.date.toLocaleString('en-US', {month: 'short', year: 'numeric'}))}}.</span>
                <span>{{" pp. "+element.details.pages}}.</span>
                <span><i>{{(" "+ element.details.publisher)}}</i></span>
                <span v-if="element.details.urlDOI">{{" "+element.details.urlDOI}}.</span>
              </p>
            </div>
            <div v-else-if="element.details.type === 'Webpage'">
              <p>
                <span>{{element.author.name}}.</span>
                <span v-if="element.details.chapter">"{{element.details.chapter}}."</span>
                <span><i>{{" " + element.titleJBW.title}},</i></span>
                <span>{{" " + element.details.accessDate.toLocaleString('en-US', {month: 'short', year: 'numeric'})}}.</span>
                <span>{{" "+element.details.urlDOI}}.</span>
              </p>
            </div>
          </section>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import html2pdf from 'html2pdf.js';
import {BibliographyElement, sampleBibliography} from '../api/models/bibliography_old'
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

const samples = ref<Array<BibliographyElement>>(sampleBibliography)


const generateBibliography = () => {
  html2pdf(document.getElementById('conversion-element'), {
    margin: [0, -1],
  	filename: 'sample.pdf',
  });
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
</style>
