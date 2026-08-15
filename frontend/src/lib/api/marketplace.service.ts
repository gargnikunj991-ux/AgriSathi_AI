import { apiClient, USE_MOCK } from './client';
import { mockMarketplaceService } from '../mock/mockServices';
import { ApiResponse, MarketplaceListing, CreateListingRequest, ContactSellerRequest, ContactSellerResponse } from '../types';

export const marketplaceService = {
  async getListings(params?: { cropName?: string; location?: string }): Promise<ApiResponse<MarketplaceListing[]>> {
    if (USE_MOCK) return mockMarketplaceService.getListings(params);
    const query = new URLSearchParams(params as Record<string, string>).toString();
    return apiClient.get<MarketplaceListing[]>(`/marketplace/listings${query ? `?${query}` : ''}`);
  },

  async createListing(req: CreateListingRequest): Promise<ApiResponse<MarketplaceListing>> {
    if (USE_MOCK) return mockMarketplaceService.createListing(req);
    return apiClient.post<MarketplaceListing>('/marketplace/listings', req);
  },

  async contactSeller(id: number, req: ContactSellerRequest): Promise<ApiResponse<ContactSellerResponse>> {
    if (USE_MOCK) return mockMarketplaceService.contactSeller(id, req);
    return apiClient.post<ContactSellerResponse>(`/marketplace/listings/${id}/contact`, req);
  },
};
