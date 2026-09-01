"use client";

import { useState } from "react";
import StorefrontComposer from "@/components/StorefrontComposer";
import { siteContent } from "@/config/siteContent";

export default function BuilderPage() {
  const [layout, setLayout] = useState(siteContent.layout);
  const [isSaving, setIsSaving] = useState(false);
  const [selectedSection, setSelectedSection] = useState<string | null>("section-2"); // Seleccionamos el hero por defecto

  const heroIndex = layout.findIndex((s) => s.type === "hero");
  const heroData = heroIndex !== -1 ? layout[heroIndex] : null;

  const updateHeroConfig = (key: string, value: string) => {
    if (heroIndex === -1) return;
    const newLayout = [...layout];
    newLayout[heroIndex] = {
      ...newLayout[heroIndex],
      config: { ...newLayout[heroIndex].config, [key]: value }
    };
    setLayout(newLayout);
  };

  const updateHeroCta = (key: string, value: string) => {
    if (heroIndex === -1) return;
    const newLayout = [...layout];
    newLayout[heroIndex] = {
      ...newLayout[heroIndex],
      config: {
        ...newLayout[heroIndex].config,
        cta: { ...newLayout[heroIndex].config.cta, [key]: value }
      }
    };
    setLayout(newLayout);
  };

  const updateHeroVariant = (variant: string) => {
    if (heroIndex === -1) return;
    const newLayout = [...layout];
    newLayout[heroIndex] = { ...newLayout[heroIndex], variant };
    setLayout(newLayout);
  };

  const saveToDisk = async () => {
    setIsSaving(true);
    try {
      const response = await fetch("/api/save-config", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ layout })
      });
      if (response.ok) {
        alert("Configuración publicada exitosamente.");
      } else {
        alert("Hubo un error al publicar.");
      }
    } catch (e) {
      alert("Error de red.");
    }
    setIsSaving(false);
  };

  return (
    <div className="flex h-screen w-full overflow-hidden bg-[#1e1e1e] font-sans selection:bg-[#0d99ff] selection:text-white">
      
      {/* LEFT SIDEBAR (Layers / Pages) */}
      <div className="w-[240px] bg-[#2c2c2c] border-r border-[#3c3c3c] flex flex-col z-50">
        <div className="h-10 border-b border-[#3c3c3c] flex items-center px-4">
          <span className="text-[11px] font-bold text-[#e0e0e0] uppercase tracking-widest">Pages</span>
        </div>
        <div className="p-2 flex-1">
          <div className="flex items-center px-2 py-1.5 bg-[#3c3c3c] rounded text-[12px] text-white cursor-pointer">
            <span className="mr-2">🏠</span> Home (Index)
          </div>
        </div>

        <div className="h-10 border-b border-t border-[#3c3c3c] flex items-center px-4 mt-auto">
          <span className="text-[11px] font-bold text-[#e0e0e0] uppercase tracking-widest">Layers</span>
        </div>
        <div className="p-2 flex-1 flex flex-col gap-0.5">
          {layout.map((s) => (
            <div 
              key={s.id}
              onClick={() => setSelectedSection(s.id)}
              className={`flex items-center px-2 py-1.5 rounded text-[12px] cursor-pointer transition-colors ${selectedSection === s.id ? 'bg-[#0d99ff] text-white' : 'text-[#a0a0a0] hover:bg-[#3c3c3c]'}`}
            >
              <span className="mr-2">❖</span> {s.type}
            </div>
          ))}
        </div>
      </div>

      {/* CANVAS (Live Preview) */}
      <div className="flex-1 relative flex items-center justify-center p-8 bg-[#1e1e1e] overflow-hidden">
        {/* Top Toolbar */}
        <div className="absolute top-0 left-0 right-0 h-12 flex items-center justify-center pointer-events-none">
          <div className="bg-[#2c2c2c] border border-[#3c3c3c] rounded-md px-4 py-1.5 text-[12px] text-[#a0a0a0] flex items-center gap-4 pointer-events-auto shadow-lg">
            <span>1920 x 1080</span>
            <span className="w-[1px] h-3 bg-[#444]"></span>
            <span>100%</span>
          </div>
        </div>
        
        {/* The "Device" Frame */}
        <div className="w-full h-full max-w-[1440px] bg-white rounded-lg overflow-y-auto shadow-2xl ring-1 ring-black/50">
          <StorefrontComposer layout={layout} />
        </div>
      </div>

      {/* RIGHT SIDEBAR (Properties Panel) */}
      <div className="w-[280px] bg-[#2c2c2c] border-l border-[#3c3c3c] flex flex-col z-50">
        <div className="h-10 border-b border-[#3c3c3c] flex items-center justify-between px-4">
          <span className="text-[11px] font-bold text-[#e0e0e0] uppercase tracking-widest">Design</span>
        </div>
        
        <div className="flex-1 overflow-y-auto">
          {selectedSection === "section-2" && heroData ? (
            <div className="flex flex-col">
              {/* Variant Section */}
              <div className="p-4 border-b border-[#3c3c3c]">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-semibold text-[#8a8a8a]">Component</span>
                </div>
                <select 
                  className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                  value={heroData.variant}
                  onChange={(e) => updateHeroVariant(e.target.value)}
                >
                  <option value="hero-1">Variant 1 (Banner)</option>
                  <option value="hero-2">Variant 2 (Split)</option>
                </select>
              </div>

              {/* Layout/Align Section */}
              {heroData.variant === "hero-1" && (
                <div className="p-4 border-b border-[#3c3c3c]">
                  <div className="flex items-center justify-between mb-3">
                    <span className="text-[11px] font-semibold text-[#8a8a8a]">Layout</span>
                  </div>
                  <div className="flex bg-[#1e1e1e] border border-[#3c3c3c] rounded p-0.5">
                    {['left', 'center', 'right'].map(align => (
                      <button
                        key={align}
                        onClick={() => updateHeroConfig('align', align)}
                        className={`flex-1 text-[11px] py-1 capitalize rounded-sm transition-colors ${heroData.config.align === align ? 'bg-[#3c3c3c] text-white shadow-sm' : 'text-[#8a8a8a] hover:text-white'}`}
                      >
                        {align}
                      </button>
                    ))}
                  </div>
                </div>
              )}

              {/* Typography Section */}
              <div className="p-4 border-b border-[#3c3c3c]">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-semibold text-[#8a8a8a]">Typography</span>
                </div>
                
                <div className="flex flex-col gap-3">
                  <div>
                    <label className="text-[10px] text-[#8a8a8a] mb-1 block">Title</label>
                    <input 
                      type="text" 
                      value={heroData.config.title} 
                      onChange={(e) => updateHeroConfig('title', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                  <div>
                    <label className="text-[10px] text-[#8a8a8a] mb-1 block">Description</label>
                    <textarea 
                      value={heroData.config.description} 
                      onChange={(e) => updateHeroConfig('description', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 min-h-[60px] focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                </div>
              </div>

              {/* Interaction Section */}
              <div className="p-4 border-b border-[#3c3c3c]">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-semibold text-[#8a8a8a]">Interaction (Button)</span>
                </div>
                
                <div className="flex flex-col gap-3">
                  <div>
                    <label className="text-[10px] text-[#8a8a8a] mb-1 block">Label</label>
                    <input 
                      type="text" 
                      value={heroData.config.cta.label} 
                      onChange={(e) => updateHeroCta('label', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                  <div>
                    <label className="text-[10px] text-[#8a8a8a] mb-1 block">URL (href)</label>
                    <input 
                      type="text" 
                      value={heroData.config.cta.href} 
                      onChange={(e) => updateHeroCta('href', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                </div>
              </div>

            </div>
          ) : (
            <div className="p-6 text-center text-[#8a8a8a] text-[12px]">
              Selecciona el Hero en el panel de Layers para editar sus propiedades.
            </div>
          )}
        </div>

        {/* Publish Button */}
        <div className="p-4 border-t border-[#3c3c3c] bg-[#2c2c2c]">
          <button 
            onClick={saveToDisk}
            disabled={isSaving}
            className="w-full bg-[#0d99ff] hover:bg-[#0b87e0] text-white font-medium text-[12px] py-2 rounded transition-colors disabled:opacity-50"
          >
            {isSaving ? "Publicando..." : "Publicar Cambios"}
          </button>
        </div>
      </div>

    </div>
  );
}
