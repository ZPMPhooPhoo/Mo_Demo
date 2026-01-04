import UserTaskManagement from "@/components/dashboard/UserTaskManagement";
import ApproverTaskManagement from "@/components/dashboard/ApproverTaskManagement";

function TaskManagement() {
  const role = JSON.parse(localStorage.getItem("user") || "{}")?.role;
  if (role === "USER") {
    return <UserTaskManagement />;
  }
  if (role === "APPROVER") {
    return <ApproverTaskManagement />;
  }
  return <div className="w-full h-full">
    <UserTaskManagement />
  </div>;
}

export default TaskManagement;
