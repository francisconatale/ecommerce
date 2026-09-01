import Image from "next/image";
import { Product } from "@/types/product";

interface ProductCardProps {
  product: Product;
}

export function ProductCard({ product }: ProductCardProps) {
  const currency = product.currency ?? "USD";

  return (
    <article className="min-w-0">
      <div className="relative aspect-[0.857] overflow-hidden bg-[#f6f6f6]">
        {product.isOnSale && (
          <span className="absolute right-[9px] top-[10px] z-10 flex h-[22px] w-[40px] items-center justify-center border border-[#d9d9d9] bg-white text-[8px] font-normal leading-none">
            SALE
          </span>
        )}

        <Image
          src={product.image}
          alt={product.name}
          fill
          sizes="(max-width: 700px) 50vw, 33vw"
          className="object-contain"
        />
      </div>

      <div className="pt-3">
        <h3 className="mb-2 text-[16px] font-light leading-[1.1] text-black">
          {product.name}
        </h3>

        <div className="flex items-center gap-2 text-[14px] leading-none">
          <span className="text-zinc-600 font-medium">
            $ {product.price.toFixed(2)} {currency}
          </span>

          {product.originalPrice !== undefined && (
            <>
              <span className="text-zinc-400 line-through">
                $ {product.originalPrice.toFixed(2)} {currency}
              </span>
            </>
          )}
        </div>
      </div>
    </article>
  );
}
