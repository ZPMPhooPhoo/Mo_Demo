import api from "../lib/axiosconfig";
import { updateAuthToken } from "../lib/axiosconfig";

export const TaskService = {
   async getTasks() {
       try {
           updateAuthToken(JSON.parse(localStorage.getItem("user") || "{}")?.token);
           const response = await api.get("/api/tasks/task-by-user");
           return response.data;
       } catch (error) {
           console.log(error);
           return [];
       }
   },

    async submitById( id: string){
    try{
        updateAuthToken(JSON.parse(localStorage.getItem("user") || "{}")?.token);
        const response = await api.put(`/api/tasks/${id}/submit`)
        return response.data;
    }catch(error){
        console.log(error);
        return null;
    }
   },

   async createTask (title:string,description:string){
    try{
        updateAuthToken(JSON.parse(localStorage.getItem("user") || "{}")?.token);
        const response = await api.post("/api/tasks/create",{title,description});
        return response.data;
    }catch(error){
        console.log(error);
        return null;
    }
   },

   async getAllTask(){
    try{
        updateAuthToken(JSON.parse(localStorage.getItem("user") || "{}")?.token);
        const response = await api.get("/api/tasks/submitted");
        return response.data;
    }catch(error){
        console.log(error);
        return [];
    }
   },
   async rejectTask(id: string, reason: string) {
    try {
        updateAuthToken(JSON.parse(localStorage.getItem("user") || "{}")?.token);
        const response = await api.put(
            `/api/tasks/${id}/reject`,
            reason,  // Send as plain text
            {
                headers: {
                    'Content-Type': 'text/plain'
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error('Failed to reject task:', error);
        throw error;
    }
   },
   async approveTask(id:string){
    try{
        updateAuthToken(JSON.parse(localStorage.getItem("user") || "{}")?.token);
        const response = await api.put(`/api/tasks/${id}/approve`)
        return response.data;
    }catch(error){
        console.log(error);
        return null;
    }
   }
}
