import { Navigate, Route, Routes } from 'react-router';
import { Layout } from './components/Layout';
import { ProtectedRoute } from './components/ProtectedRoute';
import { LoginPage } from './pages/LoginPage';
import { ProjectFormPage } from './pages/ProjectFormPage';
import { ProjectsListPage } from './pages/ProjectsListPage';
import { TechStacksPage } from './pages/TechStacksPage';

export function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<Layout />}>
          <Route index element={<Navigate to="/projects" replace />} />
          <Route path="/projects" element={<ProjectsListPage />} />
          <Route path="/projects/new" element={<ProjectFormPage />} />
          <Route path="/projects/:id" element={<ProjectFormPage />} />
          <Route path="/tech-stacks" element={<TechStacksPage />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
