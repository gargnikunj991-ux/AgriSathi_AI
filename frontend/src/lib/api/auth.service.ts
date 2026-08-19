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
    const res = await apiClient.post<AuthTokenResponse>('/auth/login', req);
    if (res.success && res.data?.accessToken && typeof window !== 'undefined') {
      localStorage.setItem('agrisathi_token', res.data.accessToken);
      try {
        const meRes = await apiClient.get<User>('/auth/me');
        if (meRes.success && meRes.data) {
          localStorage.setItem('agrisathi_user', JSON.stringify(meRes.data));
        }
      } catch {
        // me fetch fallback
      }
    }
    return res;
  },

  logout(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('agrisathi_token');
      localStorage.removeItem('agrisathi_user');
    }
  },

  getStoredUser(): User | null {
    if (typeof window === 'undefined') return null;
    const cached = localStorage.getItem('agrisathi_user');
    if (!cached) return null;
    try {
      return JSON.parse(cached);
    } catch {
      return null;
    }
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
