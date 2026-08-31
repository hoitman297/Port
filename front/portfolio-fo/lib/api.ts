import type { ProjectDetail, ProjectListItem, TechStack } from './types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? '';

async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    cache: 'no-store',
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...init?.headers,
    },
  });

  if (!res.ok) {
    throw new Error(`API request failed: ${init?.method ?? 'GET'} ${path} (${res.status})`);
  }

  if (res.status === 204) return undefined as T;
  return res.json() as Promise<T>;
}

export function getProjects(): Promise<ProjectListItem[]> {
  return apiFetch<ProjectListItem[]>('/api/projects');
}

export function getProject(id: string | number): Promise<ProjectDetail> {
  return apiFetch<ProjectDetail>(`/api/projects/${id}`);
}

export function getTechStacks(): Promise<TechStack[]> {
  return apiFetch<TechStack[]>('/api/tech-stacks');
}
