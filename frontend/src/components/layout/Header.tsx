import Logo from "./Header/Logo";
import Search from "./Header/Search";
import Account from "./Header/Account";
import Orders from "./Header/Orders";
import Cart from "./Header/Cart";

export default function Header() {
  return (
    <header className="sticky top-0 z-50 w-full border-b bg-white">
      <div className="container mx-auto px-4 h-16 flex items-center justify-between">
        <Logo />
        <Search />
        <div className="flex items-center space-x-2">
          <Account />
          <Orders />
          <Cart />
        </div>
      </div>
    </header>
  );
}
