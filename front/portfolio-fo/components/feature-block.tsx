import type { ProjectFeature } from '@/lib/types';
import { ScrollReveal } from './scroll-reveal';
import { TroubleshootingCards } from './troubleshooting-cards';
import styles from './feature-block.module.css';

export function FeatureBlock({
  feature,
  number,
}: {
  feature: ProjectFeature;
  number: string;
}) {
  return (
    <ScrollReveal className={styles.block}>
      <div className={styles.heading}>
        <span className={`mono ${styles.number}`}>{number}</span>
        <h3 className={styles.title}>{feature.name}</h3>
      </div>

      {feature.imageUrl && (
        <div className={styles.imageWrap}>
          {/* eslint-disable-next-line @next/next/no-img-element */}
          <img src={feature.imageUrl} alt={feature.name} />
        </div>
      )}

      <div className={styles.grid}>
        <div>
          <div className={`mono ${styles.label}`}>구현 내용</div>
          <p className={styles.text}>{feature.description}</p>
        </div>
        <div>
          <div className={`mono ${styles.label}`}>구현 이유</div>
          <p className={styles.text}>{feature.reason}</p>
        </div>
      </div>

      {feature.troubleshooting && (
        <TroubleshootingCards troubleshooting={feature.troubleshooting} />
      )}
    </ScrollReveal>
  );
}
