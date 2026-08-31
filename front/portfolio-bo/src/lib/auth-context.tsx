import { useCallback, useEffect, useState, type ReactNode } from 'react';
import { ApiError, login as loginRequest, logout as logoutRequest, me } from './api';
import { AuthContext, type AuthContextValue } from './auth-context-def';

type AuthStatus = AuthContextValue['status'];

export function AuthProvider({ children }: { children: ReactNode }) {
  const [status, setStatus] = useState<AuthStatus>('checking');
  const [username, setUsername] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    me()
      .then((res) => {
        if (cancelled) return;
        setUsername(res.username);
        setStatus('authenticated');
      })
      .catch(() => {
        if (cancelled) return;
        setStatus('unauthenticated');
      });

    return () => {
      cancelled = true;
    };
  }, []);

  const login = useCallback(async (id: string, password: string) => {
    const res = await loginRequest(id, password);
    setUsername(res.username);
    setStatus('authenticated');
  }, []);

  const logout = useCallback(async () => {
    try {
      await logoutRequest();
    } catch (err) {
      // Cookie may already be gone/expired server-side — proceed to clear local state either way.
      if (!(err instanceof ApiError) || err.status !== 401) throw err;
    }
    setUsername(null);
    setStatus('unauthenticated');
  }, []);

  return (
    <AuthContext.Provider value={{ status, username, login, logout }}>{children}</AuthContext.Provider>
  );
}
