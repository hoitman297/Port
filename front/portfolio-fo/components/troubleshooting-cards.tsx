import type { Troubleshooting } from '@/lib/types';
import { ScrollReveal } from './scroll-reveal';
import styles from './troubleshooting-cards.module.css';

const STEPS: { key: keyof Troubleshooting; step: string; label: string }[] = [
  { key: 'problem', step: '01', label: '문제' },
  { key: 'analysis', step: '02', label: '분석' },
  { key: 'action', step: '03', label: '실행' },
  { key: 'result', step: '04', label: '결과' },
];

export function TroubleshootingCards({
  troubleshooting,
}: {
  troubleshooting: Troubleshooting;
}) {
  return (
    <div className={styles.wrapper}>
      <div className={`mono ${styles.title}`}>TROUBLESHOOTING</div>
      <div className={styles.grid}>
        {STEPS.map((s, i) => (
          <ScrollReveal key={s.key} translateY={16} duration={500} delay={i * 90}>
            <div>
              <div className={`mono ${styles.step}`}>
                {s.step} · {s.label}
              </div>
              <p className={styles.text}>{troubleshooting[s.key]}</p>
            </div>
          </ScrollReveal>
        ))}
      </div>
    </div>
  );
}
