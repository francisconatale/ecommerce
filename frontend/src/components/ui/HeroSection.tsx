import Image from "next/image";
import Link from "next/link";

interface HeroSectionProps {
  title: string;
  description: string;
  image: string;
  cta: {
    label: string;
    href: string;
  };
  align?: 'left' | 'center' | 'right';
}

export function HeroSection({
  title,
  description,
  image,
  cta,
  align = 'left',
}: HeroSectionProps) {
  const containerAlignClasses = {
    left: "justify-start",
    center: "justify-center text-center",
    right: "justify-end text-right",
  };

  const contentAlignClasses = {
    left: "items-start",
    center: "items-center",
    right: "items-end",
  };

  return (
    <section className="w-full py-16">
      <div className="mx-auto w-full max-w-[1455px] px-4 md:px-8">
        <div className="relative h-[540px] w-full overflow-hidden rounded-3xl">
          <Image
            src={image}
            alt=""
            fill
            priority
            sizes="100vw"
            className="object-cover object-center"
          />

          <div className={`relative z-10 flex h-full items-center p-10 md:p-16 ${containerAlignClasses[align]}`}>
            <div className={`flex max-w-[500px] flex-col text-white ${contentAlignClasses[align]}`}>
              <h2 className="max-w-[480px] text-[64px] font-light leading-[1.1] tracking-tight">
                {title}
              </h2>

              <p className="mt-6 max-w-[440px] text-[24px] font-light leading-relaxed text-gray-100">
                {description}
              </p>

              <Link
                href={cta.href}
                className="mt-8 inline-flex h-[64px] px-10 min-w-[200px] items-center justify-center rounded-none bg-white text-[18px] font-medium text-black transition-colors hover:bg-gray-300"
              >
                {cta.label}
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
