import { SidebarTrigger } from "@/components/ui/sidebar"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Separator } from "@/components/ui/separator"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { useState, useEffect } from "react";
import { getProfile, logout } from "@/services/AuthService";
import { useNavigate } from "react-router-dom";
interface HeaderProps {
  name?: string;
  email?: string;
  role?: string;
  Status?: string;
}

function Header() {
  const navigate = useNavigate();
  const [profile, setProfile] = useState<HeaderProps | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        setIsLoading(true);
        const res = await getProfile();
        setProfile(res);
      } catch (err) {
        console.error("Failed to fetch profile:", err);
        setError("Failed to load profile");
      } finally {
        setIsLoading(false);
      }
    };

    fetchProfile();
  }, []);
  const logoutHandler = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="w-full h-15 bg-[#6AB55B] flex items-center px-4 justify-between">
      <SidebarTrigger className="text-white" />
      <div>
        <DropdownMenu>
          <DropdownMenuTrigger asChild className="cursor-pointer">
            <Avatar>
              <AvatarImage src="https://github.com/shadcn.png" />
              <AvatarFallback>
                {profile?.name?.charAt(0).toUpperCase() || "U"}
              </AvatarFallback>
            </Avatar>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="w-56" align="end">
            <DropdownMenuLabel>Profile</DropdownMenuLabel>
            <Separator />
            <DropdownMenuGroup className="p-2 space-y-2">
              {isLoading ? (
                <p className="text-sm">Loading...</p>
              ) : error ? (
                <p className="text-sm text-red-500">{error}</p>
              ) : (
                <>
                  <div className="space-y-1">
                    <p className="font-medium">{profile?.name || "N/A"}</p>
                    <p className="text-sm text-gray-500">{profile?.email || "N/A"}</p>
                    {profile?.role && (
                      <p className="text-xs text-gray-400">{profile.role}</p>
                    )}
                  </div>
                  <Separator />
                  <DropdownMenuItem 
                    className="cursor-pointer focus:bg-red-50 focus:text-red-600"
                    onClick={logoutHandler}
                  >
                    Logout
                  </DropdownMenuItem>
                </>
              )}
            </DropdownMenuGroup>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </div>
  );
}

export default Header;