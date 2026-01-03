import { Outlet } from "react-router-dom";
function RootLayout() {
  return (
    <div className="flex min-h-screen flex-col">
      <main className="mt-16 flex-1">
        <Outlet />
      </main>
      <hr />
    </div>
  );
}

export default RootLayout;
