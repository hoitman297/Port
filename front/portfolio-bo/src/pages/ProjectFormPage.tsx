import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router';
import { FeatureCard } from '../components/FeatureCard';
import { ImageUploadField } from '../components/ImageUploadField';
import { ApiError, createProject, getAdminTechStacks, getProjectDetail, updateProject } from '../lib/api';
import { featureToDraft, type FeatureDraft } from '../lib/feature-draft';
import type { TechStack } from '../types';

function nextLocalId() {
  return `local-${Math.random().toString(36).slice(2)}`;
}

export function ProjectFormPage() {
  const { id } = useParams();
  const isEdit = id !== undefined;
  const navigate = useNavigate();

  const [projectId, setProjectId] = useState<number | null>(isEdit ? Number(id) : null);
  const [title, setTitle] = useState('');
  const [summary, setSummary] = useState('');
  const [thumbnailUrl, setThumbnailUrl] = useState<string | null>(null);
  const [githubUrl, setGithubUrl] = useState('');
  const [demoUrl, setDemoUrl] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [techStackIds, setTechStackIds] = useState<number[]>([]);
  const [features, setFeatures] = useState<FeatureDraft[]>([]);

  const [allTechStacks, setAllTechStacks] = useState<TechStack[] | null>(null);
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    getAdminTechStacks().then(setAllTechStacks);
  }, []);

  useEffect(() => {
    if (!isEdit) return;
    getProjectDetail(Number(id))
      .then((project) => {
        setTitle(project.title);
        setSummary(project.summary ?? '');
        setThumbnailUrl(project.thumbnailUrl);
        setGithubUrl(project.githubUrl ?? '');
        setDemoUrl(project.demoUrl ?? '');
        setStartDate(project.startDate ?? '');
        setEndDate(project.endDate ?? '');
        setTechStackIds(project.techStacks.map((t) => t.id));
        setFeatures(project.features.map((f) => featureToDraft(f, String(f.id))));
      })
      .finally(() => setLoading(false));
  }, [id, isEdit]);

  function toggleTechStack(techId: number) {
    setTechStackIds((prev) => (prev.includes(techId) ? prev.filter((t) => t !== techId) : [...prev, techId]));
  }

  async function handleSaveBasicInfo() {
    setError(null);
    setSaved(false);
    setSaving(true);
    try {
      const payload = {
        title,
        summary,
        thumbnailUrl,
        githubUrl: githubUrl || null,
        demoUrl: demoUrl || null,
        startDate: startDate || null,
        endDate: endDate || null,
        techStackIds,
      };

      if (projectId) {
        await updateProject(projectId, payload);
      } else {
        const created = await createProject(payload);
        setProjectId(created.id);
        navigate(`/projects/${created.id}`, { replace: true });
      }
      setSaved(true);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : '저장 중 오류가 발생했습니다.');
    } finally {
      setSaving(false);
    }
  }

  function handleAddFeature() {
    setFeatures((prev) => [
      ...prev,
      { localId: nextLocalId(), name: '', imageUrl: null, description: '', reason: '', troubleshooting: null },
    ]);
  }

  function handleFeatureChange(localId: string, next: FeatureDraft) {
    setFeatures((prev) => prev.map((f) => (f.localId === localId ? next : f)));
  }

  function handleFeatureRemove(localId: string) {
    setFeatures((prev) => prev.filter((f) => f.localId !== localId));
  }

  if (loading) return <p>불러오는 중…</p>;

  return (
    <div>
      <h1>{isEdit ? '프로젝트 수정' : '새 프로젝트'}</h1>

      <section className="form-section">
        <label>제목</label>
        <input value={title} onChange={(e) => setTitle(e.target.value)} required />

        <label>요약</label>
        <textarea value={summary} onChange={(e) => setSummary(e.target.value)} rows={3} />

        <ImageUploadField label="대표 이미지" value={thumbnailUrl} onChange={setThumbnailUrl} />

        <div className="form-row">
          <div>
            <label>시작일</label>
            <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
          </div>
          <div>
            <label>종료일</label>
            <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
          </div>
        </div>

        <label>GitHub 링크</label>
        <input value={githubUrl} onChange={(e) => setGithubUrl(e.target.value)} />

        <label>배포 링크</label>
        <input value={demoUrl} onChange={(e) => setDemoUrl(e.target.value)} />

        <label>기술 스택</label>
        <div className="tech-stack-select">
          {allTechStacks === null && <p>불러오는 중…</p>}
          {allTechStacks?.map((tech) => (
            <label key={tech.id} className="checkbox-chip">
              <input
                type="checkbox"
                checked={techStackIds.includes(tech.id)}
                onChange={() => toggleTechStack(tech.id)}
              />
              {tech.name}
            </label>
          ))}
        </div>

        <div className="feature-card-actions">
          <button type="button" onClick={handleSaveBasicInfo} disabled={saving || !title}>
            {saving ? '저장 중…' : '기본정보 저장'}
          </button>
          {saved && <span className="save-ok">저장됨</span>}
          {error && <p className="error-text">{error}</p>}
        </div>
      </section>

      <section className="form-section">
        <div className="page-header">
          <h2>기능</h2>
          <button type="button" onClick={handleAddFeature} disabled={!projectId}>
            기능 추가
          </button>
        </div>
        {!projectId && <p>기능을 추가하려면 먼저 기본정보를 저장해주세요.</p>}

        {features.map((draft, i) => (
          <FeatureCard
            key={draft.localId}
            projectId={projectId ?? 0}
            sortOrder={i + 1}
            draft={draft}
            onChange={(next) => handleFeatureChange(draft.localId, next)}
            onRemove={() => handleFeatureRemove(draft.localId)}
          />
        ))}
      </section>
    </div>
  );
}
