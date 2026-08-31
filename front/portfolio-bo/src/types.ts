export interface TechStack {
  id: number;
  name: string;
  category: string;
}

export interface Troubleshooting {
  id: number;
  problem: string;
  analysis: string;
  action: string;
  result: string;
}

export interface ProjectFeature {
  id: number;
  name: string;
  imageUrl: string | null;
  description: string;
  reason: string;
  troubleshooting: Troubleshooting | null;
}

export interface ProjectListItem {
  id: number;
  title: string;
  summary: string;
  thumbnailUrl: string | null;
  techStacks: TechStack[];
}

export interface ProjectDetail {
  id: number;
  title: string;
  summary: string;
  thumbnailUrl: string | null;
  githubUrl: string | null;
  demoUrl: string | null;
  startDate: string | null;
  endDate: string | null;
  techStacks: TechStack[];
  features: ProjectFeature[];
}

export interface ProjectRequest {
  title: string;
  summary: string;
  thumbnailUrl: string | null;
  githubUrl: string | null;
  demoUrl: string | null;
  startDate: string | null;
  endDate: string | null;
  techStackIds: number[];
}

export interface FeatureRequest {
  name: string;
  imageUrl: string | null;
  description: string;
  reason: string;
  sortOrder: number;
}

export interface TroubleshootingRequest {
  problem: string;
  analysis: string;
  action: string;
  result: string;
}

export interface TechStackRequest {
  name: string;
  category: string;
}

export interface ApiErrorBody {
  message: string;
  errors?: Record<string, string>;
  usedByProjectIds?: number[];
}
