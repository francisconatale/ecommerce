export default function Cart() {
  return (
    <div className="px-3 hover:underline cursor-pointer flex items-center gap-1">
      <span>Cart</span>
      <span className="bg-black text-white text-xs font-bold px-2 py-1 rounded-full">
        0
      </span>
    </div>
  );
}
