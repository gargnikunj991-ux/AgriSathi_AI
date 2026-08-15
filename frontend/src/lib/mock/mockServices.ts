import {
  ApiResponse,
  User,
  FarmerProfile,
  ProfileUpdateRequest,
  Crop,
  CropRequest,
  WeatherCurrent,
  DailyForecast,
  WeatherForecastResponse,
  FarmingSummaryResponse,
  MarketplaceListing,
  CreateListingRequest,
  ContactSellerRequest,
  ContactSellerResponse,
  GovernmentScheme,
  SchemeRecommendation,
} from '../types';
import {
  MOCK_USER,
  MOCK_FARMER_PROFILE,
  MOCK_CROPS,
  MOCK_CURRENT_WEATHER,
  MOCK_DAILY_FORECASTS,
  MOCK_FARMING_SUMMARY,
  MOCK_MARKETPLACE_LISTINGS,
  MOCK_GOVERNMENT_SCHEMES,
} from './mockData';

// Simulated delay helper
const delay = (ms = 300) => new Promise((resolve) => setTimeout(resolve, ms));

let cropsList: Crop[] = [...MOCK_CROPS];
let listingsList: MarketplaceListing[] = [...MOCK_MARKETPLACE_LISTINGS];
let farmerProfile: FarmerProfile = { ...MOCK_FARMER_PROFILE };

export const mockAuthService = {
  async getMe(): Promise<ApiResponse<User>> {
    await delay();
    return {
      success: true,
      message: 'User profile retrieved successfully',
      data: MOCK_USER,
    };
  },
  async getProfile(): Promise<ApiResponse<FarmerProfile>> {
    await delay();
    return {
      success: true,
      message: 'Farmer profile retrieved successfully',
      data: farmerProfile,
    };
  },
  async updateProfile(req: ProfileUpdateRequest): Promise<ApiResponse<FarmerProfile>> {
    await delay();
    farmerProfile = { ...req };
    return {
      success: true,
      message: 'Profile updated successfully',
      data: farmerProfile,
    };
  },
};

export const mockCropService = {
  async getCrops(): Promise<ApiResponse<Crop[]>> {
    await delay();
    return {
      success: true,
      message: 'Crops retrieved successfully',
      data: cropsList,
    };
  },
  async addCrop(req: CropRequest): Promise<ApiResponse<Crop>> {
    await delay();
    const newCrop: Crop = {
      id: Date.now(),
      cropName: req.cropName,
      sowingDate: req.sowingDate,
      harvestDate: req.harvestDate,
      status: 'PLANTED',
      soilType: req.soilType || farmerProfile.soilType,
      growingGuidance: [
        'Ensure proper irrigation during early root growth.',
        'Monitor field weekly for weeds and nutrient deficiencies.',
      ],
    };
    cropsList.push(newCrop);
    return {
      success: true,
      message: 'Crop added successfully',
      data: newCrop,
    };
  },
  async deleteCrop(id: number): Promise<ApiResponse<void>> {
    await delay();
    cropsList = cropsList.filter((c) => c.id !== id);
    return {
      success: true,
      message: 'Crop removed successfully',
      data: undefined as unknown as void,
    };
  },
};

export const mockWeatherService = {
  async getCurrentWeather(lat?: number, lon?: number): Promise<ApiResponse<WeatherCurrent>> {
    await delay();
    return {
      success: true,
      message: 'Current weather retrieved successfully',
      data: MOCK_CURRENT_WEATHER,
    };
  },
  async getForecast(days = 7): Promise<ApiResponse<WeatherForecastResponse>> {
    await delay();
    return {
      success: true,
      message: 'Weather forecast retrieved successfully',
      data: {
        latitude: MOCK_CURRENT_WEATHER.latitude!,
        longitude: MOCK_CURRENT_WEATHER.longitude!,
        timezone: 'Asia/Kolkata',
        days,
        dailyForecasts: MOCK_DAILY_FORECASTS.slice(0, days),
      },
    };
  },
  async getFarmingSummary(): Promise<ApiResponse<FarmingSummaryResponse>> {
    await delay();
    return {
      success: true,
      message: 'Farming weather summary retrieved successfully',
      data: MOCK_FARMING_SUMMARY,
    };
  },
};

