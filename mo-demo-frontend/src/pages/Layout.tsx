import { Outlet } from "react-router-dom";
import { SidebarProvider } from "@/components/ui/sidebar";
import Header from "@/components/dashboard/Header";
import { AppSidebar } from "@/components/dashboard/app-sidebar";
export const RootLayout = () => {

  return (
    <SidebarProvider>
      <AppSidebar />
      <main className="flex w-full flex-col h-screen">
        <Header />
        <Outlet />
      </main>
    </SidebarProvider>
  );
};