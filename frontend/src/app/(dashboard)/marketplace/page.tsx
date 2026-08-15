'use client';

import React, { useEffect, useState } from 'react';
import Image from 'next/image';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { Select } from '@/components/common/Select';
import { Modal } from '@/components/common/Modal';
import { LoadingState } from '@/components/common/LoadingState';
import { ErrorState } from '@/components/common/ErrorState';
import { EmptyState } from '@/components/common/EmptyState';
import { Store, Plus, Search, MapPin, Phone, Mail, User, CheckCircle2 } from 'lucide-react';
import { marketplaceService } from '@/lib/api/marketplace.service';
import { MarketplaceListing } from '@/lib/types';

export default function MarketplacePage() {
  const [listings, setListings] = useState<MarketplaceListing[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedLocation, setSelectedLocation] = useState('All Locations');

  // Modal states
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isContactModalOpen, setIsContactModalOpen] = useState(false);
  const [activeListing, setActiveListing] = useState<MarketplaceListing | null>(null);

  // Form states for creating listing
  const [cropName, setCropName] = useState('Basmati Rice');
  const [description, setDescription] = useState('Organic Basmati rice harvested in Dehradun valley.');
  const [price, setPrice] = useState('35');
  const [quantity, setQuantity] = useState('200');
  const [unit, setUnit] = useState('kg');
  const [location, setLocation] = useState('Dehradun, Uttarakhand');
  const [submitting, setSubmitting] = useState(false);

  // Form states for contacting seller
  const [buyerName, setBuyerName] = useState('Anil Sharma');
  const [buyerPhone, setBuyerPhone] = useState('9876543210');
  const [buyerEmail, setBuyerEmail] = useState('anil@gmail.com');
  const [inquiryMsg, setInquiryMsg] = useState('Interested in purchasing 100kg of your Basmati Rice.');
  const [inquirySuccess, setInquirySuccess] = useState('');

  const fetchListings = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await marketplaceService.getListings();
      if (res.success) setListings(res.data);
    } catch (err: unknown) {
      setError((err as Error).message || 'Failed to load marketplace listings');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchListings();
  }, []);

  const handleCreateListing = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const res = await marketplaceService.createListing({
        cropName,
        description,
        price: parseFloat(price),
        quantity: parseFloat(quantity),
        unit,
        location,
      });
      if (res.success) {
        setListings((prev) => [res.data, ...prev]);
        setIsCreateModalOpen(false);
      }
    } catch (err: unknown) {
      alert((err as Error).message || 'Failed to create listing');
    } finally {
      setSubmitting(false);
    }
  };

  const handleContactSeller = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!activeListing) return;
    setSubmitting(true);
    try {
      const res = await marketplaceService.contactSeller(activeListing.id, {
        message: inquiryMsg,
        buyerName,
        buyerPhone,
        buyerEmail,
      });
      if (res.success) {
        setInquirySuccess(res.data.formattedInquiry);
        setTimeout(() => {
          setIsContactModalOpen(false);
          setInquirySuccess('');
        }, 1500);
      }
    } catch (err: unknown) {
      alert((err as Error).message || 'Failed to send inquiry');
    } finally {
      setSubmitting(false);
    }
  };

  const filteredListings = listings.filter((l) => {
    const matchesSearch =
      l.cropName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      l.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesLocation =
      selectedLocation === 'All Locations' || l.location.includes(selectedLocation);
    return matchesSearch && matchesLocation;
  });

  if (loading) return <LoadingState message="Loading marketplace listings..." variant="skeleton" />;
  if (error) return <ErrorState message={error} onRetry={fetchListings} />;

  return (
    <div className="space-y-6">
      {/* Header & Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-stone-900">Produce & Equipment Marketplace</h2>
          <p className="text-sm text-stone-500">
            Direct producer-to-buyer transactions and machinery rentals without middleman fees.
          </p>
        </div>
        <Button onClick={() => setIsCreateModalOpen(true)} leftIcon={<Plus className="w-4 h-4" />}>
          Post New Listing
        </Button>
      </div>

      {/* Filters Bar */}
      <div className="bg-white p-4 rounded-xl border border-stone-200/80 shadow-2xs flex flex-col sm:flex-row gap-3">
        <div className="flex-1">
          <Input
            placeholder="Search crops, produce, or tractor rental..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            leftIcon={<Search className="w-4 h-4" />}
          />
        </div>
        <div className="w-full sm:w-64">
          <Select
            value={selectedLocation}
            onChange={(e) => setSelectedLocation(e.target.value)}
            options={[
              { value: 'All Locations', label: 'All Locations' },
              { value: 'Dehradun', label: 'Dehradun Valley' },
              { value: 'Haridwar', label: 'Haridwar' },
              { value: 'Udham Singh Nagar', label: 'Udham Singh Nagar' },
            ]}
          />
        </div>
      </div>

      {/* Listings Grid */}
      {filteredListings.length === 0 ? (
        <EmptyState
          icon={<Store className="w-8 h-8" />}
          title="No Produce Listings Found"
          description="Try adjusting your search filters or be the first to post a new produce listing."
          action={
            <Button onClick={() => setIsCreateModalOpen(true)} leftIcon={<Plus className="w-4 h-4" />}>
              Create Listing
            </Button>
          }
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredListings.map((listing) => (
            <Card key={listing.id} className="bg-white border-stone-200/80 shadow-xs flex flex-col justify-between overflow-hidden">
              <div>
                {listing.imageUrl && (
                  <div className="relative h-44 w-full bg-stone-100 overflow-hidden">
                    <Image
                      src={listing.imageUrl}
                      alt={listing.cropName}
                      fill
                      sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
                      className="object-cover"
                    />
                    <div className="absolute top-3 right-3">
                      <Badge variant={listing.status === 'AVAILABLE' ? 'success' : 'outline'}>
                        {listing.status}
                      </Badge>
                    </div>
                  </div>
                )}

                <CardHeader className="pb-2">
                  <div className="flex items-start justify-between gap-2">
                    <CardTitle className="text-base">{listing.cropName}</CardTitle>
                    <div className="text-right shrink-0">
                      <span className="text-lg font-extrabold text-emerald-800">
                        ₹{listing.price}
                      </span>
                      <span className="text-xs text-stone-500 font-medium">/{listing.unit}</span>
                    </div>
                  </div>
                  <CardDescription className="line-clamp-2 text-xs mt-1">
                    {listing.description}
                  </CardDescription>
                </CardHeader>

                <CardContent className="space-y-2 text-xs text-stone-600 pt-0">
                  <div className="flex items-center gap-1.5 text-stone-500">
                    <MapPin className="w-3.5 h-3.5 text-stone-400 shrink-0" />
                    <span>{listing.location}</span>
                  </div>
                  <div className="flex items-center justify-between p-2 rounded-lg bg-stone-50 border border-stone-200/80">
                    <span className="text-stone-500">Quantity Available:</span>
                    <span className="font-bold text-stone-900">
                      {listing.quantity} {listing.unit}
                    </span>
                  </div>
                </CardContent>
              </div>

              <CardFooter className="pt-3">
                <Button
                  variant="primary"
                  size="sm"
                  className="w-full"
                  onClick={() => {
                    setActiveListing(listing);
                    setIsContactModalOpen(true);
                  }}
                >
                  Contact Seller
                </Button>
              </CardFooter>
            </Card>
          ))}
        </div>
      )}

      {/* Create Listing Modal */}
      <Modal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        title="Post Produce or Equipment Listing"
        description="List your harvested crops or tractor rentals for direct buyers."
        footer={
          <>
            <Button variant="outline" onClick={() => setIsCreateModalOpen(false)}>
              Cancel
            </Button>
            <Button onClick={handleCreateListing} isLoading={submitting}>
              Post Listing
            </Button>
          </>
        }
      >
        <form onSubmit={handleCreateListing} className="space-y-4">
          <Input
            label="Listing Title / Crop Name"
            placeholder="e.g. Basmati Rice, Organic Wheat, Tractor Rental"
            value={cropName}
            onChange={(e) => setCropName(e.target.value)}
            required
          />

          <div className="grid grid-cols-3 gap-3">
            <Input
              label="Price (₹)"
              type="number"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              required
            />
            <Input
              label="Quantity"
              type="number"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              required
            />
            <Select
              label="Unit"
              value={unit}
              onChange={(e) => setUnit(e.target.value)}
              options={[
                { value: 'kg', label: 'kg' },
                { value: 'quintal', label: 'quintal' },
                { value: 'ton', label: 'ton' },
                { value: 'day', label: 'day (rental)' },
              ]}
            />
          </div>

          <Input
            label="Location / District"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            required
          />

          <div className="space-y-1.5">
            <label className="text-xs font-semibold text-stone-700">Description</label>
            <textarea
              className="w-full p-3 text-xs rounded-lg border border-stone-300 bg-white text-stone-900 focus:ring-2 focus:ring-emerald-600 focus:outline-none min-h-[80px]"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>
        </form>
      </Modal>

      {/* Contact Seller Modal */}
      {activeListing && (
        <Modal
          isOpen={isContactModalOpen}
          onClose={() => setIsContactModalOpen(false)}
          title={`Contact Seller: ${activeListing.sellerName || 'Ramesh Kumar'}`}
          description={`Inquire about listing '${activeListing.cropName}' (${activeListing.quantity} ${activeListing.unit} @ ₹${activeListing.price}/${activeListing.unit})`}
          footer={
            <>
              <Button variant="outline" onClick={() => setIsContactModalOpen(false)}>
                Cancel
              </Button>
              <Button onClick={handleContactSeller} isLoading={submitting}>
                Send Inquiry Message
              </Button>
            </>
          }
        >
          {inquirySuccess ? (
            <div className="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs font-medium space-y-2">
              <div className="flex items-center gap-2 font-bold text-sm">
                <CheckCircle2 className="w-5 h-5 text-emerald-700" /> Inquiry Sent Successfully!
              </div>
              <p>{inquirySuccess}</p>
            </div>
          ) : (
            <form onSubmit={handleContactSeller} className="space-y-4">
              <div className="p-3 rounded-lg bg-stone-50 border border-stone-200/80 space-y-1 text-xs text-stone-700">
                <div className="flex items-center gap-1.5 font-bold text-stone-900">
                  <User className="w-3.5 h-3.5 text-stone-500" /> Seller: {activeListing.sellerName}
                </div>
                <div className="flex items-center gap-3 text-stone-500 pt-0.5">
                  <span className="flex items-center gap-1">
                    <Phone className="w-3.5 h-3.5 text-emerald-700" /> {activeListing.sellerPhone}
                  </span>
                  <span className="flex items-center gap-1">
                    <Mail className="w-3.5 h-3.5 text-stone-400" /> {activeListing.sellerEmail}
                  </span>
                </div>
              </div>

              <Input
                label="Your Buyer Name"
                value={buyerName}
                onChange={(e) => setBuyerName(e.target.value)}
                required
              />

              <div className="grid grid-cols-2 gap-3">
                <Input
                  label="Phone Number"
                  value={buyerPhone}
                  onChange={(e) => setBuyerPhone(e.target.value)}
                  required
                />
                <Input
                  label="Email Address"
                  value={buyerEmail}
                  onChange={(e) => setBuyerEmail(e.target.value)}
                  required
                />
              </div>

              <div className="space-y-1.5">
                <label className="text-xs font-semibold text-stone-700">Message to Seller</label>
                <textarea
                  className="w-full p-3 text-xs rounded-lg border border-stone-300 bg-white text-stone-900 focus:ring-2 focus:ring-emerald-600 focus:outline-none min-h-[80px]"
                  value={inquiryMsg}
                  onChange={(e) => setInquiryMsg(e.target.value)}
                  required
                />
              </div>
            </form>
          )}
        </Modal>
      )}
    </div>
  );
}
