import { notFound } from 'next/navigation';
import { StorefrontConfig } from '@/types/storefront';
import StorefrontComposer from '@/components/storefront/StorefrontComposer';

export default async function TenantPage({
  params,
}: {
  params: Promise<{ tenant: string }>;
}) {
  const { tenant } = await params;

  let config: StorefrontConfig;
  try {
    const res = await fetch(
      `http://localhost:8080/api/v1/tenants/${tenant}/storefront`,
      { cache: 'no-store' }
    );

    if (!res.ok) {
      if (res.status === 404) return notFound();
      throw new Error(`Backend returned ${res.status}`);
    }

    config = await res.json();
  } catch (error) {
    console.error(error);
    return (
      <div style={{ padding: '20px', color: 'red', fontFamily: 'sans-serif' }}>
        <h2>Error loading tenant configuration.</h2>
        <p>Make sure the Spring Boot backend is running on port 8080.</p>
      </div>
    );
  }

  return (
    <main style={{ fontFamily: 'sans-serif' }}>
      <StorefrontComposer config={config} />
    </main>
  );
}
