import { useEffect, useMemo, useState, type FormEvent } from 'react';
import { ApiError, createTechStack, deleteTechStack, getAdminTechStacks } from '../lib/api';
import type { TechStack } from '../types';

export function TechStacksPage() {
  const [techStacks, setTechStacks] = useState<TechStack[] | null>(null);
  const [name, setName] = useState('');
  const [category, setCategory] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [deleteErrors, setDeleteErrors] = useState<Record<number, string>>({});
  const [deletingId, setDeletingId] = useState<number | null>(null);

  useEffect(() => {
    load();
  }, []);

  function load() {
    getAdminTechStacks().then(setTechStacks);
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setFormError(null);
    setSubmitting(true);
    try {
      await createTechStack({ name, category });
      setName('');
      setCategory('');
      load();
    } catch (err) {
      setFormError(err instanceof ApiError ? err.message : '기술 스택 추가 중 오류가 발생했습니다.');
    } finally {
      setSubmitting(false);
    }
  }

  async function handleDelete(id: number) {
    setDeletingId(id);
    setDeleteErrors((prev) => {
      const next = { ...prev };
      delete next[id];
      return next;
    });
    try {
      await deleteTechStack(id);
      load();
    } catch (err) {
      if (err instanceof ApiError && err.status === 409) {
        setDeleteErrors((prev) => ({
          ...prev,
          [id]: '이 스택을 사용 중인 프로젝트가 있어 삭제할 수 없습니다.',
        }));
      } else {
        setDeleteErrors((prev) => ({
          ...prev,
          [id]: err instanceof ApiError ? err.message : '삭제 중 오류가 발생했습니다.',
        }));
      }
    } finally {
      setDeletingId(null);
    }
  }

  const groups = useMemo(() => {
    if (!techStacks) return [];
    const map = new Map<string, TechStack[]>();
    for (const tech of techStacks) {
      const list = map.get(tech.category) ?? [];
      list.push(tech);
      map.set(tech.category, list);
    }
    return Array.from(map.entries());
  }, [techStacks]);

  return (
    <div>
      <h1>기술 스택 관리</h1>

      <form className="inline-form" onSubmit={handleSubmit}>
        <input
          placeholder="이름 (예: Redis)"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
        <input
          placeholder="카테고리 (예: Backend)"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          required
        />
        <button type="submit" disabled={submitting}>
          {submitting ? '추가 중…' : '추가'}
        </button>
      </form>
      {formError && <p className="error-text">{formError}</p>}

      {techStacks === null && <p>불러오는 중…</p>}
      {techStacks !== null && techStacks.length === 0 && <p>등록된 기술 스택이 없습니다.</p>}

      {groups.map(([category, items]) => (
        <section key={category} className="tech-stack-group">
          <h2>{category}</h2>
          <ul className="tech-stack-list">
            {items.map((tech) => (
              <li key={tech.id}>
                <span>{tech.name}</span>
                <button
                  type="button"
                  onClick={() => handleDelete(tech.id)}
                  disabled={deletingId === tech.id}
                >
                  {deletingId === tech.id ? '삭제 중…' : '삭제'}
                </button>
                {deleteErrors[tech.id] && <p className="error-text">{deleteErrors[tech.id]}</p>}
              </li>
            ))}
          </ul>
        </section>
      ))}
    </div>
  );
}
