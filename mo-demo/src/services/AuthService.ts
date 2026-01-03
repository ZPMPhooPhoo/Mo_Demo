import api from "../lib/axiosconfig";

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