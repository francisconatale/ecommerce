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
            <div className={`flex max-w-[430px] flex-col text-white ${contentAlignClasses[align]}`}>
              <h2 className="max-w-[380px] text-[48px] font-medium leading-[1.13] tracking-[-1px]">
                {title}
              </h2>

              <p className="mt-6 max-w-[400px] text-[20px] font-normal leading-[1.45]">
                {description}
              </p>

              <Link
                href={cta.href}
                className="mt-6 inline-flex h-[68px] min-w-[280px] items-center justify-center rounded-[4px] bg-white px-8 text-[20px] font-normal text-[#222] transition-opacity hover:opacity-90"
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
