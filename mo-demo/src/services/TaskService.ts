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
   }
}
