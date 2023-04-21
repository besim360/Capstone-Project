import { Subject } from './search';

export type BookmarkRecord = {
  id: string,
  label: string,
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

export type Bookmark = {
  id: string,
  label: string,
  bookmarks: Array<BookmarkRecord>
}

export type UserBookmarks = {
  id: string,
  uid: string,
  bookmarkFolders: Array<Bookmark>
}
