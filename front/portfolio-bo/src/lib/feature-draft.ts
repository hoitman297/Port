import type { ProjectFeature, Troubleshooting } from '../types';

export interface FeatureDraft {
  localId: string;
  id?: number;
  name: string;
  imageUrl: string | null;
  description: string;
  reason: string;
  troubleshooting: Troubleshooting | null;
}

export function featureToDraft(feature: ProjectFeature, localId: string): FeatureDraft {
  return {
    localId,
    id: feature.id,
    name: feature.name,
    imageUrl: feature.imageUrl,
    description: feature.description,
    reason: feature.reason,
    troubleshooting: feature.troubleshooting,
  };
}
