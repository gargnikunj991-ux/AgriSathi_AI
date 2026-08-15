import { apiClient, USE_MOCK } from './client';
import { mockWeatherService } from '../mock/mockServices';
import { ApiResponse, WeatherCurrent, WeatherForecastResponse, FarmingSummaryResponse } from '../types';

export const weatherService = {
  async getCurrentWeather(lat = 30.3165, lon = 78.0322): Promise<ApiResponse<WeatherCurrent>> {
    if (USE_MOCK) return mockWeatherService.getCurrentWeather(lat, lon);
    return apiClient.get<WeatherCurrent>(`/weather/current?lat=${lat}&lon=${lon}`);
  },

  async getForecast(days = 7, lat = 30.3165, lon = 78.0322): Promise<ApiResponse<WeatherForecastResponse>> {
    if (USE_MOCK) return mockWeatherService.getForecast(days);
    return apiClient.get<WeatherForecastResponse>(`/weather/forecast?lat=${lat}&lon=${lon}&days=${days}`);
  },

  async getFarmingSummary(lat = 30.3165, lon = 78.0322): Promise<ApiResponse<FarmingSummaryResponse>> {
    if (USE_MOCK) return mockWeatherService.getFarmingSummary();
    return apiClient.get<FarmingSummaryResponse>(`/weather/farming-summary?lat=${lat}&lon=${lon}`);
  },
};
