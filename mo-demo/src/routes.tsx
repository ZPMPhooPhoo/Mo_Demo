import { lazy, Suspense } from "react";
import { createBrowserRouter } from "react-router-dom";
import RootLayout from "@/pages/Layout";
import Error404 from "@/pages/error";
const Login = lazy(() => import("@/Auth/login"));
const Register = lazy(() => import("@/Auth/register"));
const TaskManagement = lazy(() => import("@/pages/Dashboard/TaskManagement"));

export const routes = createBrowserRouter([
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
  // Add a root route when needed
  {
    path: "/dashboard",
    element: <RootLayout />,
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
  // Add a catch-all route for 404s
  {
    path: "*",
    element: <Error404 />,
  },
]);
