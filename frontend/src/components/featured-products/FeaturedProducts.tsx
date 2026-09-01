import Image from "next/image";
import Link from "next/link";
export default function FeaturedProducts({ config }: { config: any }) {
  const { title, description, cta, products } = config;

  return (
    <section className="mx-auto w-full max-w-[1455px] px-4 md:px-8 py-24">
      <div className="flex flex-col lg:flex-row gap-12 lg:gap-16">
        
        {/* Columna Izquierda: Sticky y Asimétrica */}
        <div className="w-full lg:w-[28%] flex flex-col items-start self-start sticky top-24 min-h-[500px]">
          <div>
            <h2 className="text-[56px] font-light leading-[1.1] text-black tracking-tight">
              {title}
            </h2>
          </div>
          
          <div className="mt-auto pt-32">
            <p className="text-[#666666] text-[18px] font-light leading-relaxed mb-8 max-w-[300px]">
              {description}
            </p>
            <Link 
              href={cta.href}
              className="inline-flex h-[56px] px-10 items-center justify-center bg-black text-white text-[16px] font-medium rounded-none hover:bg-gray-500 transition-colors"
            >
              {cta.label}
            </Link>
          </div>
        </div>

        {/* Columna Derecha: Grid 2x3 */}
        <div className="w-full lg:w-[72%]">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-x-6 gap-y-12">
            {products.map((product) => (
              <div key={product.id} className="group flex flex-col cursor-pointer">
                {/* Contenedor Cuadrado Gris Claro */}
                <div className="relative w-full aspect-square bg-[#f6f6f6] mb-5 overflow-hidden flex items-center justify-center rounded-sm">
                  <div className="relative w-4/5 h-4/5">
                    <Image
                      src={product.image}
                      alt={product.name}
                      fill
                      className="object-contain drop-shadow-[0_10px_15px_rgba(0,0,0,0.1)] transition-transform duration-500 group-hover:scale-105"
                      sizes="(max-width: 768px) 100vw, (max-width: 1200px) 33vw, 25vw"
                    />
                  </div>
                  
                  {product.isOnSale && (
                    <span className="absolute top-4 right-4 bg-white px-3 py-1 text-[10px] font-bold tracking-wider uppercase border border-zinc-200">
                      Sale
                    </span>
                  )}
                </div>

                {/* Info del Producto */}
                <div className="flex flex-col text-left mt-2">
                  <h3 className="text-[18px] font-light text-black mb-1">
                    {product.name}
                  </h3>
                  <div className="flex items-center gap-3">
                    <span className="text-[16px] text-zinc-600 font-medium">
                      ${product.price.toFixed(2)} USD
                    </span>
                    {product.originalPrice && (
                      <span className="text-[15px] text-zinc-400 line-through">
                        ${product.originalPrice.toFixed(2)}
                      </span>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
