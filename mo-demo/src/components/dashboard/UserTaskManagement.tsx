import { useEffect, useState } from "react";
import { TaskService } from "@/services/TaskService";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

interface User {
  id: string;
  name: string;
  email: string;
  role: string;
  status: string;
  created_at: string;
  updated_at: string;
}

interface Task {
  id: string;
  title: string;
  description: string;
  status: 'DRAFT' | 'SUBMITTED' | 'APPROVED' | 'REJECTED';
  createdBy: User;
  approvedBy: User | null;
  rejectionReason: string | null;
  created_at: string;
  updated_at: string;
}

function UserTaskManagement() {
    const [tasks, setTasks] = useState<Task[]>([]);
useEffect(() => {
    const fetchTasks = async () => {
        try {
            const response = await TaskService.getTasks();
            // Ensure we have an array before setting state
            if (Array.isArray(response)) {
                setTasks(response);
            } else if (response && Array.isArray(response.data)) {
                // Handle case where API returns { data: [...] }
                setTasks(response.data);
            } else {
                console.error('Unexpected response format:', response);
                setTasks([]); // Fallback to empty array
            }
        } catch (error) {
            console.error('Failed to fetch tasks:', error);
            setTasks([]); // Reset to empty array on error
        }
    };
    fetchTasks();
}, [])
async function handleStatusChange(taskId: string) 
{
    await TaskService.submitById(taskId);  
    location.reload();
}

  return (
    <div className="w-full h-full p-2">
        <div className="w-full flex justify-between">
            <h1>User Task Management</h1>
            <div>
                <button className="bg-[#6AB55B] text-white px-4 py-2 rounded">Add Task</button>
            </div>
        </div>
      <div className="w-full h-full mt-2 overflow-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-gray-100 text-sm text-gray-600">
              <th className="px-4 py-2 border text-left w-12">No.</th>
              <th className="px-4 py-2 border text-left">Title</th>
              <th className="px-4 py-2 border text-left">Description</th>
              <th className="px-4 py-2 border text-left w-15">Status</th>
              <th className="px-4 py-2 border text-left w-32">Created By</th>
              <th className="px-4 py-2 border text-left w-36">Created At</th>
              <th className="px-4 py-2 border text-left w-12">Actions</th>
            </tr>
          </thead>
          <tbody>
            {Array.isArray(tasks) && tasks.length > 0 ? (
              tasks.map((task, index) => (
                <tr key={task.id} className="hover:bg-gray-50">
                  <td className="px-4 py-2 border">{index + 1}</td>
                  <td className="px-4 py-2 border font-medium">{task.title}</td>
                  <td className="px-4 py-2 border text-sm text-wrap text-gray-600">{task.description}</td>
                  <td className="px-4 py-2 border">
                    <span className={`px-2 py-1 text-sm rounded-full whitespace-nowrap ${
                      task.status === 'APPROVED' ? 'bg-green-100 text-green-800' :
                      task.status === 'SUBMITTED' ? 'bg-blue-100 text-blue-800' :
                      task.status === 'REJECTED' ? 'bg-red-100 text-red-800' :
                      'bg-gray-100 text-gray-800'
                    }`}>
                      {task.status.charAt(0) + task.status.slice(1).toLowerCase()}
                    </span>
                  </td>
                  <td className="px-4 py-2 border">
                    <div className="text-sm">{task.createdBy.name}</div>
                    <div className="text-xs text-gray-500">{task.createdBy.role}</div>
                  </td>
                  <td className="px-4 py-2 border text-sm">
                    {new Date(task.created_at).toLocaleDateString()}
                    <div className="text-xs text-gray-500">
                      {new Date(task.created_at).toLocaleTimeString()}
                    </div>
                  </td>
                  <td className="px-4 py-2 h-full border w-12  justify-center items-center">
                     <DropdownMenu>
                        <DropdownMenuTrigger asChild className="cursor-pointer">  
                        <button className="text-[#6AB55B] h-full w-8 flex justify-center items-center cursor-pointer hover:text-[#6AB55B]">...</button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent>
                            <DropdownMenuItem onClick={() => handleStatusChange(task.id)}>Submit</DropdownMenuItem>
                            <DropdownMenuItem>Edit</DropdownMenuItem>
                            <DropdownMenuItem>Delete</DropdownMenuItem>
                        </DropdownMenuContent>
                     </DropdownMenu>
                    
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={7} className="px-4 py-4 text-center text-gray-500 border">
                  No tasks found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default UserTaskManagement