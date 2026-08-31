import type { Metadata } from 'next';
import { Space_Mono } from 'next/font/google';
import { ThemeProvider, themeInitScript } from '@/lib/theme';
import { InlineScript } from '@/components/inline-script';
import './globals.css';

const spaceMono = Space_Mono({
  variable: '--font-space-mono',
  subsets: ['latin'],
  weight: ['400', '700'],
});

export const metadata: Metadata = {
  title: '홍길동 — Developer Portfolio',
  description:
    'Spring Boot와 React 사이를 오가며, 기능 단위로 문제를 정의하고 해결하는 개발자 포트폴리오입니다.',
};

export default function RootLayout({ children }: LayoutProps<'/'>) {
  return (
    <html lang="ko" className={spaceMono.variable} suppressHydrationWarning>
      <head>
        <link
          href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css"
          rel="stylesheet"
          type="text/css"
        />
        <InlineScript html={themeInitScript} />
      </head>
      <body>
        <ThemeProvider>{children}</ThemeProvider>
      </body>
    </html>
  );
}
