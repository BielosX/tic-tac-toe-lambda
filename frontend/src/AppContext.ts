import { createContext, useContext, useEffect } from 'react'

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

export const usePage = (page: string) => {
  const { setPage } = useAppContext()

  useEffect(() => {
    setPage(page)
  }, [page, setPage])
}
