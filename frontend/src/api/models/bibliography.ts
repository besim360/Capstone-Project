export type Person = {
  firstName: string;
  lastName: string;
  middleInitial: string;
}

export type CitationRecord = {
  id: string;
  title: string;
  authors: string;
  volNum: string;
  date: string;
  startYear: string;
  endYear: string;
  pages: string;
  doi: string;
  author: Person;
  contributers: string;
  version: string;
  number: string;
  publicationLocale: string;
  format: string;
  accessDate: string;
  publisher: string;
  type: string;
  chapter: string;
  editors: string;
  translators: string;
  fullString: string;
}

export type Bibliography = {
  id: string;
  label: string;
  citations: Array<CitationRecord>
}

export type UserBibliographies = {
  id: string;
  uid: string;
  bibliographies: Array<Bibliography>
}
