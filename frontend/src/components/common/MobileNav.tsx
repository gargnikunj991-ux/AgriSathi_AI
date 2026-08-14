'use client';

import React from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { cn } from '@/lib/utils/cn';
import { NAV_ITEMS } from './Sidebar';
import { X, Sprout } from 'lucide-react';
import { Badge } from './Badge';

export interface MobileNavProps {
  isOpen: boolean;
  onClose: () => void;
}

export const MobileNav: React.FC<MobileNavProps> = ({ isOpen, onClose }) => {
  const pathname = usePathname();

  if (!isOpen) return null;

  return (
    <div className="lg:hidden fixed inset-0 z-50 flex">
      <div
        className="fixed inset-0 bg-stone-900/40 backdrop-blur-xs transition-opacity"
        onClick={onClose}
        aria-hidden="true"
      />

      <div className="relative w-4/5 max-w-xs bg-white h-full shadow-2xl flex flex-col z-10 animate-in slide-in-from-left duration-200">
        <div className="h-16 px-5 flex items-center justify-between border-b border-stone-100">
          <div className="flex items-center gap-2.5">
            <div className="p-1.5 rounded-lg bg-emerald-800 text-amber-300">
              <Sprout className="w-4 h-4" />
            </div>
            <span className="font-bold text-stone-900">AgriSathi</span>
          </div>
          <button onClick={onClose} className="p-1 rounded-lg text-stone-400 hover:text-stone-700">
            <X className="w-5 h-5" />
          </button>
        </div>

        <nav className="flex-1 px-3 py-4 space-y-1 overflow-y-auto">
          {NAV_ITEMS.map((item) => {
            const Icon = item.icon;
            const isActive =
              pathname === item.href || (item.href !== '/dashboard' && pathname?.startsWith(item.href));

            return (
              <Link
                key={item.href}
                href={item.href}
                onClick={onClose}
                className={cn(
                  'flex items-center gap-3 px-3.5 py-3 rounded-lg text-sm font-medium transition-colors select-none',
                  isActive
                    ? 'bg-emerald-50 text-emerald-800 font-semibold'
                    : 'text-stone-600 hover:bg-stone-50'
                )}
              >
                <Icon className={cn('w-5 h-5', isActive ? 'text-emerald-700' : 'text-stone-400')} />
                <span>{item.label}</span>
              </Link>
            );
          })}
        </nav>

        <div className="p-4 border-t border-stone-100 bg-stone-50 flex items-center justify-between">
          <div className="truncate">
            <p className="text-xs font-semibold text-stone-900 truncate">Ramesh Kumar</p>
            <p className="text-[11px] text-stone-500 truncate">Dehradun, UK</p>
          </div>
          <Badge variant="success" size="sm">
            FARMER
          </Badge>
        </div>
      </div>
    </div>
  );
};

export const MobileBottomBar: React.FC = () => {
  const pathname = usePathname();

  return (
    <div className="lg:hidden fixed bottom-0 left-0 right-0 h-16 bg-white border-t border-stone-200/80 z-30 px-2 flex items-center justify-around shadow-lg">
      {NAV_ITEMS.slice(0, 5).map((item) => {
        const Icon = item.icon;
        const isActive =
          pathname === item.href || (item.href !== '/dashboard' && pathname?.startsWith(item.href));

        return (
          <Link
            key={item.href}
            href={item.href}
            className={cn(
              'flex flex-col items-center justify-center w-14 py-1 rounded-lg text-[10px] font-medium transition-colors',
              isActive ? 'text-emerald-800 font-bold' : 'text-stone-500 hover:text-stone-900'
            )}
          >
            <Icon className={cn('w-5 h-5 mb-0.5', isActive ? 'text-emerald-700' : 'text-stone-400')} />
            <span className="truncate max-w-full">{item.label.split(' ')[0]}</span>
          </Link>
        );
      })}
    </div>
  );
};
