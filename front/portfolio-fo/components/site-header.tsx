import { BackLink } from './back-link';
import { ThemeToggle } from './theme-toggle';
import styles from './site-header.module.css';

export function SiteHeader({ showBack = true }: { showBack?: boolean }) {
  return (
    <div className={styles.header} data-align={showBack ? 'between' : 'end'}>
      {showBack && <BackLink />}
      <ThemeToggle />
    </div>
  );
}
