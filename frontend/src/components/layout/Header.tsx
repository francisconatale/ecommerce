'use client';

import { useState } from 'react';
import Link from 'next/link';
import { ShoppingCartIcon, ChevronDownIcon, Bars3Icon, XMarkIcon } from '@heroicons/react/24/outline';

const navItemClass = "text-base font-light text-gray-700 hover:text-gray-400 transition-colors tracking-wide";
const dropdownItemClass = "px-4 py-2 text-base font-light text-gray-600 hover:bg-gray-50 hover:text-gray-900 block tracking-wide";
const megaMenuItemClass = "text-base font-light text-gray-600 hover:text-gray-900 block tracking-wide";
const mobileItemClass = "block px-3 py-2 rounded-none text-lg font-light text-gray-700 hover:text-gray-900 hover:bg-gray-50";

// --- SUBCOMPONENTES DE BOTONES ---

export function ButtonNormal({ name, href }: { name: string, href: string }) {
  return (
    <Link href={href || '#'} className={navItemClass}>
      {name}
    </Link>
  );
}

export function ButtonInteractive({ name, onHoverProps }: { name: string, onHoverProps: any }) {
  const { component, links, menuGroups } = onHoverProps || {};

  return (
    <div className="relative group">
      <button className={`flex items-center gap-1 ${navItemClass}`}>
        {name}
        <ChevronDownIcon className="w-4 h-4 text-gray-500 group-hover:text-gray-400 transition-transform group-hover:rotate-180" />
      </button>

      {/* Renderizado dinámico del componente hover */}
      {component === 'ProductsMenu' && (
        <div className="absolute top-full left-0 mt-2 w-48 bg-white border border-gray-100 rounded-lg shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 overflow-hidden">
          <div className="flex flex-col py-2">
            {links?.map((link: any, idx: number) => (
              <Link key={idx} href={link.href || '#'} className={dropdownItemClass}>
                {link.name}
              </Link>
            ))}
          </div>
        </div>
      )}

      {component === 'MegaMenu' && (
        <div className="absolute top-full right-0 mt-2 w-[400px] bg-white border border-gray-100 rounded-lg shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 p-6">
          <div className="grid grid-cols-2 gap-8">
            {menuGroups?.map((group: any, idx: number) => (
              <div key={idx}>
                <h3 className="text-xs font-bold uppercase text-gray-900 mb-4 tracking-wider">{group.title}</h3>
                <div className="flex flex-col gap-3">
                  {group.links?.map((link: any, lIdx: number) => (
                    <Link key={lIdx} href={link.href || '#'} className={megaMenuItemClass}>
                      {link.name}
                    </Link>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

// --- COMPONENTE PRINCIPAL ---
export default function Header({ config }: { config?: any }) {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [cartCount] = useState(0);

  // Fallbacks usando la configuración si existe, o arreglos vacíos
  const leftLinks = config?.leftLinks || [];
  const rightLinks = config?.rightLinks || [];
  const mobileLinks = config?.mobileLinks || [];
  const logoUrl = config?.logoUrl || "https://cdn.prod.website-files.com/630784cc70ef0552ae1e91dc/63334bd4541e515298dcb4bd_logo-commerce-webflow-ecommerce-template.svg";

  const renderLink = (link: any, idx: number) => {
    if (link.type === 'interactive') {
      return <ButtonInteractive key={idx} name={link.name} onHoverProps={link.onHoverProps} />;
    }
    return <ButtonNormal key={idx} name={link.name} href={link.href} />;
  };

  return (
    <header className="sticky top-0 z-50 w-full bg-white border-b border-gray-100 shadow-sm">
      <div className="container mx-auto px-4 lg:px-8">
        <div className="flex h-20 items-center justify-between">
          
          {/* Lado Izquierdo - Links (Desktop) */}
          <nav className="hidden lg:flex items-center gap-8">
            {leftLinks.map((link: any, idx: number) => renderLink(link, idx))}
          </nav>

          {/* Centro - Logo */}
          <div className="flex-shrink-0 flex items-center justify-center lg:flex-1">
            <Link href="/" className="block">
              <img 
                src={logoUrl}
                alt="Commerce X" 
                className="h-8 w-auto" 
              />
            </Link>
          </div>

          {/* Lado Derecho - Pages, Contact y Carrito */}
          <div className="flex items-center justify-end gap-6">
            <nav className="hidden lg:flex items-center gap-8">
              {rightLinks.map((link: any, idx: number) => renderLink(link, idx))}
            </nav>

            {/* Divisor vertical */}
            <div className="hidden lg:block h-6 w-px bg-gray-200"></div>

            {/* Carrito de compras */}
            <button className="relative p-2 text-gray-700 hover:text-gray-400 transition-colors group">
              <ShoppingCartIcon className="w-6 h-6" />
              <span className="absolute top-0 right-0 inline-flex items-center justify-center px-1.5 py-0.5 text-xs font-bold leading-none text-white transform translate-x-1/4 -translate-y-1/4 bg-gray-400 rounded-full">
                {cartCount}
              </span>
            </button>

            {/* Botón menú hamburguesa (Mobile) */}
            <button 
              className="lg:hidden p-2 text-gray-700"
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              aria-label="Toggle Menu"
            >
              {isMobileMenuOpen ? <XMarkIcon className="w-6 h-6" /> : <Bars3Icon className="w-6 h-6" />}
            </button>
          </div>
        </div>
      </div>

      {/* Menú Mobile Desplegable */}
      {isMobileMenuOpen && (
        <div className="lg:hidden bg-white border-t border-gray-100 px-4 pt-2 pb-6 space-y-1 shadow-inner">
          {mobileLinks.map((link: any, idx: number) => (
            <Link key={idx} href={link.href || '#'} className={mobileItemClass}>
              {link.name}
            </Link>
          ))}
        </div>
      )}
    </header>
  );
}
