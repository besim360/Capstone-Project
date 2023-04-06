import { defineStore } from 'pinia';
import User from './user';
import { SearchHistory } from 'src/api/models/history';
import { UserBookmarks } from 'src/api/models/bookmark';

/**
 * Provide a store to manage the state of the user profile
 */
const useUserStore = defineStore('user', {
  /**
   * Shared user profile state
   * @returns state object
   */
  state: () => {
    return {
      loggedIn: false,
      user: <User>{},
      searchHistory: {} as SearchHistory,
      bookmarks: {
        id: '',
        uid: '',
        bookmarkFolders: [],
      } as UserBookmarks,
    };
  },
  // optional getters
  getters: {},
  // optional actions
  actions: {
    /**
     * Set the user profile
     * @param user New user profile
     */
    setUser(user: User) {
      this.user = user;
    },

    /**
     * Set the authenticated state
     * @param loggedIn True = Logged in, else logged out
     */
    setLoggedIn(loggedIn: boolean) {
      this.loggedIn = loggedIn;
    },

    /**
     * Set the logged in user search history
     * @param searchHistory Array of search records
     */
    setSearchHistory(searchHistory: SearchHistory) {
      let history = [] as SearchHistory
      if(searchHistory.length > 1){
        history = searchHistory.sort((a,b) => new Date(b.queryDate).getTime() - new Date(a.queryDate).getTime())
      } else {
        history = searchHistory
      }
      this.searchHistory = history
    },

    setBookmarks(bookmarks: UserBookmarks) {
      this.bookmarks = bookmarks
    }
  },
});

export default useUserStore;
