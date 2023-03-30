export type SearchRecord = {
  id: string,
  title: string,
  authors: string,
  sourceAbbrev: string,
  sourceLong: string,
  volNum: string,
  date: string,
  startYear: string,
  endYear: string,
  pages: string,
  subjectCodes: string,
  topics: string,
  doi: string,
}

export type SearchResults = Array<SearchRecord>
