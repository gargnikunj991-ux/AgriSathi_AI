'use client';

import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { Button } from '@/components/common/Button';
import { LoadingState } from '@/components/common/LoadingState';
import { ErrorState } from '@/components/common/ErrorState';
import {
  Sprout,
  CloudSun,
  Store,
  Landmark,
  Plus,
  ArrowRight,
  MapPin,
  Calendar,
  Droplets,
  Wind,
  CheckCircle2,
} from 'lucide-react';
import { cropService } from '@/lib/api/crop.service';
import { weatherService } from '@/lib/api/weather.service';
import { marketplaceService } from '@/lib/api/marketplace.service';
import { schemeService } from '@/lib/api/scheme.service';
import { Crop, WeatherCurrent, MarketplaceListing, GovernmentScheme } from '@/lib/types';

export default function DashboardPage() {
  const [crops, setCrops] = useState<Crop[]>([]);
  const [weather, setWeather] = useState<WeatherCurrent | null>(null);
  const [listings, setListings] = useState<MarketplaceListing[]>([]);
  const [schemes, setSchemes] = useState<GovernmentScheme[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadData = async () => {
    setLoading(true);
    setError('');
    try {
      const [cropRes, weatherRes, marketRes, schemeRes] = await Promise.all([
        cropService.getCrops(),
        weatherService.getCurrentWeather(),
        marketplaceService.getListings(),
        schemeService.getSchemes(),
      ]);

      if (cropRes.success) setCrops(cropRes.data);
      if (weatherRes.success) setWeather(weatherRes.data);
      if (marketRes.success) setListings(marketRes.data);
      if (schemeRes.success) setSchemes(schemeRes.data);
    } catch (err: unknown) {
      setError((err as Error).message || 'Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  if (loading) return <LoadingState message="Loading your farmer workspace..." variant="skeleton" />;
  if (error) return <ErrorState message={error} onRetry={loadData} />;

  const activeCrops = crops.filter((c) => c.status === 'PLANTED');

  return (
    <div className="space-y-6">
      {/* Farmer Welcome Banner */}
      <div className="bg-gradient-to-r from-emerald-800 to-emerald-900 text-white rounded-xl p-6 shadow-sm flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div className="space-y-1">
          <div className="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full bg-emerald-700/80 text-amber-200 text-xs font-semibold">
            Farmer Portal
          </div>
          <h2 className="text-2xl font-bold tracking-tight">Welcome back, Ramesh Kumar</h2>
          <p className="text-emerald-100 text-xs sm:text-sm flex items-center gap-1.5 pt-0.5">
            <MapPin className="w-3.5 h-3.5 text-amber-300 shrink-0" />
            Raipur, Dehradun, Uttarakhand • 2.5 Acres (Loamy Soil)
          </p>
        </div>

        <div className="flex items-center gap-2 shrink-0">
          <Link href="/crops">
            <Button size="sm" variant="secondary" leftIcon={<Plus className="w-4 h-4" />}>
              Add Crop
            </Button>
          </Link>
          <Link href="/weather">
            <Button size="sm" variant="outline" className="bg-white/10 text-white border-white/20 hover:bg-white/20">
              Weather Forecast
            </Button>
          </Link>
        </div>
      </div>

      {/* 4 Summary Stat Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card className="bg-white shadow-xs border-stone-200/80">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-xs font-semibold text-stone-500 uppercase tracking-wider">
              Active Crops
            </CardTitle>
            <div className="p-2 rounded-lg bg-emerald-50 text-emerald-800">
              <Sprout className="w-4 h-4" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-extrabold text-stone-900">{activeCrops.length} Active</div>
            <p className="text-xs text-stone-500 mt-1 truncate">
              {activeCrops.map((c) => c.cropName).join(', ') || 'No active crops'}
            </p>
          </CardContent>
        </Card>

        <Card className="bg-white shadow-xs border-stone-200/80">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-xs font-semibold text-stone-500 uppercase tracking-wider">
              Current Weather
            </CardTitle>
            <div className="p-2 rounded-lg bg-amber-50 text-amber-800">
              <CloudSun className="w-4 h-4" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-extrabold text-stone-900">{weather?.temperature}°C</div>
            <p className="text-xs text-amber-800 font-medium mt-1">
              {weather?.forecast} • {weather?.humidity}% Humidity
            </p>
          </CardContent>
        </Card>

        <Card className="bg-white shadow-xs border-stone-200/80">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-xs font-semibold text-stone-500 uppercase tracking-wider">
              Market Listings
            </CardTitle>
            <div className="p-2 rounded-lg bg-blue-50 text-blue-800">
              <Store className="w-4 h-4" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-extrabold text-stone-900">{listings.length} Produce</div>
            <p className="text-xs text-stone-500 mt-1">Direct produce & rentals</p>
          </CardContent>
        </Card>

        <Card className="bg-white shadow-xs border-stone-200/80">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-xs font-semibold text-stone-500 uppercase tracking-wider">
              Eligible Schemes
            </CardTitle>
            <div className="p-2 rounded-lg bg-purple-50 text-purple-800">
              <Landmark className="w-4 h-4" />
            </div>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-extrabold text-stone-900">{schemes.length} Schemes</div>
            <p className="text-xs text-emerald-800 font-medium mt-1">100% Match for Uttarakhand</p>
          </CardContent>
        </Card>
      </div>

      {/* Main Grid: Active Crops & Weather Advisory */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Active Crops List */}
        <div className="lg:col-span-2 space-y-4">
          <Card className="bg-white border-stone-200/80">
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>My Farm Crops</CardTitle>
                  <CardDescription>Track status, sowing dates, and harvest schedules</CardDescription>
                </div>
                <Link href="/crops">
                  <Button variant="ghost" size="sm" rightIcon={<ArrowRight className="w-3.5 h-3.5" />}>
                    View All
                  </Button>
                </Link>
              </div>
            </CardHeader>
            <CardContent className="space-y-3">
              {crops.map((crop) => (
                <div
                  key={crop.id}
                  className="p-4 rounded-xl border border-stone-200/80 bg-stone-50/50 hover:bg-stone-50 transition-colors flex flex-col sm:flex-row sm:items-center justify-between gap-3"
                >
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <span className="font-bold text-stone-900 text-base">{crop.cropName}</span>
                      <Badge
                        variant={
                          crop.status === 'PLANTED'
                            ? 'success'
                            : crop.status === 'HARVESTED'
                            ? 'info'
                            : 'error'
                        }
                        size="sm"
                      >
                        {crop.status}
                      </Badge>
                    </div>
                    <div className="flex flex-wrap items-center gap-x-4 gap-y-1 text-xs text-stone-500">
                      <span className="flex items-center gap-1">
                        <Calendar className="w-3.5 h-3.5 text-stone-400" /> Sowed: {crop.sowingDate}
                      </span>
                      <span className="flex items-center gap-1">
                        <Calendar className="w-3.5 h-3.5 text-stone-400" /> Harvest: {crop.harvestDate}
                      </span>
                    </div>
                    {crop.fertilizerRecommendation && (
                      <p className="text-xs text-emerald-800 font-medium pt-1 flex items-center gap-1">
                        <CheckCircle2 className="w-3.5 h-3.5 text-emerald-700 shrink-0" />
                        Guidance: {crop.fertilizerRecommendation}
                      </p>
                    )}
                  </div>

                  <Link href="/crops" className="shrink-0">
                    <Button variant="outline" size="sm">
                      Details
                    </Button>
                  </Link>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>

        {/* Weather Advisory Card */}
        <div className="space-y-4">
          <Card className="bg-white border-stone-200/80">
            <CardHeader className="pb-3">
              <div className="flex items-center justify-between">
                <CardTitle className="text-base">Local Weather Summary</CardTitle>
                <Badge variant="warning" size="sm">Dehradun</Badge>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="p-4 rounded-xl bg-amber-50/70 border border-amber-200/80 text-stone-900 space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-3xl font-extrabold text-amber-900">{weather?.temperature}°C</span>
                  <CloudSun className="w-8 h-8 text-amber-700" />
                </div>
                <p className="text-xs font-semibold text-amber-900">{weather?.forecast}</p>
                <div className="grid grid-cols-2 gap-2 text-xs text-stone-600 pt-2 border-t border-amber-200/60">
                  <span className="flex items-center gap-1">
                    <Droplets className="w-3.5 h-3.5 text-blue-600" /> {weather?.humidity}% Humidity
                  </span>
                  <span className="flex items-center gap-1">
                    <Wind className="w-3.5 h-3.5 text-stone-500" /> {weather?.windSpeed} km/h Wind
                  </span>
                </div>
              </div>

              <div className="space-y-2 pt-1">
                <h4 className="text-xs font-bold text-stone-800 uppercase tracking-wider">Farming Advisory</h4>
                <p className="text-xs text-stone-600 leading-relaxed bg-stone-50 p-3 rounded-lg border border-stone-200/80">
                  Favorable weather for field activities. Low wind speed (11 km/h) enables safe foliar nutrient spraying today.
                </p>
              </div>
            </CardContent>
            <CardFooter>
              <Link href="/weather" className="w-full">
                <Button variant="outline" size="sm" className="w-full" rightIcon={<ArrowRight className="w-3.5 h-3.5" />}>
                  7-Day Detailed Forecast
                </Button>
              </Link>
            </CardFooter>
          </Card>
        </div>
      </div>
    </div>
  );
}
