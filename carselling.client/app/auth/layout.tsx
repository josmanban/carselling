'use client'

import Container from '@mui/material/Container';


export default function LogedInLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) { 

  return (
    <>
    <Container maxWidth="sm">
        {children}        
    </Container>    
    </>
  );
}