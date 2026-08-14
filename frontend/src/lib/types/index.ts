/**
 * AgriSathi AI - TypeScript Domain Interfaces (Non-AI MVP)
 * Mapped to Spring Boot REST APIs (api_contract.md)
 */

// ----------------------------------------------------
// 1. Generic API Envelope
// ----------------------------------------------------
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  errors?: string[];
  timestamp?: string;
}

// ----------------------------------------------------
// 2. Authentication & Roles
// ----------------------------------------------------
export type UserRole = 'FARMER' | 'BUYER' | 'ADMIN';

export interface User {
  id: number;
  name: string;
  email: string;
  phone: string;
  role: UserRole;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
  role: UserRole;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthTokenResponse {
  accessToken: string;
  expiresIn: number;
  user: User;
}

// ----------------------------------------------------
// 3. Farmer Profile
// ----------------------------------------------------
export interface FarmerProfile {
  state: string;
  district: string;
  village: string;
  farmSize: number; // in Acres
  soilType: string;
  mainCrop: string;
}

export interface ProfileUpdateRequest {
  state: string;
  district: string;
  village: string;
  farmSize: number;
  soilType: string;
  mainCrop: string;
}

// ----------------------------------------------------
// 4. Crop Management & Growing Guidance
// ----------------------------------------------------
export type CropStatus = 'PLANTED' | 'HARVESTED' | 'FAILED';

export interface Crop {
  id: number;
  cropName: string;
  sowingDate: string; // YYYY-MM-DD
  harvestDate: string; // YYYY-MM-DD
  status: CropStatus;
  soilType?: string;
  fertilizerRecommendation?: string;
  growingGuidance?: string[];
}

export interface CropRequest {
  cropName: string;
  sowingDate: string;
  harvestDate: string;
  soilType?: string;
}

// ----------------------------------------------------
// 5. Weather & Agricultural Advisories
// ----------------------------------------------------
export interface WeatherCurrent {
  latitude?: number;
  longitude?: number;
  temperature: number;
  apparentTemperature?: number;
  humidity: number;
  windSpeed: number;
  windDirection?: number;
  pressure?: number;
  precipitation?: number;
  weatherCondition: string;
  weatherCode?: number;
  uvIndex?: number;
  forecast: string;
  timestamp?: string;
}

export interface DailyForecast {
  date: string;
  tempMin: number;
  tempMax: number;
  precipitationProbability: number;
  precipitation: number;
  windSpeedMax: number;
  weatherCondition: string;
  weatherCode: number;
  uvIndexMax: number;
}

export interface WeatherForecastResponse {
  latitude: number;
  longitude: number;
  timezone: string;
  days: number;
  dailyForecasts: DailyForecast[];
}

export interface FarmingSummaryResponse {
  latitude?: number;
  longitude?: number;
  currentWeather: {
    temperature: number;
    humidity: number;
    windSpeed: number;
    precipitation: number;
    weatherCondition: string;
  };
  overallAdvisory: string;
  irrigationAdvice: string;
  sprayingCondition: string;
  frostRisk: string;
  heatStressRisk: string;
  sowingSuitability: string;
  harvestingSuitability: string;
  actionItems: string[];
}

// ----------------------------------------------------
// 6. Agricultural Marketplace & Rentals
// ----------------------------------------------------
export type ListingStatus = 'AVAILABLE' | 'SOLD';

export interface MarketplaceListing {
  id: number;
  cropName: string;
  description: string;
  imageUrl?: string;
  quantity: number;
  price: number;
  unit: string;
  location: string;
  status: ListingStatus;
  sellerName?: string;
  sellerPhone?: string;
  sellerEmail?: string;
}

export interface CreateListingRequest {
  cropName: string;
  description: string;
  imageUrl?: string;
  quantity: number;
  price: number;
  unit: string;
  location: string;
}

export interface ContactSellerRequest {
  message: string;
  buyerName: string;
  buyerPhone: string;
  buyerEmail: string;
}

export interface ContactSellerResponse {
  listingId: number;
  cropName: string;
  quantity: number;
  price: number;
  unit: string;
  location: string;
  sellerName: string;
  sellerPhone: string;
  sellerEmail: string;
  contactStatus: string;
  formattedInquiry: string;
}

// ----------------------------------------------------
// 7. Government Schemes Navigator
// ----------------------------------------------------
export interface GovernmentScheme {
  id: number;
  title: string;
  description: string;
  state: string;
  targetCrop: string;
  eligibility: string;
  category: string;
  benefitAmount: string;
  minFarmSize?: number;
  maxFarmSize?: number;
  applyLink: string;
  isActive: boolean;
  requiredDocuments?: string[];
}

export interface SchemeRecommendation {
  scheme: GovernmentScheme;
  matchScore: number;
  matchReason: string;
}