export const mockMarketplaceService = {
  async getListings(filters?: { cropName?: string; location?: string }): Promise<ApiResponse<MarketplaceListing[]>> {
    await delay();
    let result = [...listingsList];
    if (filters?.cropName) {
      result = result.filter((item) => item.cropName.toLowerCase().includes(filters.cropName!.toLowerCase()));
    }
    if (filters?.location) {
      result = result.filter((item) => item.location.toLowerCase().includes(filters.location!.toLowerCase()));
    }
    return {
      success: true,
      message: 'Listings retrieved successfully',
      data: result,
    };
  },
  async createListing(req: CreateListingRequest): Promise<ApiResponse<MarketplaceListing>> {
    await delay();
    const newListing: MarketplaceListing = {
      id: Date.now(),
      cropName: req.cropName,
      description: req.description,
      imageUrl:
        req.imageUrl ||
        'https://images.unsplash.com/photo-1586201375761-83865001e31c?q=80&w=800&auto=format&fit=crop',
      quantity: req.quantity,
      price: req.price,
      unit: req.unit,
      location: req.location,
      status: 'AVAILABLE',
      sellerName: MOCK_USER.name,
      sellerPhone: MOCK_USER.phone,
      sellerEmail: MOCK_USER.email,
    };
    listingsList.unshift(newListing);
    return {
      success: true,
      message: 'Listing created successfully',
      data: newListing,
    };
  },
  async contactSeller(id: number, req: ContactSellerRequest): Promise<ApiResponse<ContactSellerResponse>> {
    await delay();
    const listing = listingsList.find((l) => l.id === id) || listingsList[0];
    return {
      success: true,
      message: 'Inquiry sent to seller successfully',
      data: {
        listingId: listing.id,
        cropName: listing.cropName,
        quantity: listing.quantity,
        price: listing.price,
        unit: listing.unit,
        location: listing.location,
        sellerName: listing.sellerName || 'Ramesh Kumar',
        sellerPhone: listing.sellerPhone || '9876543210',
        sellerEmail: listing.sellerEmail || 'ramesh@agrisathi.com',
        contactStatus: 'INQUIRY_SENT',
        formattedInquiry: `Inquiry sent: ${req.buyerName} interested in purchasing produce from listing '${listing.cropName}'.`,
      },
    };
  },
};

export const mockSchemeService = {
  async getSchemes(filters?: { state?: string; crop?: string }): Promise<ApiResponse<GovernmentScheme[]>> {
    await delay();
    let result = [...MOCK_GOVERNMENT_SCHEMES];
    if (filters?.state && filters.state !== 'All') {
      result = result.filter(
        (s) => s.state === 'All India' || s.state.toLowerCase() === filters.state!.toLowerCase()
      );
    }
    return {
      success: true,
      message: 'Government schemes retrieved successfully',
      data: result,
    };
  },
  async getRecommendations(): Promise<ApiResponse<SchemeRecommendation[]>> {
    await delay();
    const recs: SchemeRecommendation[] = MOCK_GOVERNMENT_SCHEMES.map((scheme) => {
      let score = 80;
      let reason = 'General Agricultural Eligibility';
      if (scheme.state === farmerProfile.state) {
        score = 100;
        reason = `Direct State Match (${farmerProfile.state}) | Acreage Match (${farmerProfile.farmSize} Acres)`;
      } else if (scheme.state === 'All India') {
        score = 90;
        reason = `Central Scheme Eligible for ${farmerProfile.state} Farmers`;
      }
      return { scheme, matchScore: score, matchReason: reason };
    });
    return {
      success: true,
      message: 'Scheme recommendations retrieved successfully',
      data: recs,
    };
  },
};
