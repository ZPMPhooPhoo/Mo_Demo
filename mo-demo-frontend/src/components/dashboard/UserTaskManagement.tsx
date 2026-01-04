import { useEffect, useState } from "react";
import { TaskService } from "@/services/TaskService";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";

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
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        title: '',
        description: ''
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
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
    async function handleStatusChange(taskId: string) {
        await TaskService.submitById(taskId);
        location.reload();
    }

    async function handleCreateTask() {
        if (!formData.title.trim()) return;
        
        setIsSubmitting(true);
        try {
            const newTask = await TaskService.createTask(formData.title, formData.description);
            if (newTask) {
                setTasks([newTask, ...tasks]);
                setFormData({ title: '', description: '' });
                setIsModalOpen(false);
            }
        } catch (error) {
            console.error('Failed to create task:', error);
        } finally {
            setIsSubmitting(false);
        }
    }

  return (
    <div className="w-full h-full p-2">
        <div className="w-full flex justify-between">
            <h1 className="text-xl font-semibold">User Task Management</h1>
            <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
                <DialogTrigger asChild>
                    <Button className="bg-[#6AB55B] hover:bg-[#5aa34c] text-white">
                        Add Task
                    </Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-[425px]">
                    <DialogHeader>
                        <DialogTitle>Create New Task</DialogTitle>
                    </DialogHeader>
                    <div className="grid gap-4 py-4">
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="title" className="text-right">
                                Title
                            </Label>
                            <Input
                                id="title"
                                value={formData.title}
                                onChange={(e) => setFormData({...formData, title: e.target.value})}
                                className="col-span-3"
                                placeholder="Enter task title"
                            />
                        </div>
                        <div className="grid grid-cols-4 items-start gap-4">
                            <Label htmlFor="description" className="text-right pt-2">
                                Description
                            </Label>
                            <Textarea
                                id="description"
                                value={formData.description}
                                onChange={(e) => setFormData({...formData, description: e.target.value})}
                                className="col-span-3 min-h-[100px]"
                                placeholder="Enter task description"
                            />
                        </div>
                    </div>
                    <div className="flex justify-end gap-2">
                        <Button 
                            variant="outline" 
                            onClick={() => setIsModalOpen(false)}
                            disabled={isSubmitting}
                        >
                            Cancel
                        </Button>
                        <Button 
                            className="bg-[#6AB55B] hover:bg-[#5aa34c]"
                            onClick={handleCreateTask}
                            disabled={isSubmitting || !formData.title.trim()}
                        >
                            {isSubmitting ? 'Creating...' : 'Create Task'}
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>
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
                            <DropdownMenuItem onClick={() => alert("Coming Soon!")}>Edit</DropdownMenuItem>
                            <DropdownMenuItem onClick={() => alert("Coming Soon!")}>Delete</DropdownMenuItem>
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