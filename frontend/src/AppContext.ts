import { createContext } from 'react'

interface AppContextProps {
  page: string
  setPage: (page: string) => void
}

export const AppContext = createContext<AppContextProps>({
  page: '',
  setPage: () => {},
})
