import Link from 'next/link';
import styles from './back-link.module.css';

export function BackLink() {
  return (
    <Link href="/" className={`mono ${styles.link}`}>
      <span>←</span> 뒤로가기
    </Link>
  );
}
