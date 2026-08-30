'use client';

import { useState } from 'react';
import { StorefrontSection } from '@/types/storefront';
import StorefrontComposer from '@/components/storefront/StorefrontComposer';

// Secciones disponibles para agregar al layout
const AVAILABLE_SECTIONS: Array<Omit<StorefrontSection, 'priority'> & { label: string }> = [
  { type: 'hero', component: 'hero1', label: '🔴 Hero 1' },
  { type: 'hero', component: 'hero2', label: '🔵 Hero 2' },
  { type: 'banner', component: 'banner1', label: '🏷️ Banner Promo' },
  { type: 'featuredProducts', component: 'featuredProducts', label: '📦 Productos Destacados' },
];

// Config inicial de prueba
const INITIAL_SECTIONS: StorefrontSection[] = [
  { priority: 1, type: 'hero', component: 'hero1' },
  { priority: 2, type: 'banner', component: 'banner1' },
  { priority: 3, type: 'featuredProducts', component: 'featuredProducts' },
];

function rebuildPriorities(sections: StorefrontSection[]): StorefrontSection[] {
  return sections.map((s, i) => ({ ...s, priority: i + 1 }));
}

export default function TenantMockPage() {
  const [sections, setSections] = useState<StorefrontSection[]>(INITIAL_SECTIONS);

  const move = (index: number, direction: -1 | 1) => {
    const next = [...sections];
    const target = index + direction;
    if (target < 0 || target >= next.length) return;
    [next[index], next[target]] = [next[target], next[index]];
    setSections(rebuildPriorities(next));
  };

  const remove = (index: number) => {
    const next = sections.filter((_, i) => i !== index);
    setSections(rebuildPriorities(next));
  };

  const add = (item: (typeof AVAILABLE_SECTIONS)[number]) => {
    const next = [...sections, { type: item.type, component: item.component, priority: sections.length + 1 }];
    setSections(rebuildPriorities(next));
  };

  const config = { tenantSlug: 'tenant-mock', layoutType: 'default', sections };

  return (
    <div style={{ display: 'flex', height: '100vh', fontFamily: 'sans-serif', overflow: 'hidden' }}>

      {/* ── PANEL EDITOR ── */}
      <aside
        style={{
          width: '300px',
          minWidth: '300px',
          background: '#111',
          color: '#fff',
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden',
        }}
      >
        {/* Header */}
        <div style={{ padding: '20px 16px 12px', borderBottom: '1px solid #333' }}>
          <div style={{ fontSize: '11px', color: '#888', textTransform: 'uppercase', letterSpacing: '0.1em', marginBottom: '4px' }}>
            Tenant Mock Editor
          </div>
          <div style={{ fontSize: '18px', fontWeight: 700 }}>Layout Builder</div>
        </div>

        {/* Secciones activas */}
        <div style={{ flex: 1, overflowY: 'auto', padding: '16px' }}>
          <div style={{ fontSize: '11px', color: '#888', textTransform: 'uppercase', letterSpacing: '0.08em', marginBottom: '10px' }}>
            Secciones activas
          </div>

          {sections.length === 0 && (
            <div style={{ color: '#555', fontSize: '13px', padding: '12px 0' }}>
              Sin secciones. Agregá una abajo.
            </div>
          )}

          {sections.map((section, i) => {
            const meta = AVAILABLE_SECTIONS.find(
              (a) => a.type === section.type && a.component === section.component
            );
            return (
              <div
                key={`${section.component}-${i}`}
                style={{
                  background: '#1e1e1e',
                  border: '1px solid #2a2a2a',
                  borderRadius: '8px',
                  padding: '10px 12px',
                  marginBottom: '8px',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '8px',
                }}
              >
                {/* Priority badge */}
                <div
                  style={{
                    minWidth: '24px',
                    height: '24px',
                    background: '#333',
                    borderRadius: '50%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: '11px',
                    fontWeight: 700,
                    color: '#aaa',
                  }}
                >
                  {section.priority}
                </div>

                {/* Label */}
                <div style={{ flex: 1, fontSize: '13px' }}>
                  {meta?.label ?? `${section.type} / ${section.component}`}
                </div>

                {/* Controls */}
                <div style={{ display: 'flex', gap: '4px' }}>
                  <button
                    onClick={() => move(i, -1)}
                    disabled={i === 0}
                    style={btnStyle(i === 0)}
                    title="Subir"
                  >
                    ↑
                  </button>
                  <button
                    onClick={() => move(i, 1)}
                    disabled={i === sections.length - 1}
                    style={btnStyle(i === sections.length - 1)}
                    title="Bajar"
                  >
                    ↓
                  </button>
                  <button
                    onClick={() => remove(i)}
                    style={{ ...btnStyle(false), color: '#ff5555' }}
                    title="Eliminar"
                  >
                    ✕
                  </button>
                </div>
              </div>
            );
          })}
        </div>

        {/* Agregar secciones */}
        <div style={{ padding: '12px 16px', borderTop: '1px solid #333' }}>
          <div style={{ fontSize: '11px', color: '#888', textTransform: 'uppercase', letterSpacing: '0.08em', marginBottom: '10px' }}>
            Agregar sección
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
            {AVAILABLE_SECTIONS.map((item) => (
              <button
                key={`${item.type}-${item.component}`}
                onClick={() => add(item)}
                style={{
                  background: '#1e1e1e',
                  border: '1px solid #333',
                  borderRadius: '6px',
                  color: '#ccc',
                  padding: '8px 10px',
                  textAlign: 'left',
                  cursor: 'pointer',
                  fontSize: '13px',
                  transition: 'background 0.15s',
                }}
                onMouseEnter={(e) => (e.currentTarget.style.background = '#2a2a2a')}
                onMouseLeave={(e) => (e.currentTarget.style.background = '#1e1e1e')}
              >
                + {item.label}
              </button>
            ))}
          </div>

          {/* JSON dump */}
          <details style={{ marginTop: '16px' }}>
            <summary style={{ fontSize: '11px', color: '#555', cursor: 'pointer', userSelect: 'none' }}>
              Ver payload JSON
            </summary>
            <pre
              style={{
                marginTop: '8px',
                background: '#0a0a0a',
                padding: '10px',
                borderRadius: '6px',
                fontSize: '10px',
                color: '#6f6',
                overflowX: 'auto',
                maxHeight: '160px',
                overflowY: 'auto',
              }}
            >
              {JSON.stringify(config, null, 2)}
            </pre>
          </details>
        </div>
      </aside>

      {/* ── PREVIEW ── */}
      <main style={{ flex: 1, overflowY: 'auto', background: '#f0f0f0' }}>
        <div
          style={{
            background: '#fff',
            minHeight: '100%',
            boxShadow: '0 0 40px rgba(0,0,0,0.08)',
          }}
        >
          {/* Barra de preview */}
          <div
            style={{
              padding: '10px 16px',
              background: '#fafafa',
              borderBottom: '1px solid #e0e0e0',
              display: 'flex',
              alignItems: 'center',
              gap: '8px',
            }}
          >
            <div style={{ display: 'flex', gap: '6px' }}>
              <div style={{ width: 12, height: 12, borderRadius: '50%', background: '#ff5f57' }} />
              <div style={{ width: 12, height: 12, borderRadius: '50%', background: '#febc2e' }} />
              <div style={{ width: 12, height: 12, borderRadius: '50%', background: '#28c840' }} />
            </div>
            <div
              style={{
                flex: 1,
                background: '#e8e8e8',
                borderRadius: '4px',
                padding: '4px 12px',
                fontSize: '12px',
                color: '#666',
              }}
            >
              localhost:3000/tenant-mock
            </div>
          </div>

          {/* Render del storefront */}
          <StorefrontComposer config={config} />
        </div>
      </main>
    </div>
  );
}

function btnStyle(disabled: boolean): React.CSSProperties {
  return {
    background: disabled ? '#1a1a1a' : '#2a2a2a',
    border: '1px solid #333',
    borderRadius: '4px',
    color: disabled ? '#444' : '#aaa',
    cursor: disabled ? 'not-allowed' : 'pointer',
    width: '24px',
    height: '24px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '12px',
    padding: 0,
  };
}
