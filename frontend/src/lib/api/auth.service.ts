import { apiClient, USE_MOCK } from './client';
import { mockAuthService } from '../mock/mockServices';
import { ApiResponse, User, LoginRequest, RegisterRequest, AuthTokenResponse, FarmerProfile, ProfileUpdateRequest } from '../types';

export const authService = {
  async login(req: LoginRequest): Promise<ApiResponse<AuthTokenResponse>> {
    if (USE_MOCK) {
      const user: User = {
        id: 101,
        name: 'Ramesh Kumar',
        email: req.email,
        phone: '9876543210',
        role: 'FARMER',
      };
      if (typeof window !== 'undefined') {
        localStorage.setItem('agrisathi_token', 'mock_jwt_token_123');
      }
      return {
        success: true,
        message: 'Login successful',
        data: { accessToken: 'mock_jwt_token_123', expiresIn: 1800, user },
      };
    }
    return apiClient.post<AuthTokenResponse>('/auth/login', req);
  },

  async register(req: RegisterRequest): Promise<ApiResponse<void>> {
    if (USE_MOCK) {
      return { success: true, message: 'User registered successfully', data: undefined as unknown as void };
    }
    return apiClient.post<void>('/auth/register', req);
  },

  async getMe(): Promise<ApiResponse<User>> {
    if (USE_MOCK) return mockAuthService.getMe();
    return apiClient.get<User>('/auth/me');
  },

  async getProfile(): Promise<ApiResponse<FarmerProfile>> {
    if (USE_MOCK) return mockAuthService.getProfile();
    return apiClient.get<FarmerProfile>('/farmer/profile');
  },

  async updateProfile(req: ProfileUpdateRequest): Promise<ApiResponse<FarmerProfile>> {
    if (USE_MOCK) return mockAuthService.updateProfile(req);
    return apiClient.put<FarmerProfile>('/farmer/profile', req);
  },
};
