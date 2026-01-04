
import { useEffect, useState } from "react";
import { TaskService } from "@/services/TaskService";
import { Button } from "@/components/ui/button";

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

function ApproverTaskManagement() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState<{ [key: string]: 'approve' | 'reject' | null }>({});

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        setLoading(true);
        const response = await TaskService.getAllTask();
        setTasks(Array.isArray(response) ? response : []);
      } catch (error) {
        console.error('Failed to fetch tasks:', error);
        setTasks([]);
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const handleApprove = async (taskId: string) => {
    try {
      setActionLoading(prev => ({ ...prev, [taskId]: 'approve' }));
      await TaskService.approveTask(taskId);
      // Refresh tasks after approval
      const updatedTasks = await TaskService.getAllTask();
      setTasks(Array.isArray(updatedTasks) ? updatedTasks : []);
    } catch (error) {
      console.error('Failed to approve task:', error);
      alert('Failed to approve task. Please try again.');
    } finally {
      setActionLoading(prev => ({ ...prev, [taskId]: null }));
    }
  };

  const handleReject = async (taskId: string) => {
    const reason = prompt('Please enter rejection reason:');
    if (reason) {
      try {
        setActionLoading(prev => ({ ...prev, [taskId]: 'reject' }));
        await TaskService.rejectTask(taskId, reason);
        // Refresh tasks after rejection
        const updatedTasks = await TaskService.getAllTask();
        setTasks(Array.isArray(updatedTasks) ? updatedTasks : []);
      } catch (error) {
        console.error('Failed to reject task:', error);
        alert('Failed to reject task. Please try again.');
      } finally {
        setActionLoading(prev => ({ ...prev, [taskId]: null }));
      }
    }
  };

  if (loading) {
    return <div className="p-4">Loading tasks...</div>;
  }

  return (
    <div className="p-2">
      <h1 className="text-xl font-semibold mb-4">Approver Task Management</h1>
      <div className="mt-2 overflow-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-gray-100 text-sm text-gray-600">
              <th className="px-4 py-2 border text-left w-12">No.</th>
              <th className="px-4 py-2 border text-left">Title</th>
              <th className="px-4 py-2 border text-left">Description</th>
              <th className="px-4 py-2 border text-left w-24">Status</th>
              <th className="px-4 py-2 border text-left w-32">Created By</th>
              <th className="px-4 py-2 border text-left w-36">Created At</th>
              <th className="px-4 py-2 border text-left w-40">Actions</th>
            </tr>
          </thead>
          <tbody>
            {tasks.filter(task => task.status !== 'DRAFT').length > 0 ? (
              tasks.filter(task => task.status !== 'DRAFT').map((task, index) => (
                <tr key={task.id} className="hover:bg-gray-50">
                  <td className="px-4 py-2 border">{index + 1}</td>
                  <td className="px-4 py-2 border font-medium">{task.title}</td>
                  <td className="px-4 py-2 border text-sm text-gray-600">{task.description}</td>
                  <td className="px-4 py-2 border">
                    <span className={`px-2 py-1 text-xs rounded-full whitespace-nowrap ${
                      task.status === 'APPROVED' ? 'bg-green-100 text-green-800' :
                      task.status === 'SUBMITTED' ? 'bg-blue-100 text-blue-800' :
                      task.status === 'REJECTED' ? 'bg-red-100 text-red-800' :
                      'bg-gray-100 text-gray-800'
                    }`}>
                      {task.status.charAt(0) + task.status.slice(1).toLowerCase()}
                    </span>
                  </td>
                  <td className="px-4 py-2 border">
                    <div className="text-sm">{task.createdBy?.name || 'N/A'}</div>
                    <div className="text-xs text-gray-500">{task.createdBy?.role || 'N/A'}</div>
                  </td>
                  <td className="px-4 py-2 border text-sm">
                    {new Date(task.created_at).toLocaleDateString()}
                    <div className="text-xs text-gray-500">
                      {new Date(task.created_at).toLocaleTimeString()}
                    </div>
                  </td>
                  <td className="px-2 py-2 border">
                    <div className="flex gap-2 items-center justify-center">
                      {task.status === 'APPROVED' ? (
                        <span className="px-2 py-1 text-xs rounded-full whitespace-nowrap bg-green-100 text-green-800">
                          Approved
                        </span>
                      ) : task.status === 'REJECTED' ? (
                        <span className="px-2 py-1 text-xs rounded-full whitespace-nowrap bg-red-100 text-red-800">
                          Rejected
                        </span>
                      ) : task.status === 'SUBMITTED' ? (
                        <div className="flex gap-2">
                          <Button 
                            variant="outline" 
                            size="sm" 
                            className="h-8 text-xs cursor-pointer bg-green-500 text-white hover:border hover:border-green-500"
                            onClick={() => handleApprove(task.id)}
                            disabled={actionLoading[task.id] === 'approve' || actionLoading[task.id] === 'reject'}
                          >
                            {actionLoading[task.id] === 'approve' ? 'Approving...' : 'Approve'}
                          </Button>
                          <Button 
                            variant="outline" 
                            size="sm" 
                            className="h-8 text-xs cursor-pointer bg-red-500 text-white hover:border hover:border-red-500"
                            onClick={() => handleReject(task.id)}
                            disabled={actionLoading[task.id] === 'approve' || actionLoading[task.id] === 'reject'}
                          >
                            {actionLoading[task.id] === 'reject' ? 'Rejecting...' : 'Reject'}
                          </Button>
                        </div>
                      ) : (
                        <span className="px-2 py-1 text-xs rounded-full whitespace-nowrap bg-gray-100 text-gray-800">
                          {task.status}
                        </span>
                      )}
                    </div>
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
  );
}

export default ApproverTaskManagement;