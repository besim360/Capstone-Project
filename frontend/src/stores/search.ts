import { defineStore } from 'pinia';
import { SearchHistory } from 'src/api/models/history';
import { SearchResults } from 'src/api/models/search';

export interface TextField {
  category: 'Subject' | 'Author' | 'Title' | 'Source' | 'Year' | 'DOI' | 'Date Range' | 'All';
  queryText: string;
  logic: 'AND' | 'OR' | 'NOT' | 'NA'
}

const initialState = {
  category: 'All',
  queryText: '',
  logic: 'NA'
} as TextField


export const useSearchStore = defineStore('search', {
  state: () => ({
    queryLine: {0: initialState} as Record<number, TextField>,
    results: [] as SearchResults
  }),
  getters: {},
  actions: {
    updateQuery(queryline: number, queryText: string, category: 'Subject' | 'Author' | 'Title' | 'Source' | 'Year' | 'DOI' | 'Date Range' | 'All', logic: 'AND' | 'OR' | 'NOT' | 'NA') {
      const state = {
        category: category,
        logic: logic,
        queryText: queryText,
      }
      this.queryLine[queryline] = state;
    },

    addNewQueryLine() {
      const keys = (Object.keys(this.queryLine))
      const numKeys = keys.map(Number)
      const max = Math.max(...numKeys)
      this.queryLine[max+1] = {
        category: 'All',
        queryText: '',
        logic: 'AND'
      } as TextField
    },

    clearQuery() {
      this.queryLine = {0: initialState}
      this.results = [] as SearchResults
    },

    clearQueryText() {
      this.queryLine = {0: initialState}
    },

    resetQuery() {
      const duplicateFirstEntry = this.queryLine[0]
      const resetState = {0: duplicateFirstEntry} as Record<number, TextField>
      this.queryLine = resetState
    },

    setResults(searchResults: SearchResults) {
      this.results = searchResults
    },
  },
})
