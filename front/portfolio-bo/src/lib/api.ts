import type {
  ApiErrorBody,
  FeatureRequest,
  ProjectDetail,
  ProjectFeature,
  ProjectListItem,
  ProjectRequest,
  TechStack,
  TechStackRequest,
  Troubleshooting,
  TroubleshootingRequest,
} from '../types';

const API_BASE_URL = import.meta.env.VITE_API_URL ?? '';

export class ApiError extends Error {
  status: number;
  body: ApiErrorBody | null;

  constructor(status: number, message: string, body: ApiErrorBody | null = null) {
    super(message);
    this.status = status;
    this.body = body;
  }
}

async function apiFetch<T>(path: string, init: RequestInit = {}): Promise<T> {
  const isFormData = init.body instanceof FormData;

  const res = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    credentials: 'include',
    headers: {
      ...(isFormData ? {} : { 'Content-Type': 'application/json' }),
      ...init.headers,
    },
  });

  if (!res.ok) {
    let body: ApiErrorBody | null = null;
    try {
      body = await res.json();
    } catch {
      // no JSON body
    }
    throw new ApiError(res.status, body?.message ?? `요청에 실패했습니다 (${res.status})`, body);
  }

  if (res.status === 204) return undefined as T;
  return res.json() as Promise<T>;
}

// --- Auth ---

export function login(username: string, password: string) {
  return apiFetch<{ username: string; expiresIn: number }>('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  });
}

export function me() {
  return apiFetch<{ username: string; email: string }>('/api/auth/me');
}

export function logout() {
  return apiFetch<void>('/api/auth/logout', { method: 'POST' });
}

// --- Projects ---

export function getAdminProjects() {
  return apiFetch<ProjectListItem[]>('/api/admin/projects');
}

/**
 * There's no dedicated admin detail endpoint — the public detail endpoint already returns
 * everything (features, troubleshooting, tech stacks) needed to populate the edit screen,
 * so the BO edit form reuses it for reads. All writes still go through /api/admin/**.
 */
export function getProjectDetail(id: number | string) {
  return apiFetch<ProjectDetail>(`/api/projects/${id}`);
}

export function createProject(data: ProjectRequest) {
  return apiFetch<ProjectListItem>('/api/admin/projects', {
    method: 'POST',
    body: JSON.stringify(data),
  });
}

export function updateProject(id: number, data: ProjectRequest) {
  return apiFetch<ProjectListItem>(`/api/admin/projects/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

export function deleteProject(id: number) {
  return apiFetch<void>(`/api/admin/projects/${id}`, { method: 'DELETE' });
}

// --- Features ---

export function createFeature(projectId: number, data: FeatureRequest) {
  return apiFetch<ProjectFeature>(`/api/admin/projects/${projectId}/features`, {
    method: 'POST',
    body: JSON.stringify(data),
  });
}

export function updateFeature(id: number, data: FeatureRequest) {
  return apiFetch<ProjectFeature>(`/api/admin/features/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

export function deleteFeature(id: number) {
  return apiFetch<void>(`/api/admin/features/${id}`, { method: 'DELETE' });
}

// --- Troubleshooting ---

export function createTroubleshooting(featureId: number, data: TroubleshootingRequest) {
  return apiFetch<Troubleshooting>(`/api/admin/features/${featureId}/troubleshootings`, {
    method: 'POST',
    body: JSON.stringify(data),
  });
}

export function updateTroubleshooting(id: number, data: TroubleshootingRequest) {
  return apiFetch<Troubleshooting>(`/api/admin/troubleshootings/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

export function deleteTroubleshooting(id: number) {
  return apiFetch<void>(`/api/admin/troubleshootings/${id}`, { method: 'DELETE' });
}

// --- Tech stacks ---

export function getAdminTechStacks() {
  return apiFetch<TechStack[]>('/api/admin/tech-stacks');
}

export function createTechStack(data: TechStackRequest) {
  return apiFetch<TechStack>('/api/admin/tech-stacks', {
    method: 'POST',
    body: JSON.stringify(data),
  });
}

export function deleteTechStack(id: number) {
  return apiFetch<void>(`/api/admin/tech-stacks/${id}`, { method: 'DELETE' });
}

// --- Images ---

export function uploadImage(file: File) {
  const formData = new FormData();
  formData.append('file', file);
  return apiFetch<{ imageUrl: string }>('/api/admin/images', {
    method: 'POST',
    body: formData,
  });
}
