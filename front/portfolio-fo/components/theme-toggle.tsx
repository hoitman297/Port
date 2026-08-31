'use client';

import { useTheme } from '@/lib/theme';
import styles from './theme-toggle.module.css';

export function ThemeToggle() {
  const { theme, toggleTheme } = useTheme();

  return (
    <button
      type="button"
      onClick={toggleTheme}
      className={`mono ${styles.toggle}`}
      aria-label="테마 전환"
    >
      <span className={styles.dot} />
      {theme === 'dark' ? 'Dark' : 'Light'}
    </button>
  );
}
