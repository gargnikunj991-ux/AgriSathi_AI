'use client';

import React, { useEffect, useState } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { LoadingState } from '@/components/common/LoadingState';
import { ErrorState } from '@/components/common/ErrorState';
import {
  CloudSun,
  Droplets,
  Wind,
  Sun,
  CloudRain,
  MapPin,
  CheckCircle2,
  AlertCircle,
  Thermometer,
} from 'lucide-react';
import { weatherService } from '@/lib/api/weather.service';
import { WeatherCurrent, DailyForecast, FarmingSummaryResponse } from '@/lib/types';

export default function WeatherPage() {
  const [current, setCurrent] = useState<WeatherCurrent | null>(null);
  const [forecast, setForecast] = useState<DailyForecast[]>([]);
  const [summary, setSummary] = useState<FarmingSummaryResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchWeatherData = async () => {
    setLoading(true);
    setError('');
    try {
      const [currentRes, forecastRes, summaryRes] = await Promise.all([
        weatherService.getCurrentWeather(),
        weatherService.getForecast(7),
        weatherService.getFarmingSummary(),
      ]);

      if (currentRes.success) setCurrent(currentRes.data);
      if (forecastRes.success) setForecast(forecastRes.data.dailyForecasts);
      if (summaryRes.success) setSummary(summaryRes.data);
    } catch (err: unknown) {
      setError((err as Error).message || 'Failed to load weather data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWeatherData();
  }, []);

  if (loading) return <LoadingState message="Fetching hyperlocal weather forecast..." variant="skeleton" />;
  if (error) return <ErrorState message={error} onRetry={fetchWeatherData} />;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-stone-900">Weather & Agricultural Advisories</h2>
          <p className="text-sm text-stone-500 flex items-center gap-1 mt-0.5">
            <MapPin className="w-3.5 h-3.5 text-stone-400" /> Raipur, Dehradun Valley (30.3165° N, 78.0322° E)
          </p>
        </div>
        <Badge variant="warning" size="md">
          Updated Today 14:30
        </Badge>
      </div>

      {/* Current Weather Widget Banner */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-2 bg-gradient-to-br from-amber-500 to-amber-600 text-white border-none shadow-md">
          <CardContent className="p-6 sm:p-8 space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div>
                <span className="text-xs font-semibold uppercase tracking-wider text-amber-100">
                  Current Atmosphere
                </span>
                <div className="flex items-baseline gap-3 mt-1">
                  <span className="text-5xl font-extrabold">{current?.temperature}°C</span>
                  <span className="text-amber-100 text-sm">Feels like {current?.apparentTemperature}°C</span>
                </div>
                <p className="text-lg font-semibold text-amber-50 mt-1">{current?.weatherCondition}</p>
              </div>

              <div className="p-4 rounded-2xl bg-white/10 backdrop-blur-xs text-white flex items-center justify-center shrink-0">
                <Sun className="w-16 h-16 text-amber-200" />
              </div>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 pt-4 border-t border-amber-400/40 text-amber-50 text-xs">
              <div>
                <span className="text-amber-200 block">Humidity</span>
                <span className="text-base font-bold text-white flex items-center gap-1 mt-0.5">
                  <Droplets className="w-4 h-4 text-sky-200" /> {current?.humidity}%
                </span>
              </div>
              <div>
                <span className="text-amber-200 block">Wind Speed</span>
                <span className="text-base font-bold text-white flex items-center gap-1 mt-0.5">
                  <Wind className="w-4 h-4" /> {current?.windSpeed} km/h
                </span>
              </div>
              <div>
                <span className="text-amber-200 block">Rainfall</span>
                <span className="text-base font-bold text-white flex items-center gap-1 mt-0.5">
                  <CloudRain className="w-4 h-4 text-sky-200" /> {current?.precipitation || 0} mm
                </span>
              </div>
              <div>
                <span className="text-amber-200 block">UV Index</span>
                <span className="text-base font-bold text-white flex items-center gap-1 mt-0.5">
                  <Thermometer className="w-4 h-4 text-amber-200" /> {current?.uvIndex || 6.0} (Moderate)
                </span>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Agricultural Action Items Summary */}
        <Card className="bg-white border-stone-200/80 shadow-xs flex flex-col justify-between">
          <CardHeader className="pb-3">
            <CardTitle className="text-base">Farming Action Items</CardTitle>
            <CardDescription>Tailored for your Basmati Rice crop</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3 pt-1">
            {summary?.actionItems.map((item, idx) => (
              <div key={idx} className="p-3 rounded-lg bg-stone-50 border border-stone-200/80 text-xs text-stone-700 flex items-start gap-2">
                <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0 mt-0.5" />
                <span>{item}</span>
              </div>
            ))}
          </CardContent>
        </Card>
      </div>

      {/* 7-Day Forecast Cards */}
      <div className="space-y-3">
        <h3 className="text-base font-bold text-stone-900">7-Day Daily Forecast</h3>
        <div className="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-7 gap-3">
          {forecast.map((day, idx) => (
            <Card key={idx} className={`bg-white border-stone-200/80 text-center p-3 ${idx === 0 ? 'ring-2 ring-emerald-600' : ''}`}>
              <span className="text-xs font-bold text-stone-700 block">
                {idx === 0 ? 'Today' : day.date.slice(5)}
              </span>
              <div className="my-2 flex justify-center text-amber-600">
                {day.precipitationProbability > 40 ? (
                  <CloudRain className="w-7 h-7 text-sky-600" />
                ) : (
                  <CloudSun className="w-7 h-7" />
                )}
              </div>
              <span className="text-xs font-semibold text-stone-900 block">
                {day.tempMax}° / {day.tempMin}°
              </span>
              <span className="text-[11px] text-stone-500 block truncate mt-0.5">
                {day.weatherCondition}
              </span>
              <span className="text-[10px] text-sky-700 font-semibold block mt-1">
                {day.precipitationProbability}% Rain
              </span>
            </Card>
          ))}
        </div>
      </div>

      {/* Detailed Agricultural Advisory Cards */}
      {summary && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Card className="bg-white border-stone-200/80">
            <CardHeader className="pb-2">
              <div className="flex items-center gap-2">
                <Droplets className="w-5 h-5 text-blue-600" />
                <CardTitle className="text-base">Irrigation Guidance</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-xs text-stone-600 leading-relaxed bg-blue-50/50 p-3 rounded-lg border border-blue-100">
                {summary.irrigationAdvice}
              </p>
            </CardContent>
          </Card>

          <Card className="bg-white border-stone-200/80">
            <CardHeader className="pb-2">
              <div className="flex items-center gap-2">
                <Wind className="w-5 h-5 text-emerald-700" />
                <CardTitle className="text-base">Spraying Conditions</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-xs text-stone-600 leading-relaxed bg-emerald-50/50 p-3 rounded-lg border border-emerald-100">
                {summary.sprayingCondition}
              </p>
            </CardContent>
          </Card>

          <Card className="bg-white border-stone-200/80">
            <CardHeader className="pb-2">
              <div className="flex items-center gap-2">
                <AlertCircle className="w-5 h-5 text-amber-600" />
                <CardTitle className="text-base">Frost & Heat Stress Risk</CardTitle>
              </div>
            </CardHeader>
            <CardContent className="space-y-2 text-xs text-stone-600">
              <p className="p-2.5 rounded-lg bg-stone-50 border border-stone-200/80">
                <strong>Frost Risk:</strong> {summary.frostRisk}
              </p>
              <p className="p-2.5 rounded-lg bg-stone-50 border border-stone-200/80">
                <strong>Heat Stress:</strong> {summary.heatStressRisk}
              </p>
            </CardContent>
          </Card>

          <Card className="bg-white border-stone-200/80">
            <CardHeader className="pb-2">
              <div className="flex items-center gap-2">
                <CheckCircle2 className="w-5 h-5 text-emerald-700" />
                <CardTitle className="text-base">Sowing & Harvesting Suitability</CardTitle>
              </div>
            </CardHeader>
            <CardContent className="space-y-2 text-xs text-stone-600">
              <p className="p-2.5 rounded-lg bg-stone-50 border border-stone-200/80">
                <strong>Sowing:</strong> {summary.sowingSuitability}
              </p>
              <p className="p-2.5 rounded-lg bg-stone-50 border border-stone-200/80">
                <strong>Harvesting:</strong> {summary.harvestingSuitability}
              </p>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
}
