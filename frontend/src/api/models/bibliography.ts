export type Person = {
  firstName: string;
  lastName: string;
  middleInitials: string;
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
  contributers: Array<Person>
  version: string;
  number: string;
  publicationLocale: string;
  format: string;
  accessDate: string;
  publisher: string;
  type: string;
  chapter: string;
  editors: Array<Person>
  translators: Array<Person>
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
