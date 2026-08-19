'use client';

import React, { createContext, useContext, useEffect, useState, useCallback } from 'react';
import { User, FarmerProfile } from '@/lib/types';
import { authService } from '@/lib/api/auth.service';

export function getInitials(name?: string, fallback = 'U'): string {
  if (!name || !name.trim()) return fallback;
  const parts = name.trim().split(/\s+/);
  if (parts.length === 1) {
    return parts[0].slice(0, 2).toUpperCase();
  }
  return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
}

interface AuthContextType {
  user: User | null;
  profile: FarmerProfile | null;
  loading: boolean;
  logout: () => void;
  refreshUser: () => Promise<void>;
  setUser: (user: User | null) => void;
  setProfile: (profile: FarmerProfile | null) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [profile, setProfile] = useState<FarmerProfile | null>(null);
  const [loading, setLoading] = useState(true);

  const refreshUser = useCallback(async () => {
    if (typeof window === 'undefined') return;
    const token = localStorage.getItem('agrisathi_token');
    if (!token) {
      setUser(null);
      setProfile(null);
      setLoading(false);
      return;
    }

    try {
      const userRes = await authService.getMe();
      if (userRes && userRes.success && userRes.data) {
        setUser(userRes.data);
        localStorage.setItem('agrisathi_user', JSON.stringify(userRes.data));
      }
    } catch {
      // Ignored if unauthenticated or network error
    }

    try {
      const profileRes = await authService.getProfile();
      if (profileRes && profileRes.success && profileRes.data) {
        setProfile(profileRes.data);
      }
    } catch {
      // Profile may not exist yet
    }

    setLoading(false);
  }, []);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const cached = localStorage.getItem('agrisathi_user');
      if (cached) {
        try {
          setUser(JSON.parse(cached));
        } catch {
          // Ignore parse error
        }
      }
    }
    refreshUser();
  }, [refreshUser]);

  const logout = () => {
    authService.logout();
    setUser(null);
    setProfile(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        profile,
        loading,
        logout,
        refreshUser,
        setUser,
        setProfile,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
