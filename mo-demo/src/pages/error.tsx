import { Link } from "react-router-dom";

export default function Error404() {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center items-center px-4">
      <div className="max-w-md w-full space-y-8 text-center">
        <div className="text-9xl font-bold text-[#6AB55B]">404</div>
        <h1 className="text-3xl font-extrabold text-[#6AB55B]">
          Page not found
        </h1>
        <p className="mt-4 text-lg text-[#6AB55B]">
          Sorry, we couldn't find the page you're looking for.
        </p>
        <div className="mt-8">
          <Link
            to="/"
            className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-[#6AB55B] hover:bg-[#6AB55B] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
          >
            Go back home
          </Link>
        </div>
      </div>
    </div>
  );
}
