export default function Search() {
  return (
    <div className="flex-1 max-w-md mx-4">
      <input
        type="search"
        placeholder="Search products..."
        className="w-full px-4 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-black"
      />
    </div>
  );
}
