import Link from 'next/link';
import type { ProjectListItem } from '@/lib/types';
import { ScrollReveal } from './scroll-reveal';
import { TagChip } from './tag-chip';
import styles from './project-card.module.css';

export function ProjectCard({
  project,
  delay = 0,
}: {
  project: ProjectListItem;
  delay?: number;
}) {
  return (
    <ScrollReveal delay={delay} translateY={28}>
      <Link href={`/projects/${project.id}`} className={styles.card}>
        <div className={styles.thumb}>
          {project.thumbnailUrl ? (
            // eslint-disable-next-line @next/next/no-img-element
            <img src={project.thumbnailUrl} alt={project.title} />
          ) : (
            <div className={styles.thumbPlaceholder} />
          )}
        </div>
        <div className={styles.body}>
          <div className={styles.titleRow}>
            <h3 className={styles.title}>{project.title}</h3>
            <span className={`mono ${styles.viewLink}`}>보기 →</span>
          </div>
          <p className={styles.desc}>{project.summary}</p>
          <div className={styles.tags}>
            {project.techStacks.map((tech) => (
              <TagChip key={tech.id}>{tech.name}</TagChip>
            ))}
          </div>
        </div>
      </Link>
    </ScrollReveal>
  );
}
