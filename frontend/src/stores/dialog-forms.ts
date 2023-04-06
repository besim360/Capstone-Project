import { defineStore } from 'pinia';
import { Bookmark } from 'src/api/models/bookmark';
import { SearchRecord } from 'src/api/models/search';
export interface Author {
  id: number;
  firstName: string;
  lastName: string;
  middleInitial: string;
}

export interface AdvancedSearchForm {
  articleTitle: string;
  articleSubjectTags: string;
  articleAuthors: Array<Author>;
  articleDOI: string;
  dateStart: Date;
  dateEnd: Date;
  source: string;
}
export interface AddFolderForm {
  folderName: string,
}
export interface AddBookmarkForm {
  folder: Bookmark
  bookmarkName: string,
  record: SearchRecord,
}

export interface dialogFormMeta {
  formType: 'AdvancedSearch' | 'AddFolder' | 'AddBookmark';
  AdvancedSearch: AdvancedSearchForm;
  AddFolder: AddFolderForm;
  AddBookmark: AddBookmarkForm;
}

export const useDialogStore = defineStore('dialog', {
  state: () => ({
    activeBookmark: false,
    activeSearchForm: false,
    activeFolder: false,
    activeFType: '' as dialogFormMeta['formType'],
    fAdvSearchOptions: {} as dialogFormMeta['AdvancedSearch'],
    fAddFolderOptions: {} as dialogFormMeta['AddFolder'],
    fAddBookmarkOptions: {} as dialogFormMeta['AddBookmark'],
  }),
  getters: {},
  actions: {
    showBookmarkForm() {
      this.removeSearchForm();
      this.removeFolderForm();
      this.activeBookmark = true;

    },
    removeBookmarkForm() {
      this.activeBookmark = false;
    },
    showSearchForm() {
      this.removeBookmarkForm();
      this.removeFolderForm();
      this.activeSearchForm = true;
    },
    removeSearchForm() {
      this.activeSearchForm = false;
    },
    showFolderForm() {
      this.removeBookmarkForm();
      this.removeSearchForm();
      this.activeFolder = true;
    },
    removeFolderForm() {
      this.activeFolder = false;
    },
    loadAddBookmark() {
      this.activeFType = 'AddBookmark'
      this.fAddBookmarkOptions = {
        folderName: '',
        bookmarkName: '',
        folder: {} as Bookmark,
        record: {} as SearchRecord
      } as dialogFormMeta['AddBookmark']
    },
    loadAddFolder() {
      this.activeFType = 'AddFolder'
      this.fAddFolderOptions = {
        folderName: '',
      } as dialogFormMeta['AddFolder']
    },
    loadAdvancedSearch() {
      this.activeFType = 'AdvancedSearch'
      this.fAdvSearchOptions = {
        articleTitle: '',
        articleSubjectTags: '',
        articleAuthors: [] as Array<Author>,
        articleDOI: '',
        dateStart: new Date(),
        dateEnd: new Date(),
        source: '',
      } as dialogFormMeta['AdvancedSearch']
    },
    addAuthorToAdvancedSearch(author: Author) {
      this.fAdvSearchOptions.articleAuthors.push(author);
    },
    setAuthors(authors: Array<Author>) {
      this.fAdvSearchOptions.articleAuthors = authors;
    },
    removeAuthorFromAdvancedSearch(author: Author) {
      const idx = this.fAdvSearchOptions.articleAuthors.indexOf(author)
      if(idx > -1){
        this.fAdvSearchOptions.articleAuthors.splice(idx, 1);
      }
    },
    updateAuthorInAdvancedSearch(author: Author) {
      const idx = this.fAdvSearchOptions.articleAuthors.indexOf(author)
      if(idx > -1){
        this.fAdvSearchOptions.articleAuthors[idx] = author;
      }
    },
    updateAdvancedSearchForm(formInp: dialogFormMeta['AdvancedSearch']){
      this.fAdvSearchOptions = formInp;
    },

    setBookmark(record: SearchRecord) {
      this.fAddBookmarkOptions.record = record;
      this.fAddBookmarkOptions.bookmarkName = record.title;
    },

    clearAdvancedSearchForm() {
      this.clearForm();
      this.fAdvSearchOptions = {} as dialogFormMeta['AdvancedSearch'];
    },
    clearAddBookmarkForm() {
      this.clearForm();
      this.fAddBookmarkOptions = {} as dialogFormMeta['AddBookmark'];
    },
    clearAddFolderForm() {
      this.clearForm();
      this.fAddFolderOptions = {} as dialogFormMeta['AddFolder'];
    },
    clearForm() {
      this.activeFType = '' as dialogFormMeta['formType']
    }
  },
});
