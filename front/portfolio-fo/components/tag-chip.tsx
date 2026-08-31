import styles from './tag-chip.module.css';

export function TagChip({ children }: { children: React.ReactNode }) {
  return <span className={`mono ${styles.chip}`}>{children}</span>;
}
