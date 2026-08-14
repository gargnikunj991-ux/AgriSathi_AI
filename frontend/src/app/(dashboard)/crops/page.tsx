'use client';

import React, { useEffect, useState } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { Select } from '@/components/common/Select';
import { Modal } from '@/components/common/Modal';
import { LoadingState } from '@/components/common/LoadingState';
import { ErrorState } from '@/components/common/ErrorState';
import { EmptyState } from '@/components/common/EmptyState';
import { Sprout, Plus, Calendar, CheckCircle2, Trash2 } from 'lucide-react';
import { cropService } from '@/lib/api/crop.service';
import { Crop } from '@/lib/types';

export default function CropsPage() {
  const [crops, setCrops] = useState<Crop[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Form states for adding crop
  const [cropName, setCropName] = useState('Mustard Crop');
  const [sowingDate, setSowingDate] = useState('2026-10-01');
  const [harvestDate, setHarvestDate] = useState('2027-02-15');
  const [soilType, setSoilType] = useState('Loamy Soil');
  const [submitting, setSubmitting] = useState(false);

  const fetchCrops = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await cropService.getCrops();
      if (res.success) setCrops(res.data);
    } catch (err: unknown) {
      setError((err as Error).message || 'Failed to load crops');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCrops();
  }, []);

  const handleAddCrop = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const res = await cropService.addCrop({ cropName, sowingDate, harvestDate, soilType });
      if (res.success) {
        setCrops((prev) => [...prev, res.data]);
        setIsModalOpen(false);
      }
    } catch (err: unknown) {
      alert((err as Error).message || 'Failed to add crop');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteCrop = async (id: number) => {
    if (!confirm('Are you sure you want to remove this crop log?')) return;
    try {
      await cropService.deleteCrop(id);
      setCrops((prev) => prev.filter((c) => c.id !== id));
    } catch (err: unknown) {
      alert((err as Error).message || 'Failed to delete crop');
    }
  };

  if (loading) return <LoadingState message="Loading your crop records..." variant="skeleton" />;
  if (error) return <ErrorState message={error} onRetry={fetchCrops} />;

  return (
    <div className="space-y-6">
      {/* Header Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-stone-900">Crop Management</h2>
          <p className="text-sm text-stone-500">
            Log sowing dates, harvest timelines, and growing guidance.
          </p>
        </div>
        <Button onClick={() => setIsModalOpen(true)} leftIcon={<Plus className="w-4 h-4" />}>
          Add New Crop
        </Button>
      </div>

      {/* Crop List */}
      {crops.length === 0 ? (
        <EmptyState
          icon={<Sprout className="w-8 h-8" />}
          title="No Crops Tracked Yet"
          description="Start tracking your farm crops to receive tailored watering and fertilizer guidance."
          action={
            <Button onClick={() => setIsModalOpen(true)} leftIcon={<Plus className="w-4 h-4" />}>
              Add Your First Crop
            </Button>
          }
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {crops.map((crop) => (
            <Card key={crop.id} className="bg-white border-stone-200/80 shadow-xs flex flex-col justify-between">
              <CardHeader className="pb-3">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <div className="p-2 rounded-lg bg-emerald-50 text-emerald-800">
                      <Sprout className="w-5 h-5" />
                    </div>
                    <div>
                      <CardTitle className="text-base">{crop.cropName}</CardTitle>
                      <p className="text-xs text-stone-500">{crop.soilType || 'Loamy Soil'}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge
                      variant={
                        crop.status === 'PLANTED'
                          ? 'success'
                          : crop.status === 'HARVESTED'
                          ? 'info'
                          : 'error'
                      }
                    >
                      {crop.status}
                    </Badge>
                    <button
                      onClick={() => handleDeleteCrop(crop.id)}
                      className="text-stone-400 hover:text-red-600 p-1 transition-colors"
                      title="Remove Crop Log"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              </CardHeader>

              <CardContent className="space-y-4 pt-1 flex-1">
                <div className="grid grid-cols-2 gap-2 text-xs bg-stone-50 p-3 rounded-lg border border-stone-200/80">
                  <div>
                    <span className="text-stone-500 block">Sowing Date</span>
                    <span className="font-semibold text-stone-800 flex items-center gap-1 mt-0.5">
                      <Calendar className="w-3.5 h-3.5 text-stone-400" /> {crop.sowingDate}
                    </span>
                  </div>
                  <div>
                    <span className="text-stone-500 block">Expected Harvest</span>
                    <span className="font-semibold text-stone-800 flex items-center gap-1 mt-0.5">
                      <Calendar className="w-3.5 h-3.5 text-stone-400" /> {crop.harvestDate}
                    </span>
                  </div>
                </div>

                {crop.fertilizerRecommendation && (
                  <div className="space-y-1">
                    <h4 className="text-xs font-bold text-stone-800">Fertilizer Recommendation:</h4>
                    <p className="text-xs text-emerald-800 bg-emerald-50 p-2.5 rounded-lg border border-emerald-200/60 font-medium">
                      {crop.fertilizerRecommendation}
                    </p>
                  </div>
                )}

                {crop.growingGuidance && crop.growingGuidance.length > 0 && (
                  <div className="space-y-1.5">
                    <h4 className="text-xs font-bold text-stone-800">Growing Guidance:</h4>
                    <ul className="space-y-1 text-xs text-stone-600">
                      {crop.growingGuidance.map((tip, idx) => (
                        <li key={idx} className="flex items-start gap-1.5">
                          <CheckCircle2 className="w-3.5 h-3.5 text-emerald-700 shrink-0 mt-0.5" />
                          <span>{tip}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                )}
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Add Crop Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Add New Farm Crop"
        description="Record crop sowing details to generate fertilizer and growing recommendations."
        footer={
          <>
            <Button variant="outline" onClick={() => setIsModalOpen(false)}>
              Cancel
            </Button>
            <Button onClick={handleAddCrop} isLoading={submitting}>
              Save Crop Record
            </Button>
          </>
        }
      >
        <form onSubmit={handleAddCrop} className="space-y-4">
          <Input
            label="Crop Name"
            placeholder="e.g. Basmati Rice, Wheat, Cotton"
            value={cropName}
            onChange={(e) => setCropName(e.target.value)}
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

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Input
              label="Sowing Date"
              type="date"
              value={sowingDate}
              onChange={(e) => setSowingDate(e.target.value)}
              required
            />
            <Input
              label="Expected Harvest Date"
              type="date"
              value={harvestDate}
              onChange={(e) => setHarvestDate(e.target.value)}
              required
            />
          </div>
        </form>
      </Modal>
    </div>
  );
}
