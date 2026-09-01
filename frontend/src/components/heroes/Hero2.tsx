"use client";

import Image from "next/image";
import Link from "next/link";
import { siteContent } from "@/config/siteContent";

export default function Hero2({ config }: { config: any }) {
  const { title, description, image, cta } = config;

  return (
    <section className="w-full py-16 bg-white">
      <div className="mx-auto w-full max-w-[1455px] px-4 md:px-8">
        <div className="flex flex-col md:flex-row gap-8 items-center bg-[#f9f9f9] rounded-3xl overflow-hidden p-8 md:p-12">
          
          <div className="w-full md:w-1/2 flex flex-col items-start text-left">
            <span className="text-sm font-medium tracking-widest uppercase text-gray-500 mb-4">
              Featured Edition
            </span>
            <h2 className="text-[48px] md:text-[64px] font-light leading-[1.1] tracking-tight text-black mb-6">
              {title}
            </h2>
            <p className="text-[#666] text-[20px] font-light leading-relaxed mb-8 max-w-[450px]">
              {description}
            </p>
            <Link 
              href={cta.href}
              className="inline-flex h-[56px] px-8 items-center justify-center bg-black text-white text-[16px] font-medium rounded-none hover:bg-gray-500 transition-colors"
            >
              {cta.label}
            </Link>
          </div>

          <div className="w-full md:w-1/2 h-[400px] md:h-[500px] relative rounded-2xl overflow-hidden">
            <Image
              src={image}
              alt=""
              fill
              className="object-cover"
              sizes="(max-width: 768px) 100vw, 50vw"
            />
          </div>

        </div>
      </div>
    </section>
  );
}
