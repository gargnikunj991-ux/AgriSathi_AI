'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Sprout, User, ArrowRight, CheckCircle2, Lock } from 'lucide-react';
import { Button } from '@/components/common/Button';
import { Input } from '@/components/common/Input';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { authService } from '@/lib/api/auth.service';
import { UserRole } from '@/lib/types';

export default function LoginPage() {
  const router = useRouter();
  const [tab, setTab] = useState<'login' | 'register'>('login');
  const [role, setRole] = useState<UserRole>('FARMER');

  // Form states
  const [name, setName] = useState('Ramesh Kumar');
  const [email, setEmail] = useState('ramesh@agrisathi.com');
  const [password, setPassword] = useState('Password@123');
  const [phone, setPhone] = useState('9876543210');
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMsg('');
    setSuccessMsg('');

    try {
      if (tab === 'login') {
        const res = await authService.login({ email, password });
        if (res.success) {
          setSuccessMsg('Login successful! Redirecting to dashboard...');
          setTimeout(() => router.push('/dashboard'), 500);
        }
      } else {
        const res = await authService.register({ name, email, password, phone, role });
        if (res.success) {
          setSuccessMsg('Registration successful! You can now sign in.');
          setTab('login');
        }
      }
    } catch (err: unknown) {
      setErrorMsg((err as Error).message || 'Authentication failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#faf9f5] flex flex-col justify-between text-stone-900">
      {/* Header */}
      <header className="h-16 border-b border-stone-200/80 bg-white px-4 sm:px-8 flex items-center justify-between">
        <Link href="/" className="flex items-center gap-2.5">
          <div className="p-1.5 rounded-lg bg-emerald-800 text-amber-300">
            <Sprout className="w-5 h-5" />
          </div>
          <span className="font-bold text-stone-900 text-lg">AgriSathi</span>
        </Link>
        <Link href="/">
          <Button variant="ghost" size="sm">
            Back to Home
          </Button>
        </Link>
      </header>

      {/* Main Auth Card */}
      <main className="flex-1 flex items-center justify-center p-4 sm:p-6 my-8">
        <div className="w-full max-w-md space-y-6">
          <div className="text-center space-y-2">
            <h2 className="text-2xl sm:text-3xl font-extrabold text-stone-900 tracking-tight">
              {tab === 'login' ? 'Welcome Back' : 'Create an Account'}
            </h2>
            <p className="text-sm text-stone-600">
              {tab === 'login'
                ? 'Sign in to access your farmer dashboard & crop logs'
                : 'Join AgriSathi to connect with marketplace & weather tools'}
            </p>
          </div>

          <Card className="shadow-md border-stone-200/90 bg-white">
            <CardHeader className="p-5 border-b border-stone-100 pb-4">
              {/* Tab Switcher */}
              <div className="grid grid-cols-2 p-1 rounded-lg bg-stone-100">
                <button
                  onClick={() => {
                    setTab('login');
                    setErrorMsg('');
                    setSuccessMsg('');
                  }}
                  className={`py-2 text-xs font-bold rounded-md transition-all ${
                    tab === 'login'
                      ? 'bg-white text-stone-900 shadow-2xs'
                      : 'text-stone-600 hover:text-stone-900'
                  }`}
                >
                  Sign In
                </button>
                <button
                  onClick={() => {
                    setTab('register');
                    setErrorMsg('');
                    setSuccessMsg('');
                  }}
                  className={`py-2 text-xs font-bold rounded-md transition-all ${
                    tab === 'register'
                      ? 'bg-white text-stone-900 shadow-2xs'
                      : 'text-stone-600 hover:text-stone-900'
                  }`}
                >
                  Register
                </button>
              </div>
            </CardHeader>

            <CardContent className="p-5 sm:p-6 pt-5">
              <form onSubmit={handleSubmit} className="space-y-4">
                {/* Role Selector during Registration */}
                {tab === 'register' && (
                  <div className="space-y-1.5">
                    <label className="text-xs font-semibold text-stone-700">Account Type (Role)</label>
                    <div className="grid grid-cols-2 gap-2">
                      <button
                        type="button"
                        onClick={() => setRole('FARMER')}
                        className={`p-2.5 rounded-lg border text-xs font-semibold flex items-center justify-center gap-1.5 transition-all ${
                          role === 'FARMER'
                            ? 'border-emerald-700 bg-emerald-50 text-emerald-900'
                            : 'border-stone-200 bg-white text-stone-600'
                        }`}
                      >
                        <User className="w-3.5 h-3.5 text-emerald-700" /> Farmer
                      </button>
                      <button
                        type="button"
                        onClick={() => setRole('BUYER')}
                        className={`p-2.5 rounded-lg border text-xs font-semibold flex items-center justify-center gap-1.5 transition-all ${
                          role === 'BUYER'
                            ? 'border-emerald-700 bg-emerald-50 text-emerald-900'
                            : 'border-stone-200 bg-white text-stone-600'
                        }`}
                      >
                        <Badge variant="outline" size="sm">BUYER</Badge> Produce Buyer
                      </button>
                    </div>
                  </div>
                )}

                {tab === 'register' && (
                  <Input
                    label="Full Name"
                    placeholder="Enter your name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                )}

                <Input
                  label="Email Address"
                  type="email"
                  placeholder="ramesh@agrisathi.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />

                {tab === 'register' && (
                  <Input
                    label="Phone Number"
                    placeholder="10-digit mobile number"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    required
                  />
                )}

                <Input
                  label="Password"
                  type="password"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  leftIcon={<Lock className="w-4 h-4" />}
                  required
                />

                {errorMsg && (
                  <div className="p-3 rounded-lg bg-red-50 border border-red-200 text-red-800 text-xs font-medium">
                    {errorMsg}
                  </div>
                )}

                {successMsg && (
                  <div className="p-3 rounded-lg bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs font-medium flex items-center gap-1.5">
                    <CheckCircle2 className="w-4 h-4 shrink-0 text-emerald-700" />
                    {successMsg}
                  </div>
                )}

                <Button
                  type="submit"
                  size="lg"
                  className="w-full mt-2 font-semibold shadow-xs"
                  isLoading={isLoading}
                  rightIcon={<ArrowRight className="w-4 h-4" />}
                >
                  {tab === 'login' ? 'Sign In to Portal' : 'Create Account'}
                </Button>
              </form>

              <div className="mt-4 pt-4 border-t border-stone-100 text-center">
                <p className="text-xs text-stone-500">
                  Demo credentials: <span className="font-medium text-stone-800">ramesh@agrisathi.com</span> / <span className="font-medium text-stone-800">Password@123</span>
                </p>
              </div>
            </CardContent>
          </Card>
        </div>
      </main>

      {/* Footer */}
      <footer className="py-4 text-center text-xs text-stone-500 border-t border-stone-200/80 bg-white">
        AgriSathi Platform • Secure Authentication
      </footer>
    </div>
  );
}
