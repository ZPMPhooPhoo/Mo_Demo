import api from "../lib/axiosconfig";
import { updateAuthToken } from "../lib/axiosconfig";
export const register = async (name: string, email: string, password: string, isApprover: boolean) => {
    console.log(name, email, password, isApprover);
    const role = isApprover ? "APPROVER" : "USER";
    try {
        const response = await api.post("/api/auth/register", { name, email, password, role });
        if (response.status === 201) {
            return true;
        }
        return false;
    } catch (error) {
        console.log(error);
        return false;
    }
}

export const login = async (email: string, password: string) => {
    try {
        const response = await api.post("/api/auth/login", { email, password });
        if (response.data.token) {
            localStorage.setItem("user", JSON.stringify(response.data));
            return true;
        }
        return false;
    } catch (error) {
        console.log(error);
        return false;
    }
}

export interface ProfileResponse {
  name?: string;
  email?: string;
  role?: string;
  status?: string;  // Note: Changed from 'Status' to 'status' for consistency
  [key: string]: unknown;  // For any additional fields
}

export const getProfile = async (): Promise<ProfileResponse | null> => {
  try {
   updateAuthToken(JSON.parse(localStorage.getItem("user") || "{}")?.token);
    const response = await api.get<ProfileResponse>("/api/auth/profile");
    return response.data;
  } catch (error) {
    console.error("Error fetching profile:", error);
    
    return null;
  }
};

export const logout = () => {
  localStorage.removeItem("user");
  updateAuthToken(null);
};
