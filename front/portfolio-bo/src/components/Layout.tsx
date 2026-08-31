import { NavLink, Outlet, useNavigate } from 'react-router';
import { useAuth } from '../lib/use-auth';

export function Layout() {
  const { username, logout } = useAuth();
  const navigate = useNavigate();

  async function handleLogout() {
    await logout();
    navigate('/login', { replace: true });
  }

  return (
    <div className="app-shell">
      <header className="app-header">
        <span className="app-title">Portfolio Admin</span>
        <nav className="app-nav">
          <NavLink to="/projects" className={({ isActive }) => (isActive ? 'active' : '')}>
            프로젝트 관리
          </NavLink>
          <NavLink to="/tech-stacks" className={({ isActive }) => (isActive ? 'active' : '')}>
            기술 스택 관리
          </NavLink>
        </nav>
        <div className="app-user">
          <span>{username}</span>
          <button type="button" onClick={handleLogout}>
            로그아웃
          </button>
        </div>
      </header>
      <main className="app-main">
        <Outlet />
      </main>
    </div>
  );
}
