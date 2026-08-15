'use client';

import React from 'react';
import { usePathname, useRouter } from 'next/navigation';
import { Menu, Bell, User as UserIcon, LogOut } from 'lucide-react';
import Link from 'next/link';
import { authService } from '@/lib/api/auth.service';

interface HeaderProps {
  onOpenMobileNav?: () => void;
}

const ROUTE_TITLES: Record<string, string> = {
  '/dashboard': 'Farmer Dashboard',
  '/crops': 'Crop Management',
  '/weather': 'Weather Forecast & Advisories',
  '/marketplace': 'Produce & Equipment Marketplace',
  '/schemes': 'Government Schemes Navigator',
  '/profile': 'Farmer Profile',
  '/login': 'Sign In / Register',
};

export const Header: React.FC<HeaderProps> = ({ onOpenMobileNav }) => {
  const pathname = usePathname();
  const router = useRouter();

  const handleLogout = () => {
    authService.logout();
    router.push('/login');
  };

  const title =
    Object.entries(ROUTE_TITLES).find(
      ([route]) => pathname === route || (route !== '/dashboard' && pathname?.startsWith(route))
    )?.[1] || 'AgriSathi Platform';

  return (
    <header className="h-16 border-b border-stone-200/80 bg-white sticky top-0 z-30 px-4 sm:px-6 flex items-center justify-between shadow-2xs">
      <div className="flex items-center gap-3">
        <button
          onClick={onOpenMobileNav}
          className="lg:hidden p-2 rounded-lg text-stone-600 hover:bg-stone-100 transition-colors"
          aria-label="Open menu"
        >
          <Menu className="w-5 h-5" />
        </button>
        <h1 className="text-lg font-bold text-stone-900 tracking-tight">{title}</h1>
      </div>

      <div className="flex items-center gap-2 sm:gap-3">
        <button
          className="p-2 rounded-lg text-stone-500 hover:text-stone-800 hover:bg-stone-100 transition-colors relative"
          aria-label="Notifications"
        >
          <Bell className="w-5 h-5" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-emerald-600" />
        </button>

        <Link
          href="/profile"
          className="p-1.5 rounded-lg text-stone-600 hover:bg-stone-100 transition-colors flex items-center gap-2"
        >
          <div className="w-8 h-8 rounded-full bg-emerald-100 text-emerald-800 flex items-center justify-center text-xs font-bold border border-emerald-200">
            <UserIcon className="w-4 h-4" />
          </div>
        </Link>

        <button
          onClick={handleLogout}
          className="hidden sm:flex items-center gap-1.5 px-2.5 py-1.5 text-xs font-semibold text-red-700 hover:text-red-800 hover:bg-red-50 rounded-lg border border-stone-200 hover:border-red-200 transition-colors"
          title="Sign Out"
        >
          <LogOut className="w-3.5 h-3.5" />
          <span>Sign Out</span>
        </button>
      </div>
    </header>
  );
};
