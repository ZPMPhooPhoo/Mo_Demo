import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox"
import { useState } from "react";
import { Link } from "react-router-dom";
import { register } from "@/services/AuthService";
import { useNavigate } from "react-router-dom";


function CreateUser() {
const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isApprover, setIsApprover] = useState(false);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);
    try {
      const response = await register(name, email, password, isApprover);
      if (response) {
        alert("Registration successful! Please log in.");
        navigate("/login", { 
          state: { 
            message: "Registration successful! Please log in." 
          } 
        });
      }else{
        alert("Registration failed. Please try again.");
        setError("Registration failed. Please try again.");
      }
    } catch (error: any) {
      alert("Registration failed. Please try again.");
      setError(
        error.response?.data?.message || 
        "Registration failed. Please try again."
      );
    } finally {
      setIsLoading(false);
    }
  };
  return (
    <Card className="w-full max-w-sm">
      <CardHeader>
        <CardTitle>Create New User</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={(e) => e.preventDefault()}>
          <div className="flex flex-col gap-6">
            <div className="grid gap-2">
              <Label htmlFor="name">Name</Label>
              <Input
                id="name"
                type="text"
                placeholder="Name"
                required
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                placeholder="jone@example.com"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div className="grid gap-2">
                <Label htmlFor="password">Password</Label>
              <Input id="password" type="password" required maxLength={8} minLength={8} value={password} onChange={(e) => setPassword(e.target.value)}/>
            </div>
            <div className="flex items-center gap-2">
              <Checkbox id="terms" checked={isApprover} onCheckedChange={(checked) => setIsApprover(checked as boolean)} className="cursor-pointer"/>
              <Label htmlFor="terms">APPROVER</Label>
            </div>

          </div>
        </form>
      </CardContent>
      <CardFooter className="flex-col gap-2">
        <Button type="submit" className="w-full cursor-pointer bg-[#6AB55B]" onClick={handleSubmit} disabled={isLoading}>
           {isLoading ? "Submitting..." : "Submit"}
        </Button>
        <Link to="/login" className="text-center">Already have an account? <span className="text-blue-500">Go to Login!</span></Link>
      </CardFooter>
    </Card>
  );
}

export default CreateUser;
