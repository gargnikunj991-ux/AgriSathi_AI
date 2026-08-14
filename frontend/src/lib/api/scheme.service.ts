import { apiClient, USE_MOCK } from './client';
import { mockSchemeService } from '../mock/mockServices';
import { ApiResponse, GovernmentScheme, SchemeRecommendation } from '../types';

export const schemeService = {
  async getSchemes(params?: { state?: string; crop?: string }): Promise<ApiResponse<GovernmentScheme[]>> {
    if (USE_MOCK) return mockSchemeService.getSchemes(params);
    const query = new URLSearchParams(params as Record<string, string>).toString();
    return apiClient.get<GovernmentScheme[]>(`/government-schemes${query ? `?${query}` : ''}`);
  },

  async getRecommendations(): Promise<ApiResponse<SchemeRecommendation[]>> {
    if (USE_MOCK) return mockSchemeService.getRecommendations();
    return apiClient.get<SchemeRecommendation[]>('/government-schemes/recommendations');
  },
};
