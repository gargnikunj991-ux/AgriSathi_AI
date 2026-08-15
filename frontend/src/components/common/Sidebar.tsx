'use client';

import React from 'react';
import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import { cn } from '@/lib/utils/cn';
import {
  LayoutDashboard,
  Sprout,
  CloudSun,
  Store,
  Landmark,
  User,
  LogOut,
  Sprout as BrandIcon,
} from 'lucide-react';
import { Badge } from './Badge';
import { authService } from '@/lib/api/auth.service';

export interface NavItem {
  label: string;
  href: string;
  icon: React.ComponentType<{ className?: string }>;
}

export const NAV_ITEMS: NavItem[] = [
  { label: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
  { label: 'My Crops', href: '/crops', icon: Sprout },
  { label: 'Weather', href: '/weather', icon: CloudSun },
  { label: 'Marketplace', href: '/marketplace', icon: Store },
  { label: 'Government Schemes', href: '/schemes', icon: Landmark },
  { label: 'Farmer Profile', href: '/profile', icon: User },
];

export const Sidebar: React.FC = () => {
  const pathname = usePathname();
  const router = useRouter();

  const handleLogout = () => {
    authService.logout();
    router.push('/login');
  };

  return (
    <aside className="hidden lg:flex flex-col w-64 border-r border-stone-200/80 bg-white shrink-0 h-screen sticky top-0">
      {/* Brand Logo Header */}
      <div className="h-16 px-6 flex items-center gap-3 border-b border-stone-100">
        <div className="p-2 rounded-lg bg-emerald-800 text-amber-300">
          <BrandIcon className="w-5 h-5" />
        </div>
        <div>
          <span className="text-lg font-bold text-stone-900 tracking-tight">
            AgriSathi
          </span>
          <span className="text-xs font-medium text-emerald-800 block -mt-1">
            Farming Companion
          </span>
        </div>
      </div>

      {/* Nav Items */}
      <nav className="flex-1 px-3 py-4 space-y-1 overflow-y-auto">
        {NAV_ITEMS.map((item) => {
          const Icon = item.icon;
          const isActive =
            pathname === item.href || (item.href !== '/dashboard' && pathname?.startsWith(item.href));

          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                'flex items-center gap-3 px-3.5 py-2.5 rounded-lg text-sm font-medium transition-all select-none',
                isActive
                  ? 'bg-emerald-50 text-emerald-800 font-semibold border-r-2 border-emerald-700 shadow-2xs'
                  : 'text-stone-600 hover:text-stone-900 hover:bg-stone-50'
              )}
            >
              <Icon
                className={cn(
                  'w-5 h-5 transition-colors',
                  isActive ? 'text-emerald-700' : 'text-stone-400 group-hover:text-stone-600'
                )}
              />
              <span>{item.label}</span>
            </Link>
          );
        })}
      </nav>

      {/* User Profile Badge & Logout */}
      <div className="p-4 border-t border-stone-100 bg-stone-50/60 space-y-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2.5 overflow-hidden">
            <div className="w-8 h-8 rounded-full bg-emerald-800 text-white flex items-center justify-center text-xs font-bold shrink-0">
              RK
            </div>
            <div className="truncate">
              <p className="text-xs font-semibold text-stone-900 truncate">Ramesh Kumar</p>
              <p className="text-[11px] text-stone-500 truncate">Dehradun, UK</p>
            </div>
          </div>
          <Badge variant="success" size="sm">
            FARMER
          </Badge>
        </div>

        <button
          onClick={handleLogout}
          className="w-full flex items-center justify-center gap-2 px-3 py-2 text-xs font-semibold text-red-700 bg-red-50 hover:bg-red-100 rounded-lg border border-red-200/80 transition-colors"
        >
          <LogOut className="w-3.5 h-3.5" />
          Sign Out
        </button>
      </div>
    </aside>
  );
};
