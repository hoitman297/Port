import { getProjects, getTechStacks } from '@/lib/api';
import { profile } from '@/lib/content/profile';
import { SiteHeader } from '@/components/site-header';
import { HeroText } from '@/components/hero-text';
import { ProjectCard } from '@/components/project-card';
import { TagChip } from '@/components/tag-chip';
import styles from './page.module.css';

export default async function HomePage() {
  const [projects, techStacks] = await Promise.all([
    getProjects(),
    getTechStacks(),
  ]);

  const stackGroups = groupByCategory(techStacks);

  return (
    <div>
      <SiteHeader showBack={false} />

      <section className={styles.hero}>
        <h1 className={styles.heroName}>
          <HeroText text={profile.name} />
        </h1>
        <p className={`mono ${styles.heroRole}`}>
          <HeroText text={profile.role} startDelay={profile.name.length * 40} />
        </p>
        <p className={styles.heroBio}>{profile.heroBio}</p>
      </section>

      <section className={styles.section}>
        <div className={styles.sectionHeader}>
          <h2 className={`mono ${styles.sectionTitle}`}>PROJECTS</h2>
        </div>
        <div className={styles.grid}>
          {projects.map((project, i) => (
            <ProjectCard key={project.id} project={project} delay={i * 100} />
          ))}
        </div>
      </section>

      <section className={styles.stackSection}>
        <div className={styles.sectionHeader}>
          <h2 className={`mono ${styles.sectionTitle}`}>STACK</h2>
        </div>
        <div className={styles.stackGrid}>
          {stackGroups.map((group) => (
            <div key={group.label}>
              <div className={`mono ${styles.stackLabel}`}>{group.label}</div>
              <div className={styles.stackTags}>
                {group.techStacks.map((tech) => (
                  <TagChip key={tech.id}>{tech.name}</TagChip>
                ))}
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

function groupByCategory(techStacks: Awaited<ReturnType<typeof getTechStacks>>) {
  const map = new Map<string, typeof techStacks>();
  for (const tech of techStacks) {
    const list = map.get(tech.category) ?? [];
    list.push(tech);
    map.set(tech.category, list);
  }
  return Array.from(map.entries()).map(([label, items]) => ({
    label: label.toUpperCase(),
    techStacks: items,
  }));
}
