import { lazy, Suspense } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";
import { RootLayout } from "@/pages/Layout";
import Error404 from "@/pages/error";
const Login = lazy(() => import("@/Auth/login"));

// Auth Guard Component
const AuthGuard = ({ children }: { children: React.ReactNode }) => {
  const user = localStorage.getItem("user");
  
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  
  return <>{children}</>;
};
const Register = lazy(() => import("@/Auth/register"));
const TaskManagement = lazy(() => import("@/pages/Dashboard/TaskManagement"));

export const routes = createBrowserRouter([
  
  {
    path: "/",
    element: <Navigate to="/login" replace />,
  },
  {
    path: "/login",
    element: (
      <Suspense fallback={<div>Loading...</div>}>
        <Login />
      </Suspense>
    ),
  },
  {
    path: "/register",
    element: (
      <Suspense fallback={<div>Loading...</div>}>
        <Register />
      </Suspense>
    ),
  },
  {
    path: "/dashboard",
    element: (
      <AuthGuard>
        <RootLayout />
      </AuthGuard>
    ),
    children: [
      {
        index: true,
        element: (
          <Suspense fallback={<div>Loading...</div>}>
            <TaskManagement />
          </Suspense>
        ),
      }
    ],
  },
  {
    path: "*",
    element: <Error404 />,
  },
]);
