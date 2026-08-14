'use client';

import React, { useEffect, useState } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { Select } from '@/components/common/Select';
import { LoadingState } from '@/components/common/LoadingState';
import { ErrorState } from '@/components/common/ErrorState';
import { User, MapPin, CheckCircle2, Save } from 'lucide-react';
import { authService } from '@/lib/api/auth.service';
import { FarmerProfile, User as UserType } from '@/lib/types';

export default function ProfilePage() {
  const [user, setUser] = useState<UserType | null>(null);
  const [profile, setProfile] = useState<FarmerProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  // Form states
  const [state, setState] = useState('Uttarakhand');
  const [district, setDistrict] = useState('Dehradun');
  const [village, setVillage] = useState('Raipur');
  const [farmSize, setFarmSize] = useState('2.5');
  const [soilType, setSoilType] = useState('Loamy Soil');
  const [mainCrop, setMainCrop] = useState('Basmati Rice');

  const fetchProfile = async () => {
    setLoading(true);
    setError('');
    try {
      const [userRes, profileRes] = await Promise.all([
        authService.getMe(),
        authService.getProfile(),
      ]);

      if (userRes.success) setUser(userRes.data);
      if (profileRes.success) {
        setProfile(profileRes.data);
        setState(profileRes.data.state);
        setDistrict(profileRes.data.district);
        setVillage(profileRes.data.village);
        setFarmSize(profileRes.data.farmSize.toString());
        setSoilType(profileRes.data.soilType);
        setMainCrop(profileRes.data.mainCrop);
      }
    } catch (err: unknown) {
      setError((err as Error).message || 'Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  const handleSaveProfile = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setSuccessMsg('');
    try {
      const res = await authService.updateProfile({
        state,
        district,
        village,
        farmSize: parseFloat(farmSize),
        soilType,
        mainCrop,
      });
      if (res.success) {
        setProfile(res.data);
        setSuccessMsg('Profile updated successfully! Tailored advisories updated.');
        setTimeout(() => setSuccessMsg(''), 3000);
      }
    } catch (err: unknown) {
      alert((err as Error).message || 'Failed to update profile');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <LoadingState message="Loading profile information..." variant="skeleton" />;
  if (error) return <ErrorState message={error} onRetry={fetchProfile} />;

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-stone-900">Farmer Profile & Farm Context</h2>
          <p className="text-sm text-stone-500">
            Manage your personal contact details, location, acreage, and soil profile.
          </p>
        </div>
        <Badge variant="success" size="md">
          {user?.role || 'FARMER'}
        </Badge>
      </div>

      {/* Account Info Box */}
      <Card className="bg-white border-stone-200/80 shadow-xs">
        <CardHeader className="pb-3">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-full bg-emerald-800 text-amber-300 flex items-center justify-center text-lg font-bold">
              RK
            </div>
            <div>
              <CardTitle className="text-base">{user?.name || 'Ramesh Kumar'}</CardTitle>
              <CardDescription className="text-xs">
                {user?.email || 'ramesh@agrisathi.com'} • {user?.phone || '9876543210'}
              </CardDescription>
            </div>
          </div>
        </CardHeader>
      </Card>

      {/* Profile Form */}
      <Card className="bg-white border-stone-200/80 shadow-xs">
        <CardHeader>
          <CardTitle className="text-base flex items-center gap-2">
            <MapPin className="w-4 h-4 text-emerald-700" /> Farm Location & Soil Profiling
          </CardTitle>
          <CardDescription>
            This information is used to match government schemes and calculate weather advisories.
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSaveProfile} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <Input
                label="State"
                value={state}
                onChange={(e) => setState(e.target.value)}
                required
              />
              <Input
                label="District"
                value={district}
                onChange={(e) => setDistrict(e.target.value)}
                required
              />
              <Input
                label="Village / Town"
                value={village}
                onChange={(e) => setVillage(e.target.value)}
                required
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <Input
                label="Farm Size (in Acres)"
                type="number"
                step="0.1"
                value={farmSize}
                onChange={(e) => setFarmSize(e.target.value)}
                required
              />

              <Select
                label="Soil Type"
                value={soilType}
                onChange={(e) => setSoilType(e.target.value)}
                options={[
                  { value: 'Loamy Soil', label: 'Loamy Soil' },
                  { value: 'Alluvial Soil', label: 'Alluvial Soil' },
                  { value: 'Clay Soil', label: 'Clay Soil' },
                  { value: 'Black Soil', label: 'Black Cotton Soil' },
                  { value: 'Red & Yellow Soil', label: 'Red & Yellow Soil' },
                ]}
              />

              <Input
                label="Main Primary Crop"
                value={mainCrop}
                onChange={(e) => setMainCrop(e.target.value)}
                required
              />
            </div>

            {successMsg && (
              <div className="p-3 rounded-lg bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs font-medium flex items-center gap-1.5">
                <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" />
                {successMsg}
              </div>
            )}

            <div className="pt-2 flex justify-end">
              <Button type="submit" isLoading={saving} leftIcon={<Save className="w-4 h-4" />}>
                Save Profile Changes
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
