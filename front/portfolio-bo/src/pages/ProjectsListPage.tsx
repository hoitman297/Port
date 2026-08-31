import { useEffect, useState } from 'react';
import { Link } from 'react-router';
import { ApiError, deleteProject, getAdminProjects } from '../lib/api';
import type { ProjectListItem } from '../types';

export function ProjectsListPage() {
  const [projects, setProjects] = useState<ProjectListItem[] | null>(null);
  const [deletingId, setDeletingId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    load();
  }, []);

  function load() {
    getAdminProjects().then(setProjects);
  }

  async function handleDelete(id: number) {
    if (!confirm('이 프로젝트를 삭제할까요? 기능과 트러블슈팅도 함께 삭제됩니다.')) return;
    setError(null);
    setDeletingId(id);
    try {
      await deleteProject(id);
      load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : '삭제 중 오류가 발생했습니다.');
    } finally {
      setDeletingId(null);
    }
  }

  return (
    <div>
      <div className="page-header">
        <h1>프로젝트 관리</h1>
        <Link to="/projects/new" className="btn-primary">
          새 프로젝트
        </Link>
      </div>

      {error && <p className="error-text">{error}</p>}
      {projects === null && <p>불러오는 중…</p>}
      {projects !== null && projects.length === 0 && <p>등록된 프로젝트가 없습니다.</p>}

      <ul className="project-list">
        {projects?.map((project) => (
          <li key={project.id} className="project-list-item">
            <div>
              <h2>{project.title}</h2>
              <p>{project.summary}</p>
              <div className="tag-row">
                {project.techStacks.map((tech) => (
                  <span key={tech.id} className="tag">
                    {tech.name}
                  </span>
                ))}
              </div>
            </div>
            <div className="project-list-actions">
              <Link to={`/projects/${project.id}`}>수정</Link>
              <button
                type="button"
                onClick={() => handleDelete(project.id)}
                disabled={deletingId === project.id}
              >
                {deletingId === project.id ? '삭제 중…' : '삭제'}
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
