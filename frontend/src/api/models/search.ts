export type Subject = {
  subjectCode: string,
  generalTopic: string,
  topics: string,
}

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
  subjects: Array<Subject>
  doi: string,
}

export type SearchResults = Array<SearchRecord>
