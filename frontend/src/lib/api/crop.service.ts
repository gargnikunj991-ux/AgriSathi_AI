import { apiClient, USE_MOCK } from './client';
import { mockCropService } from '../mock/mockServices';
import { ApiResponse, Crop, CropRequest } from '../types';

export const cropService = {
  async getCrops(): Promise<ApiResponse<Crop[]>> {
    if (USE_MOCK) return mockCropService.getCrops();
    return apiClient.get<Crop[]>('/crops');
  },

  async addCrop(req: CropRequest): Promise<ApiResponse<Crop>> {
    if (USE_MOCK) return mockCropService.addCrop(req);
    return apiClient.post<Crop>('/crops', req);
  },

  async deleteCrop(id: number): Promise<ApiResponse<void>> {
    if (USE_MOCK) return mockCropService.deleteCrop(id);
    return apiClient.delete<void>(`/crops/${id}`);
  },
};
