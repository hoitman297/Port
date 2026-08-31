'use client';

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useSyncExternalStore,
  type ReactNode,
} from 'react';

type Theme = 'light' | 'dark';

const STORAGE_KEY = 'portfolio-theme';
const THEME_CHANGE_EVENT = 'portfolio-theme-change';
const MEDIA_QUERY = '(prefers-color-scheme: dark)';

const ThemeContext = createContext<{
  theme: Theme;
  toggleTheme: () => void;
} | null>(null);

/** Inline script that runs before hydration so the stored (or OS-detected) theme applies with no flash. */
export const themeInitScript = `(function(){try{var t=localStorage.getItem('${STORAGE_KEY}');if(t!=='light'&&t!=='dark'){t=window.matchMedia('${MEDIA_QUERY}').matches?'dark':'light';}document.documentElement.setAttribute('data-theme',t);}catch(e){document.documentElement.setAttribute('data-theme','light');}})();`;

function subscribe(callback: () => void) {
  const media = window.matchMedia(MEDIA_QUERY);
  window.addEventListener(THEME_CHANGE_EVENT, callback);
  window.addEventListener('storage', callback);
  media.addEventListener('change', callback);
  return () => {
    window.removeEventListener(THEME_CHANGE_EVENT, callback);
    window.removeEventListener('storage', callback);
    media.removeEventListener('change', callback);
  };
}

/** Explicit user choice (localStorage) wins; otherwise falls back to the OS setting. */
function getSnapshot(): Theme {
  const stored = window.localStorage.getItem(STORAGE_KEY);
  if (stored === 'light' || stored === 'dark') return stored;
  return window.matchMedia(MEDIA_QUERY).matches ? 'dark' : 'light';
}

function getServerSnapshot(): Theme {
  return 'light';
}

function applyTheme(theme: Theme) {
  window.localStorage.setItem(STORAGE_KEY, theme);
  window.dispatchEvent(new Event(THEME_CHANGE_EVENT));
}

export function ThemeProvider({ children }: { children: ReactNode }) {
  const theme = useSyncExternalStore(subscribe, getSnapshot, getServerSnapshot);

  // Keeps the DOM attribute (and therefore the CSS variables) in sync,
  // including live OS theme changes that happen with no explicit user choice stored.
  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
  }, [theme]);

  const toggleTheme = useCallback(() => {
    applyTheme(theme === 'dark' ? 'light' : 'dark');
  }, [theme]);

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  const ctx = useContext(ThemeContext);
  if (!ctx) throw new Error('useTheme must be used within ThemeProvider');
  return ctx;
}
