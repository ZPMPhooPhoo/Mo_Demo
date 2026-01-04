import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { login } from "@/services/AuthService";



const LoginUser = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  // const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user") || "{}");
    if (user && user.id) {
      navigate("/dashboard");
    }
  }, [navigate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // setError("");
    
    if (!email || !password) {
      // setError("Please enter both email and password");
      return;
    }

    setIsLoading(true);
    
    try {
      const success = await login(email, password);
      if (success) {
        navigate("/dashboard", {
          state: {
            message: "Login successful!"
          }
        });
      } else {
        // setError("Invalid email or password");
      }
    } catch (error: any) {
      console.error("Login error:", error);
      // setError(
      //   error.response?.data?.message || 
      //   "An error occurred during login. Please try again."
      // );
    } finally {
      setIsLoading(false);
    }
  };
  return (
    <Card className="w-full max-w-sm border border-[#6AB55B]">
      <CardHeader>
        <CardTitle>Login</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={(e) => e.preventDefault()}>
          <div className="flex flex-col gap-6">
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
              <Input id="password" type="password" required value={password} onChange={(e) => setPassword(e.target.value)}/>
            </div>
          </div>
        </form>
      </CardContent>
      <CardFooter className="flex-col gap-2">
        <Button type="submit" className="w-full cursor-pointer bg-[#6AB55B] hover:bg-[#6AB55B]" onClick={handleSubmit} disabled={isLoading}>
           {isLoading ? "Submitting..." : "Submit"}
        </Button>
        <Link to="/register" className="text-center">I am new user <span className="text-blue-500">Go to register!</span></Link>
      </CardFooter>
    </Card>
  );
}

export default LoginUser