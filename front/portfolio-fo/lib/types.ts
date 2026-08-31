export interface TechStack {
  id: number;
  name: string;
  category: string;
}

export interface Troubleshooting {
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
