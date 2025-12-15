'use client'
import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { AppRouterCacheProvider } from '@mui/material-nextjs/v15-appRouter';
import CssBaseline from '@mui/material/CssBaseline';
import { useContext, useState } from "react";
import { Roboto } from 'next/font/google';
import { ThemeProvider } from '@mui/material/styles';
import theme from '../src/theme';
import Toast from "@/src/components/toast/Toast";
import useAuth from "@/src/hooks/useAuth";


const roboto = Roboto({
  weight: ['300', '400', '500', '700'],
  subsets: ['latin'],
  display: 'swap',
  variable: '--font-roboto',
});
import GlobalContext from "@/src/contexts/GlogalContext";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const [toastProps, setToastProps] = useState({
    open: false,
    message: "",
    severity: "info" as "success" | "info" | "warning" | "error",    
    setProps: () => {}
  });
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  
  return (
    <html lang="en" className={roboto.variable}>      
      <body>      
      <AppRouterCacheProvider>
        <ThemeProvider theme={theme}>
          <GlobalContext.Provider value={{ toastProps, setToastProps, isAuthenticated, setIsAuthenticated }}>
            <CssBaseline />
            <Toast
              open={toastProps.open}
              message={toastProps.message}
              severity={toastProps.severity}
              setProps= {setToastProps}
              />
            {children}
          </GlobalContext.Provider>
        </ThemeProvider>
      </AppRouterCacheProvider>
      </body>
    </html>
  );
}
