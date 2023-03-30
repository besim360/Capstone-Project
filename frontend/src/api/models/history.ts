import { SearchRecord } from './search';


export type HistoryRecord = {
  id: string,
  uid: string,
  query: string,
  queryDate: string,
  results: Array<SearchRecord>
}

export type SearchHistory = Array<HistoryRecord>

