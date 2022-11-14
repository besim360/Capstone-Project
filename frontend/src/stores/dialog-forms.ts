import { defineStore } from 'pinia';
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
  folderName: string,
  bookmarkName: string,
}

export interface dialogFormMeta {
  formType: 'AdvancedSearch' | 'AddFolder' | 'AddBookmark';
  AdvancedSearch: AdvancedSearchForm;
  AddFolder: AddFolderForm;
  AddBookmark: AddBookmarkForm;
}

export const useDialogStore = defineStore('dialog', {
  state: () => ({
    active: false,
    activeFType: '' as dialogFormMeta['formType'],
    fAdvSearchOptions: {} as dialogFormMeta['AdvancedSearch'],
    fAddFolderOptions: {} as dialogFormMeta['AddFolder'],
    fAddBookmarkOptions: {} as dialogFormMeta['AddBookmark'],
  }),
  getters: {},
  actions: {
    showForm() {
      this.active = true;
    },
    removeForm() {
      this.active = false;
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
    clearAdvancedSearchForm() {
      this.fAdvSearchOptions = {} as dialogFormMeta['AdvancedSearch'];
    }

  },
});
