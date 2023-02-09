
export type Person = {
  _id: number;
  firstName: string;
  lastName: string;
  middleInitial: string;
}

export type Website = {
  _id: number;
  name: string;
}

export type Title = {
  _id: number;
  title: string;
  subtitles: Array<string>;
}

export type OtherContributers = {
  _id: number;
  contributers: Array<Person>;
}
export type Details = {
  _id: number;
  version: string;
  number: string;
  pages: string;
  publicationlocale: string;
  urlDOI: string;
  format: string;
  date: Date;
  accessDate: Date;
  publisher: string;
  type: string;
  chapter: string;
  editors: Array<Person>;
  translators: Array<Person>;
}

export type BibliographyElement = {
  _id: number;
  author: Person | Website;
  titleACW: Title;
  titleJBW: Title;
  otherContribs: OtherContributers;
  details: Details;
}

export const sampleBibliography: Array<BibliographyElement> = [
  {
    _id: 0,
    author: {_id: 0, firstName: 'Russell', lastName: 'Berman', middleInitial: 'A'},
    titleACW: {} as Title,
    titleJBW: {_id: 0, title: 'The Cambridge Companion to the Modern German Novel', subtitles: [] as string[]},
    otherContribs: {} as OtherContributers,
    details: {
      _id: 0,
      version: '',
      pages: '77-92',
      publicationlocale: 'Cambridge',
      urlDOI: '',
      format: '',
      date: new Date('01-01-2004'),
      publisher: 'Cambridge University Press',
      chapter: 'Modernism and the Bildungsroman: Thomas Mann\'s Magic Mountain',
      type: 'Book',
      editors: [{
        _id: 0,
        firstName: 'Graham',
        lastName: 'Bartram',
        middleInitial: ''
      }],
    } as Details
  },
  {
    _id: 1,
    author: {_id: 0, name: 'Biography.com'},
    titleACW: {} as Title,
    titleJBW: {_id: 0, title: 'Virginia Woolf', subtitles: [] as string[]},
    otherContribs: {} as OtherContributers,
    details: {
      _id: 0,
      version: '',
      pages: '',
      publicationlocale: '',
      urlDOI: 'https://www.biography.com/writer/virginia-woolf',
      format: '',
      date: new Date('03-27.2020'),
      accessDate: new Date('01-01-2020'),
      publisher: '',
      chapter: '',
      type: 'Webpage',
    } as Details
  },
  {
    _id: 2,
    author: {_id: 0, firstName: 'Marianne', lastName: 'Dekoven', middleInitial: ''},
    titleACW: {} as Title,
    titleJBW: {_id: 0, title: 'The Cambridge Companion to Modernism 2nd ed', subtitles: [] as string[]},
    otherContribs: {} as OtherContributers,
    details: {
      _id: 0,
      version: '',
      pages: '212-231',
      publicationlocale: 'Cambridge',
      urlDOI: '',
      format: '',
      date: new Date('01-01-2011'),
      publisher: 'Cambridge University Press',
      chapter: 'Modernism and Gender',
      type: 'Book',
      editors: [{
        _id: 0,
        firstName: 'Michael',
        lastName: 'Levenson',
        middleInitial: ''
      }],
    } as Details
  },
  {
    _id: 3,
    author: {_id: 0, firstName: 'Thomas', lastName: 'Mann', middleInitial: ''},
    titleACW: {} as Title,
    titleJBW: {_id: 0, title: 'The Magic Mountain', subtitles: [] as string[]},
    otherContribs: {} as OtherContributers,
    details: {
      _id: 0,
      version: '',
      pages: '212-231',
      publicationlocale: 'London',
      urlDOI: '',
      format: '',
      date: new Date('01-01-1999'),
      publisher: 'Vintage',
      chapter: 'The Cambridge Companion to Modernism 2nd ed.',
      type: 'Book',
      translators: [{
        _id: 0,
        firstName: 'H. T.',
        lastName: 'Lowe-Porter',
        middleInitial: '',
      }]
    } as Details
  },
  {
    _id: 4,
    author: {_id: 0, firstName: 'Shadi', lastName: 'Neimneh', middleInitial: ''},
    titleACW: {} as Title,
    titleJBW: {_id: 0, title: 'Mosaic: An Interdisciplinary Critical Journal', subtitles: [] as string[]},
    otherContribs: {} as OtherContributers,
    details: {
      _id: 0,
      version: '46',
      number: '4',
      pages: '75-90',
      publicationlocale: '',
      urlDOI: 'https://www.jsotr.org/stable/44030709',
      format: '',
      date: new Date('12-02-2013'),
      publisher: '',
      chapter: 'The Anti-Hero in Modernist Fiction: From Irony to Cultural Renewal',
      type: 'Journal',
    } as Details
  },
  {
    _id: 5,
    author: {_id: 0, firstName: 'Dorothy', lastName: 'Richardson', middleInitial: ''},
    titleACW: {} as Title,
    titleJBW: {_id: 0, title: 'Modernism: An Anthology', subtitles: [] as string[]},
    otherContribs: {} as OtherContributers,
    details: {
      _id: 0,
      version: '',
      pages: '587-591',
      publicationlocale: 'Oxford',
      urlDOI: '',
      format: '',
      date: new Date('01-01-2005'),
      publisher: 'Blackwell',
      chapter: 'The Reality of Feminism',
      type: 'Book',
      editors: [{
        _id: 0,
        firstName: 'Lawrence',
        lastName: 'Rainey',
        middleInitial: ''
      }],
    } as Details
  },
  {
    _id: 6,
    author: {_id: 0, firstName: 'Virginia', lastName: 'Woolf', middleInitial: ''},
    titleACW: {} as Title,
    titleJBW: {_id: 0, title: 'To the Lighthouse', subtitles: [] as string[]},
    otherContribs: {} as OtherContributers,
    details: {
      _id: 0,
      version: '',
      pages: '212-231',
      publicationlocale: 'Oxford',
      urlDOI: '',
      format: '',
      date: new Date('01-01-2006'),
      publisher: 'Oxford University Press',
      chapter: '',
      type: 'Book',
    } as Details
  },
]
