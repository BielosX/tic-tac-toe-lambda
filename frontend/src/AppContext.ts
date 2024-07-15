import { createContext, useContext } from 'react'

export interface AppContextProps {
  page: string
  setPage: (page: string) => void
}

export const AppContext = createContext<AppContextProps>({
  page: '',
  setPage: () => {},
})

export const useAppContext = () => {
  return useContext(AppContext)
}
