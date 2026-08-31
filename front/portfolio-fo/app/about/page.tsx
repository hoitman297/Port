import { profile } from '@/lib/content/profile';
import { SiteHeader } from '@/components/site-header';
import styles from './page.module.css';

export const metadata = { title: 'About — 홍길동' };

export default function AboutPage() {
  return (
    <div>
      <SiteHeader />

      <section className={styles.intro}>
        <div className={`mono ${styles.eyebrow}`}>ABOUT</div>
        <h1 className={styles.name}>{profile.name}</h1>
        <p className={styles.introText}>{profile.aboutIntro}</p>
      </section>

      <section className={styles.section}>
        <h2 className={`mono ${styles.sectionTitle}`}>EXPERIENCE</h2>
        {profile.experience.map((item) => (
          <div key={item.title} className={styles.row}>
            <div className={`mono ${styles.period}`}>{item.period}</div>
            <div>
              <div className={styles.itemTitle}>{item.title}</div>
              <div className={styles.itemOrg}>{item.org}</div>
              <p className={styles.itemDesc}>{item.desc}</p>
            </div>
          </div>
        ))}
      </section>

      <section className={styles.section}>
        <h2 className={`mono ${styles.sectionTitle}`}>EDUCATION</h2>
        {profile.education.map((item) => (
          <div key={item.title} className={styles.row}>
            <div className={`mono ${styles.period}`}>{item.period}</div>
            <div>
              <div className={styles.itemTitle}>{item.title}</div>
              <div className={styles.itemOrg}>{item.org}</div>
            </div>
          </div>
        ))}
      </section>

      <section className={styles.contactSection}>
        <h2 className={`mono ${styles.sectionTitle}`}>CONTACT</h2>
        <div className={styles.contactList}>
          {profile.contacts.map((c) => (
            <a key={c.label} href={c.href} className={styles.contactRow}>
              <span className={`mono ${styles.contactLabel}`}>{c.label}</span>
              <span className={styles.contactValue}>{c.value}</span>
            </a>
          ))}
        </div>
      </section>
    </div>
  );
}
