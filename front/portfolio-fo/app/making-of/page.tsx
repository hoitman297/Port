import { SiteHeader } from '@/components/site-header';
import { ScrollReveal } from '@/components/scroll-reveal';
import { archNodes, essaySections, introText, links } from '@/lib/content/making-of';
import styles from './page.module.css';

export const metadata = { title: 'Making Of — 홍길동' };

export default function MakingOfPage() {
  return (
    <div>
      <SiteHeader />

      <section className={styles.intro}>
        <div className={`mono ${styles.eyebrow}`}>MAKING OF</div>
        <h1 className={styles.heading}>
          이 포트폴리오는 어떻게, 왜 이렇게 만들어졌나
        </h1>
        <p className={styles.introText}>{introText}</p>
      </section>

      <section className={styles.archSection}>
        <div className={`mono ${styles.archLabel}`}>ARCHITECTURE</div>
        <div className={styles.archDiagram}>
          {archNodes.map((node, i) => (
            <div key={node.label} className={styles.archNodeWrap}>
              <div className={styles.archNode}>
                <div className={`mono ${styles.archNodeLabel}`}>{node.label}</div>
                <div className={`mono ${styles.archNodeSub}`}>{node.sub}</div>
              </div>
              {i < archNodes.length - 1 && (
                <span className={`mono ${styles.archArrow}`}>↔</span>
              )}
            </div>
          ))}
        </div>
      </section>

      <section className={styles.essaySection}>
        {essaySections.map((sec, i) => (
          <ScrollReveal key={sec.title} className={styles.essayBlock}>
            <div className={`mono ${styles.essayNumber}`}>
              {String(i + 1).padStart(2, '0')}
            </div>
            <h2 className={styles.essayTitle}>{sec.title}</h2>
            {sec.paragraphs.map((p) => (
              <p key={p.slice(0, 12)} className={styles.essayText}>
                {p}
              </p>
            ))}
          </ScrollReveal>
        ))}
      </section>

      <section className={styles.linksSection}>
        <a href={links.githubUrl} className={`mono ${styles.linkBtn}`}>
          GitHub ↗
        </a>
        <a href={links.demoUrl} className={`mono ${styles.linkBtnPrimary}`}>
          배포 링크 ↗
        </a>
      </section>
    </div>
  );
}
