import React from 'react';
import Link from 'next/link';
import {
  Sprout,
  CloudSun,
  Store,
  Landmark,
  ArrowRight,
  ShieldCheck,
  CheckCircle2,
  TrendingUp,
  MapPin,
} from 'lucide-react';
import { Button } from '@/components/common/Button';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';

export default function Home() {
  return (
    <div className="min-h-screen bg-[#faf9f5] text-stone-900 flex flex-col justify-between selection:bg-emerald-100">
      {/* Navigation Header */}
      <header className="h-20 border-b border-stone-200/80 bg-white/90 backdrop-blur-xs sticky top-0 z-40 px-4 sm:px-8 flex items-center justify-between shadow-2xs">
        <div className="flex items-center gap-3">
          <div className="p-2 rounded-xl bg-emerald-800 text-amber-300 shadow-xs">
            <Sprout className="w-6 h-6" />
          </div>
          <div>
            <span className="text-xl font-bold tracking-tight text-stone-900">AgriSathi</span>
            <span className="text-xs font-semibold text-emerald-800 block -mt-1">Farming Platform</span>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <Link href="/login">
            <Button variant="ghost" size="md" className="font-semibold text-stone-700 hover:text-stone-900">
              Sign In
            </Button>
          </Link>
          <Link href="/dashboard">
            <Button variant="primary" size="md" rightIcon={<ArrowRight className="w-4 h-4" />}>
              Get Started
            </Button>
          </Link>
        </div>
      </header>

      {/* Hero Section */}
      <section className="relative pt-12 sm:pt-16 pb-16 px-4 sm:px-6 lg:px-8 max-w-6xl w-full mx-auto">
        <div className="text-center max-w-3xl mx-auto space-y-6">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-emerald-50 text-emerald-800 text-xs sm:text-sm font-semibold border border-emerald-200/80 shadow-2xs">
            <ShieldCheck className="w-4 h-4 text-emerald-700" />
            <span>Trusted Agricultural Companion for Indian Farmers</span>
          </div>

          <h1 className="text-4xl sm:text-6xl font-extrabold text-stone-900 tracking-tight leading-[1.15]">
            Smarter Farming. <span className="text-emerald-800">Simpler Decisions.</span>
          </h1>

          <p className="text-base sm:text-xl text-stone-600 max-w-2xl mx-auto leading-relaxed font-normal">
            Manage your crops, understand the weather, discover better market opportunities, and find government schemes — all in one place.
          </p>

          <div className="pt-4 flex flex-col sm:flex-row items-center justify-center gap-4">
            <Link href="/dashboard" className="w-full sm:w-auto">
              <Button size="lg" className="w-full sm:w-auto px-8 text-base shadow-sm" rightIcon={<ArrowRight className="w-5 h-5" />}>
                Get Started
              </Button>
            </Link>
            <Link href="/login" className="w-full sm:w-auto">
              <Button variant="outline" size="lg" className="w-full sm:w-auto px-8 text-base border-stone-300 text-stone-700 hover:bg-stone-100">
                Explore Platform
              </Button>
            </Link>
          </div>
        </div>

        {/* Practical Farming Showcase Card */}
        <div className="mt-14 max-w-4xl mx-auto">
          <Card className="border-stone-200/90 shadow-md bg-white p-2 sm:p-4 rounded-2xl overflow-hidden">
            <div className="bg-stone-50 border border-stone-200/80 rounded-xl p-4 sm:p-6 space-y-4">
              <div className="flex flex-wrap items-center justify-between gap-3 border-b border-stone-200/80 pb-4">
                <div className="flex items-center gap-2.5">
                  <div className="w-9 h-9 rounded-full bg-emerald-800 text-white flex items-center justify-center font-bold text-sm">
                    RK
                  </div>
                  <div>
                    <h4 className="text-sm font-bold text-stone-900">Ramesh Kumar&apos;s Farm</h4>
                    <p className="text-xs text-stone-500 flex items-center gap-1">
                      <MapPin className="w-3 h-3 text-stone-400" /> Raipur, Dehradun • 2.5 Acres (Loamy Soil)
                    </p>
                  </div>
                </div>
                <Badge variant="success">FARMER MVP</Badge>
              </div>

              {/* Sample Live Stats Grid */}
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 pt-1">
                <div className="bg-white p-3 rounded-lg border border-stone-200/80">
                  <span className="text-xs text-stone-500 block">Main Crop</span>
                  <span className="text-sm font-bold text-stone-900">Basmati Rice</span>
                  <span className="text-[11px] text-emerald-700 block mt-0.5 font-medium">Planted Jul 30</span>
                </div>
                <div className="bg-white p-3 rounded-lg border border-stone-200/80">
                  <span className="text-xs text-stone-500 block">Today Weather</span>
                  <span className="text-sm font-bold text-stone-900">31°C Clear</span>
                  <span className="text-[11px] text-amber-700 block mt-0.5 font-medium">Safe to Spray</span>
                </div>
                <div className="bg-white p-3 rounded-lg border border-stone-200/80">
                  <span className="text-xs text-stone-500 block">Produce Listing</span>
                  <span className="text-sm font-bold text-stone-900">250kg Rice</span>
                  <span className="text-[11px] text-stone-600 block mt-0.5 font-medium">₹35/kg • Dehradun</span>
                </div>
                <div className="bg-white p-3 rounded-lg border border-stone-200/80">
                  <span className="text-xs text-stone-500 block">Scheme Eligibility</span>
                  <span className="text-sm font-bold text-stone-900">PM-KISAN</span>
                  <span className="text-[11px] text-emerald-700 block mt-0.5 font-medium">100% Match</span>
                </div>
              </div>
            </div>
          </Card>
        </div>
      </section>

      {/* Core Feature Matrix */}
      <section className="py-16 px-4 sm:px-6 lg:px-8 max-w-6xl w-full mx-auto border-t border-stone-200/70">
        <div className="text-center max-w-2xl mx-auto mb-12 space-y-3">
          <h2 className="text-2xl sm:text-3xl font-extrabold text-stone-900 tracking-tight">
            Built for Practical Everyday Farming Workflows
          </h2>
          <p className="text-sm sm:text-base text-stone-600">
            Four dedicated modules designed to simplify farm tracking, weather planning, marketplace selling, and government scheme access.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="hover:border-emerald-300 transition-colors bg-white shadow-xs">
            <CardHeader className="p-6">
              <div className="p-3 w-fit rounded-xl bg-emerald-50 text-emerald-800 border border-emerald-100 mb-3">
                <Sprout className="w-6 h-6" />
              </div>
              <CardTitle className="text-lg">Crop Management</CardTitle>
              <CardDescription className="text-stone-600 mt-1">
                Log sowing dates, monitor harvest timelines, track soil compatibility, and access growing guidance tailored to your crops.
              </CardDescription>
              <ul className="mt-4 space-y-2 text-xs text-stone-600">
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Track PLANTED, HARVESTED, and FAILED status
                </li>
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Soil-based fertilizer dosage guidelines
                </li>
              </ul>
            </CardHeader>
          </Card>

          <Card className="hover:border-emerald-300 transition-colors bg-white shadow-xs">
            <CardHeader className="p-6">
              <div className="p-3 w-fit rounded-xl bg-amber-50 text-amber-800 border border-amber-100 mb-3">
                <CloudSun className="w-6 h-6" />
              </div>
              <CardTitle className="text-lg">Weather & Agricultural Advisories</CardTitle>
              <CardDescription className="text-stone-600 mt-1">
                Get current temperature, humidity, 7-day daily forecasts, rain probability, and practical irrigation or spraying guidance.
              </CardDescription>
              <ul className="mt-4 space-y-2 text-xs text-stone-600">
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Daily temperature min/max & rain chance
                </li>
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Irrigation & spraying condition warnings
                </li>
              </ul>
            </CardHeader>
          </Card>

          <Card className="hover:border-emerald-300 transition-colors bg-white shadow-xs">
            <CardHeader className="p-6">
              <div className="p-3 w-fit rounded-xl bg-blue-50 text-blue-800 border border-blue-100 mb-3">
                <Store className="w-6 h-6" />
              </div>
              <CardTitle className="text-lg">Produce & Equipment Marketplace</CardTitle>
              <CardDescription className="text-stone-600 mt-1">
                List your harvested crops for direct buyers or find local agricultural tractor and machinery rentals.
              </CardDescription>
              <ul className="mt-4 space-y-2 text-xs text-stone-600">
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Direct seller contact without middleman commission
                </li>
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Filter by location, crop type, and price range
                </li>
              </ul>
            </CardHeader>
          </Card>

          <Card className="hover:border-emerald-300 transition-colors bg-white shadow-xs">
            <CardHeader className="p-6">
              <div className="p-3 w-fit rounded-xl bg-purple-50 text-purple-800 border border-purple-100 mb-3">
                <Landmark className="w-6 h-6" />
              </div>
              <CardTitle className="text-lg">Government Schemes Navigator</CardTitle>
              <CardDescription className="text-stone-600 mt-1">
                Discover central and state government agricultural subsidies, check eligibility criteria, and apply on official portals.
              </CardDescription>
              <ul className="mt-4 space-y-2 text-xs text-stone-600">
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Match Score calculation based on farm acreage
                </li>
                <li className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" /> Direct links to official PM-KISAN & state portals
                </li>
              </ul>
            </CardHeader>
          </Card>
        </div>
      </section>

      {/* Trust Footer */}
      <footer className="border-t border-stone-200/80 bg-white py-8 px-4 text-center text-xs text-stone-500">
        <div className="max-w-6xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-2 font-bold text-stone-800">
            <Sprout className="w-4 h-4 text-emerald-700" /> AgriSathi AI — Non-AI MVP Platform
          </div>
          <p>© 2026 AgriSathi Team. Designed for simple, reliable farmer assistance.</p>
        </div>
      </footer>
    </div>
  );
}
