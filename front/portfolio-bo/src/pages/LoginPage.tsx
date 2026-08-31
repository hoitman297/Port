import { useState, type FormEvent } from 'react';
import { Navigate, useNavigate } from 'react-router';
import { ApiError } from '../lib/api';
import { useAuth } from '../lib/use-auth';

export function LoginPage() {
  const { status, login } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  if (status === 'authenticated') return <Navigate to="/projects" replace />;

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await login(username, password);
      navigate('/projects', { replace: true });
    } catch (err) {
      setError(err instanceof ApiError ? err.message : '로그인 중 오류가 발생했습니다.');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="login-page">
      <form className="login-form" onSubmit={handleSubmit}>
        <h1>관리자 로그인</h1>

        {error && <p className="error-text">{error}</p>}

        <label htmlFor="username">아이디</label>
        <input
          id="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          autoComplete="username"
          required
        />

        <label htmlFor="password">비밀번호</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          autoComplete="current-password"
          required
        />

        <button type="submit" disabled={submitting}>
          {submitting ? '로그인 중…' : '로그인'}
        </button>
      </form>
    </div>
  );
}
