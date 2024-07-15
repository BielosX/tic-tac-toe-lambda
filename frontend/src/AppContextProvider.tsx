import { ReactNode, useState } from 'react'
import { AppContext } from './AppContext'

export interface AppContextProviderProps {
  children: ReactNode
}

export const AppContextProvider = (props: AppContextProviderProps) => {
  const [page, setPage] = useState('')

  return (
    <AppContext.Provider value={{
      page,
      setPage,
    }}
    >
      {props.children}
    </AppContext.Provider>
  )
}
