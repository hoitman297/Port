import { notFound } from 'next/navigation';
import { getProject } from '@/lib/api';
import { SiteHeader } from '@/components/site-header';
import { TagChip } from '@/components/tag-chip';
import { FeatureBlock } from '@/components/feature-block';
import styles from './page.module.css';

export default async function ProjectDetailPage(
  props: PageProps<'/projects/[id]'>
) {
  const { id } = await props.params;
  const project = await getProject(id).catch(() => null);
  if (!project) notFound();

  return (
    <div>
      <SiteHeader />

      <section className={styles.hero}>
        <h1 className={styles.title}>{project.title}</h1>

        {project.thumbnailUrl && (
          <div className={styles.heroImage}>
            {/* eslint-disable-next-line @next/next/no-img-element */}
            <img src={project.thumbnailUrl} alt={project.title} />
          </div>
        )}

        <div className={styles.stackRow}>
          {project.techStacks.map((tech) => (
            <TagChip key={tech.id}>{tech.name}</TagChip>
          ))}
        </div>

        <p className={styles.overview}>{project.summary}</p>
      </section>

      <section className={styles.featuresSection}>
        <div className={styles.sectionHeader}>
          <h2 className={`mono ${styles.sectionTitle}`}>FEATURES</h2>
        </div>
        {project.features.map((feature, i) => (
          <FeatureBlock
            key={feature.id}
            feature={feature}
            number={String(i + 1).padStart(2, '0')}
          />
        ))}
      </section>

      <section className={styles.linksSection}>
        {project.githubUrl && (
          <a
            href={project.githubUrl}
            target="_blank"
            rel="noreferrer"
            className={`mono ${styles.linkBtn}`}
          >
            GitHub ↗
          </a>
        )}
        {project.demoUrl && (
          <a
            href={project.demoUrl}
            target="_blank"
            rel="noreferrer"
            className={`mono ${styles.linkBtnPrimary}`}
          >
            배포 링크 ↗
          </a>
        )}
      </section>
    </div>
  );
}
