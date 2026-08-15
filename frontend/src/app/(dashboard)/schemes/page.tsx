'use client';

import React, { useEffect, useState } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { Button } from '@/components/common/Button';
import { Select } from '@/components/common/Select';
import { LoadingState } from '@/components/common/LoadingState';
import { ErrorState } from '@/components/common/ErrorState';
import { EmptyState } from '@/components/common/EmptyState';
import { Landmark, ExternalLink, CheckCircle2, ShieldCheck, FileText } from 'lucide-react';
import { schemeService } from '@/lib/api/scheme.service';
import { SchemeRecommendation } from '@/lib/types';

export default function SchemesPage() {
  const [recommendations, setRecommendations] = useState<SchemeRecommendation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedState, setSelectedState] = useState('All');

  const fetchSchemes = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await schemeService.getRecommendations();
      if (res.success) setRecommendations(res.data);
    } catch (err: unknown) {
      setError((err as Error).message || 'Failed to load government schemes');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSchemes();
  }, []);

  const filtered = recommendations.filter((rec) => {
    if (selectedState === 'All') return true;
    return rec.scheme.state === 'All India' || rec.scheme.state === selectedState;
  });

  if (loading) return <LoadingState message="Searching government schemes and matching subsidies..." variant="skeleton" />;
  if (error) return <ErrorState message={error} onRetry={fetchSchemes} />;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-stone-900">Government Schemes Navigator</h2>
          <p className="text-sm text-stone-500">
            Federal and state agricultural subsidies matched to your farm profile (Uttarakhand, 2.5 Acres).
          </p>
        </div>
        <div className="w-full sm:w-64">
          <Select
            label="Filter by State"
            value={selectedState}
            onChange={(e) => setSelectedState(e.target.value)}
            options={[
              { value: 'All', label: 'All India & States' },
              { value: 'Uttarakhand', label: 'Uttarakhand (Home)' },
              { value: 'Punjab', label: 'Punjab' },
            ]}
          />
        </div>
      </div>

      {/* Schemes Grid */}
      {filtered.length === 0 ? (
        <EmptyState
          icon={<Landmark className="w-8 h-8" />}
          title="No Government Schemes Found"
          description="Try resetting your state filter to view central government agricultural programs."
          action={
            <Button variant="outline" onClick={() => setSelectedState('All')}>
              Show All India Schemes
            </Button>
          }
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {filtered.map(({ scheme, matchScore, matchReason }) => (
            <Card key={scheme.id} className="bg-white border-stone-200/80 shadow-xs flex flex-col justify-between">
              <div>
                <CardHeader className="pb-3">
                  <div className="flex items-start justify-between gap-3">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <Badge variant="outline">{scheme.category}</Badge>
                        <Badge variant={scheme.state === 'Uttarakhand' ? 'success' : 'default'}>
                          {scheme.state}
                        </Badge>
                      </div>
                      <CardTitle className="text-base pt-1">{scheme.title}</CardTitle>
                    </div>

                    <div className="text-right shrink-0">
                      <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-emerald-100 text-emerald-900 text-xs font-extrabold border border-emerald-200">
                        <ShieldCheck className="w-3.5 h-3.5 text-emerald-700" />
                        {matchScore}% Match
                      </span>
                    </div>
                  </div>
                  <CardDescription className="text-xs text-stone-600 mt-2 leading-relaxed">
                    {scheme.description}
                  </CardDescription>
                </CardHeader>

                <CardContent className="space-y-3 pt-0 text-xs">
                  {/* Benefit Banner */}
                  <div className="p-3 rounded-lg bg-emerald-50/80 border border-emerald-200/70 flex items-center justify-between">
                    <span className="text-stone-600 font-medium">Financial Benefit:</span>
                    <span className="font-extrabold text-emerald-900 text-sm">{scheme.benefitAmount}</span>
                  </div>

                  {/* Match Reason */}
                  <p className="text-[11px] text-stone-500 bg-stone-50 p-2 rounded border border-stone-200/70">
                    <strong>Match Criteria:</strong> {matchReason}
                  </p>

                  {/* Eligibility */}
                  <div>
                    <h4 className="font-bold text-stone-800 mb-1">Eligibility Info:</h4>
                    <p className="text-stone-600">{scheme.eligibility}</p>
                  </div>

                  {/* Required Documents */}
                  {scheme.requiredDocuments && scheme.requiredDocuments.length > 0 && (
                    <div className="space-y-1">
                      <h4 className="font-bold text-stone-800 flex items-center gap-1">
                        <FileText className="w-3.5 h-3.5 text-stone-500" /> Required Documents:
                      </h4>
                      <ul className="space-y-1 text-stone-600 pl-1">
                        {scheme.requiredDocuments.map((doc, idx) => (
                          <li key={idx} className="flex items-center gap-1.5">
                            <CheckCircle2 className="w-3 h-3 text-emerald-700 shrink-0" />
                            <span>{doc}</span>
                          </li>
                        ))}
                      </ul>
                    </div>
                  )}
                </CardContent>
              </div>

              <CardFooter className="pt-3">
                <a
                  href={scheme.applyLink}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="w-full"
                >
                  <Button variant="primary" size="sm" className="w-full" rightIcon={<ExternalLink className="w-3.5 h-3.5" />}>
                    Apply on Official Portal
                  </Button>
                </a>
              </CardFooter>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
