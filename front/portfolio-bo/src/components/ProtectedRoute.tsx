import { Navigate, Outlet } from 'react-router';
import { useAuth } from '../lib/use-auth';

export function ProtectedRoute() {
  const { status } = useAuth();

  if (status === 'checking') return null;
  if (status === 'unauthenticated') return <Navigate to="/login" replace />;

  return <Outlet />;
}
