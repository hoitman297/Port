import { useState } from 'react';
import {
  ApiError,
  createFeature,
  createTroubleshooting,
  deleteFeature,
  deleteTroubleshooting,
  updateFeature,
  updateTroubleshooting,
} from '../lib/api';
import type { FeatureDraft } from '../lib/feature-draft';
import { ImageUploadField } from './ImageUploadField';

export function FeatureCard({
  projectId,
  sortOrder,
  draft,
  onChange,
  onRemove,
}: {
  projectId: number;
  sortOrder: number;
  draft: FeatureDraft;
  onChange: (draft: FeatureDraft) => void;
  onRemove: () => void;
}) {
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [troubleshootingEnabled, setTroubleshootingEnabled] = useState(draft.troubleshooting !== null);
  const [tsDraft, setTsDraft] = useState({
    problem: draft.troubleshooting?.problem ?? '',
    analysis: draft.troubleshooting?.analysis ?? '',
    action: draft.troubleshooting?.action ?? '',
    result: draft.troubleshooting?.result ?? '',
  });
  const [tsSaving, setTsSaving] = useState(false);
  const [tsError, setTsError] = useState<string | null>(null);

  function set<K extends keyof FeatureDraft>(key: K, value: FeatureDraft[K]) {
    onChange({ ...draft, [key]: value });
  }

  async function handleSaveFeature() {
    setError(null);
    setSaving(true);
    try {
      const payload = {
        name: draft.name,
        imageUrl: draft.imageUrl,
        description: draft.description,
        reason: draft.reason,
        sortOrder,
      };
      const saved = draft.id ? await updateFeature(draft.id, payload) : await createFeature(projectId, payload);
      onChange({ ...draft, id: saved.id });
    } catch (err) {
      setError(err instanceof ApiError ? err.message : '기능 저장 중 오류가 발생했습니다.');
    } finally {
      setSaving(false);
    }
  }

  async function handleDeleteFeature() {
    if (!draft.id) {
      onRemove();
      return;
    }
    if (!confirm('이 기능을 삭제할까요?')) return;
    setError(null);
    setSaving(true);
    try {
      await deleteFeature(draft.id);
      onRemove();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : '삭제 중 오류가 발생했습니다.');
    } finally {
      setSaving(false);
    }
  }

  async function handleSaveTroubleshooting() {
    if (!draft.id) {
      setTsError('먼저 기능을 저장해주세요.');
      return;
    }
    setTsError(null);
    setTsSaving(true);
    try {
      const saved = draft.troubleshooting
        ? await updateTroubleshooting(draft.troubleshooting.id, tsDraft)
        : await createTroubleshooting(draft.id, tsDraft);
      onChange({ ...draft, troubleshooting: saved });
    } catch (err) {
      setTsError(err instanceof ApiError ? err.message : '트러블슈팅 저장 중 오류가 발생했습니다.');
    } finally {
      setTsSaving(false);
    }
  }

  async function handleRemoveTroubleshooting() {
    if (draft.troubleshooting) {
      if (!confirm('트러블슈팅을 삭제할까요?')) return;
      setTsError(null);
      setTsSaving(true);
      try {
        await deleteTroubleshooting(draft.troubleshooting.id);
        onChange({ ...draft, troubleshooting: null });
        setTsDraft({ problem: '', analysis: '', action: '', result: '' });
        setTroubleshootingEnabled(false);
      } catch (err) {
        setTsError(err instanceof ApiError ? err.message : '삭제 중 오류가 발생했습니다.');
      } finally {
        setTsSaving(false);
      }
    } else {
      setTroubleshootingEnabled(false);
      setTsDraft({ problem: '', analysis: '', action: '', result: '' });
    }
  }

  return (
    <div className="feature-card">
      <div className="feature-card-header">
        <h3>기능 {sortOrder}</h3>
        <button type="button" onClick={handleDeleteFeature} disabled={saving}>
          기능 삭제
        </button>
      </div>

      <label>기능명</label>
      <input value={draft.name} onChange={(e) => set('name', e.target.value)} required />

      <ImageUploadField label="기능 이미지" value={draft.imageUrl} onChange={(url) => set('imageUrl', url)} />

      <label>구현 내용</label>
      <textarea value={draft.description} onChange={(e) => set('description', e.target.value)} rows={3} />

      <label>구현 이유</label>
      <textarea value={draft.reason} onChange={(e) => set('reason', e.target.value)} rows={3} />

      <div className="feature-card-actions">
        <button type="button" onClick={handleSaveFeature} disabled={saving || !draft.name}>
          {saving ? '저장 중…' : draft.id ? '기능 수정 저장' : '기능 저장'}
        </button>
        {error && <p className="error-text">{error}</p>}
      </div>

      <div className="troubleshooting-toggle">
        <label>
          <input
            type="checkbox"
            checked={troubleshootingEnabled}
            onChange={(e) => {
              if (!e.target.checked) {
                handleRemoveTroubleshooting();
              } else {
                setTroubleshootingEnabled(true);
              }
            }}
          />
          트러블슈팅 추가
        </label>
      </div>

      {troubleshootingEnabled && (
        <div className="troubleshooting-fields">
          <label>문제</label>
          <textarea
            value={tsDraft.problem}
            onChange={(e) => setTsDraft((prev) => ({ ...prev, problem: e.target.value }))}
            rows={2}
          />
          <label>분석</label>
          <textarea
            value={tsDraft.analysis}
            onChange={(e) => setTsDraft((prev) => ({ ...prev, analysis: e.target.value }))}
            rows={2}
          />
          <label>실행</label>
          <textarea
            value={tsDraft.action}
            onChange={(e) => setTsDraft((prev) => ({ ...prev, action: e.target.value }))}
            rows={2}
          />
          <label>결과</label>
          <textarea
            value={tsDraft.result}
            onChange={(e) => setTsDraft((prev) => ({ ...prev, result: e.target.value }))}
            rows={2}
          />
          <div className="feature-card-actions">
            <button
              type="button"
              onClick={handleSaveTroubleshooting}
              disabled={tsSaving || !tsDraft.problem || !tsDraft.analysis || !tsDraft.action || !tsDraft.result}
            >
              {tsSaving ? '저장 중…' : draft.troubleshooting ? '트러블슈팅 수정 저장' : '트러블슈팅 저장'}
            </button>
            {tsError && <p className="error-text">{tsError}</p>}
          </div>
        </div>
      )}
    </div>
  );
}
