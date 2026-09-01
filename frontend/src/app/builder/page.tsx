"use client";

import { useState } from "react";
import StorefrontComposer from "@/components/StorefrontComposer";
import { siteContent } from "@/config/siteContent";

export default function BuilderPage() {
  const [layout, setLayout] = useState(siteContent.layout);
  const [isSaving, setIsSaving] = useState(false);
  const [selectedSection, setSelectedSection] = useState<string | null>("section-nav");

  const selectedIndex = layout.findIndex((s) => s.id === selectedSection);
  const selectedData = selectedIndex !== -1 ? layout[selectedIndex] : null;

  const updateConfig = (key: string, value: any) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    newLayout[selectedIndex] = {
      ...newLayout[selectedIndex],
      config: { ...newLayout[selectedIndex].config, [key]: value }
    };
    setLayout(newLayout);
  };

  const updateConfigCta = (key: string, value: string) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    newLayout[selectedIndex] = {
      ...newLayout[selectedIndex],
      config: {
        ...newLayout[selectedIndex].config,
        cta: { ...newLayout[selectedIndex].config.cta, [key]: value }
      }
    };
    setLayout(newLayout);
  };

  const updateVariant = (variant: string) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    newLayout[selectedIndex] = { ...newLayout[selectedIndex], variant };
    setLayout(newLayout);
  };

  // --- MÉTODOS PARA LINKS ---
  const updateLink = (category: 'leftLinks' | 'rightLinks', index: number, field: string, value: string) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    const section = { ...newLayout[selectedIndex] };
    const config = { ...section.config };
    const newLinks = [...(config[category] || [])];
    
    newLinks[index] = { ...newLinks[index], [field]: value };
    config[category] = newLinks;
    section.config = config;
    newLayout[selectedIndex] = section;
    setLayout(newLayout);
  };

  const addLink = (category: 'leftLinks' | 'rightLinks', type: 'normal' | 'interactive') => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    const section = { ...newLayout[selectedIndex] };
    const config = { ...section.config };
    const newLinks = [...(config[category] || [])];
    
    if (type === 'normal') {
      newLinks.push({ type: 'normal', name: 'New Link', href: '#' });
    } else {
      newLinks.push({ 
        type: 'interactive', 
        name: 'New Dropdown', 
        onHoverProps: { component: 'ProductsMenu', links: [{ name: 'Item 1', href: '#' }] } 
      });
    }
    
    config[category] = newLinks;
    section.config = config;
    newLayout[selectedIndex] = section;
    setLayout(newLayout);
  };

  const removeLink = (category: 'leftLinks' | 'rightLinks', index: number) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    const section = { ...newLayout[selectedIndex] };
    const config = { ...section.config };
    const newLinks = [...(config[category] || [])];
    
    newLinks.splice(index, 1);
    config[category] = newLinks;
    section.config = config;
    newLayout[selectedIndex] = section;
    setLayout(newLayout);
  };

  const updateSubLink = (category: 'leftLinks' | 'rightLinks', index: number, subIndex: number, field: string, value: string) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    const section = { ...newLayout[selectedIndex] };
    const config = { ...section.config };
    const newLinks = [...(config[category] || [])];
    
    const updatedLink = { ...newLinks[index] };
    const onHoverProps = { ...updatedLink.onHoverProps };
    const subLinks = [...(onHoverProps.links || [])];
    
    subLinks[subIndex] = { ...subLinks[subIndex], [field]: value };
    onHoverProps.links = subLinks;
    updatedLink.onHoverProps = onHoverProps;
    
    newLinks[index] = updatedLink;
    config[category] = newLinks;
    section.config = config;
    newLayout[selectedIndex] = section;
    setLayout(newLayout);
  };

  const addSubLink = (category: 'leftLinks' | 'rightLinks', index: number) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    const section = { ...newLayout[selectedIndex] };
    const config = { ...section.config };
    const newLinks = [...(config[category] || [])];
    
    const updatedLink = { ...newLinks[index] };
    const onHoverProps = { ...updatedLink.onHoverProps };
    const subLinks = [...(onHoverProps.links || [])];
    
    subLinks.push({ name: 'New Sublink', href: '#' });
    
    onHoverProps.links = subLinks;
    updatedLink.onHoverProps = onHoverProps;
    newLinks[index] = updatedLink;
    
    config[category] = newLinks;
    section.config = config;
    newLayout[selectedIndex] = section;
    setLayout(newLayout);
  };

  const removeSubLink = (category: 'leftLinks' | 'rightLinks', index: number, subIndex: number) => {
    if (selectedIndex === -1) return;
    const newLayout = [...layout];
    const section = { ...newLayout[selectedIndex] };
    const config = { ...section.config };
    const newLinks = [...(config[category] || [])];
    
    const updatedLink = { ...newLinks[index] };
    const onHoverProps = { ...updatedLink.onHoverProps };
    const subLinks = [...(onHoverProps.links || [])];
    
    subLinks.splice(subIndex, 1);
    
    onHoverProps.links = subLinks;
    updatedLink.onHoverProps = onHoverProps;
    newLinks[index] = updatedLink;
    
    config[category] = newLinks;
    section.config = config;
    newLayout[selectedIndex] = section;
    setLayout(newLayout);
  };
  // -------------------------

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

  const renderLinkEditor = (link: any, i: number, category: 'leftLinks' | 'rightLinks') => (
    <div key={i} className="flex flex-col gap-2 p-3 bg-[#1e1e1e] border border-[#3c3c3c] rounded mb-2 relative group/link">
      <div className="flex justify-between items-center mb-1">
        <input 
          type="text" 
          value={link.name} 
          onChange={(e) => updateLink(category, i, 'name', e.target.value)}
          className="bg-transparent text-[#e0e0e0] text-[12px] focus:outline-none w-2/3 font-semibold border-b border-transparent focus:border-[#0d99ff] pb-1"
          placeholder="Nombre del Link"
        />
        <div className="flex items-center gap-2">
          <span className="text-[9px] px-1.5 py-0.5 rounded bg-[#3c3c3c] uppercase text-[#a0a0a0]">{link.type}</span>
          <button 
            onClick={() => removeLink(category, i)} 
            className="text-[#ff4d4f] hover:text-[#ff7875] text-[12px]" 
            title="Eliminar"
          >
            ✕
          </button>
        </div>
      </div>

      {link.type === 'normal' ? (
        <div>
          <label className="text-[9px] text-[#8a8a8a] mb-1 block">URL (href)</label>
          <input 
            type="text" 
            value={link.href} 
            onChange={(e) => updateLink(category, i, 'href', e.target.value)}
            className="w-full bg-[#2c2c2c] border border-[#444] rounded text-[#e0e0e0] text-[11px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
            placeholder="/"
          />
        </div>
      ) : (
        <div className="mt-1">
          <div className="text-[10px] text-[#8a8a8a] mb-2 flex items-center gap-1">
            <span className="w-2 h-2 rounded-full bg-[#0d99ff]"></span> 
            Dropdown: {link.onHoverProps?.component}
          </div>
          
          {/* Sub-links if it is a simple list like ProductsMenu */}
          {link.onHoverProps?.links && (
            <div className="pl-2 border-l border-[#444] flex flex-col gap-2 mt-2">
              {link.onHoverProps.links.map((sublink: any, subIdx: number) => (
                <div key={subIdx} className="flex gap-2 items-center">
                  <input 
                    value={sublink.name} 
                    onChange={(e) => updateSubLink(category, i, subIdx, 'name', e.target.value)}
                    className="w-[45%] bg-[#2c2c2c] border border-[#444] rounded text-[#e0e0e0] text-[10px] px-2 py-1 focus:outline-none focus:border-[#0d99ff]"
                    placeholder="Nombre"
                  />
                  <input 
                    value={sublink.href} 
                    onChange={(e) => updateSubLink(category, i, subIdx, 'href', e.target.value)}
                    className="w-[45%] bg-[#2c2c2c] border border-[#444] rounded text-[#e0e0e0] text-[10px] px-2 py-1 focus:outline-none focus:border-[#0d99ff]"
                    placeholder="URL"
                  />
                  <button 
                    onClick={() => removeSubLink(category, i, subIdx)} 
                    className="text-[#ff4d4f] hover:text-[#ff7875] text-[10px]" 
                    title="Eliminar Sublink"
                  >
                    ✕
                  </button>
                </div>
              ))}
              <button 
                onClick={() => addSubLink(category, i)} 
                className="text-[#0d99ff] text-[10px] mt-1 hover:underline text-left"
              >
                + Sublink
              </button>
            </div>
          )}
          
          {link.onHoverProps?.menuGroups && (
            <div className="text-[10px] italic text-[#666] mt-1 pl-2">
              (MegaMenu complex items are read-only in UI)
            </div>
          )}
        </div>
      )}
    </div>
  );

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
      <div className="w-[300px] bg-[#2c2c2c] border-l border-[#3c3c3c] flex flex-col z-50">
        <div className="h-10 border-b border-[#3c3c3c] flex items-center justify-between px-4">
          <span className="text-[11px] font-bold text-[#e0e0e0] uppercase tracking-widest">Design</span>
        </div>
        
        <div className="flex-1 overflow-y-auto">
          {selectedData?.type === "hero" ? (
            <div className="flex flex-col">
              {/* Variant Section */}
              <div className="p-4 border-b border-[#3c3c3c]">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-semibold text-[#8a8a8a]">Component</span>
                </div>
                <select 
                  className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                  value={selectedData.variant}
                  onChange={(e) => updateVariant(e.target.value)}
                >
                  <option value="hero-1">Variant 1 (Banner)</option>
                  <option value="hero-2">Variant 2 (Split)</option>
                </select>
              </div>

              {/* Layout/Align Section */}
              {selectedData.variant === "hero-1" && (
                <div className="p-4 border-b border-[#3c3c3c]">
                  <div className="flex items-center justify-between mb-3">
                    <span className="text-[11px] font-semibold text-[#8a8a8a]">Layout</span>
                  </div>
                  <div className="flex bg-[#1e1e1e] border border-[#3c3c3c] rounded p-0.5">
                    {['left', 'center', 'right'].map(align => (
                      <button
                        key={align}
                        onClick={() => updateConfig('align', align)}
                        className={`flex-1 text-[11px] py-1 capitalize rounded-sm transition-colors ${selectedData.config.align === align ? 'bg-[#3c3c3c] text-white shadow-sm' : 'text-[#8a8a8a] hover:text-white'}`}
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
                      value={selectedData.config.title} 
                      onChange={(e) => updateConfig('title', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                  <div>
                    <label className="text-[10px] text-[#8a8a8a] mb-1 block">Description</label>
                    <textarea 
                      value={selectedData.config.description} 
                      onChange={(e) => updateConfig('description', e.target.value)}
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
                      value={selectedData.config.cta.label} 
                      onChange={(e) => updateConfigCta('label', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                  <div>
                    <label className="text-[10px] text-[#8a8a8a] mb-1 block">URL (href)</label>
                    <input 
                      type="text" 
                      value={selectedData.config.cta.href} 
                      onChange={(e) => updateConfigCta('href', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                </div>
              </div>
            </div>
          ) : selectedData?.type === "navbar" ? (
            <div className="flex flex-col pb-6">
              {/* Logo Settings */}
              <div className="p-4 border-b border-[#3c3c3c]">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-semibold text-[#8a8a8a]">Branding</span>
                </div>
                <div className="flex flex-col gap-3">
                  <div>
                    <label className="text-[10px] text-[#8a8a8a] mb-1 block">Logo URL</label>
                    <input 
                      type="text" 
                      value={selectedData.config.logoUrl || ""} 
                      onChange={(e) => updateConfig('logoUrl', e.target.value)}
                      className="w-full bg-[#1e1e1e] border border-[#3c3c3c] rounded text-[#e0e0e0] text-[12px] px-2 py-1.5 focus:outline-none focus:border-[#0d99ff]"
                    />
                  </div>
                </div>
              </div>

              {/* Left Links Configuration */}
              <div className="p-4 border-b border-[#3c3c3c]">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-semibold text-[#8a8a8a]">Left Nav Links</span>
                </div>
                <div className="flex flex-col">
                  {selectedData.config.leftLinks?.map((link: any, i: number) => renderLinkEditor(link, i, 'leftLinks'))}
                  
                  <div className="flex gap-2 mt-2">
                    <button onClick={() => addLink('leftLinks', 'normal')} className="flex-1 bg-[#3c3c3c] text-[#e0e0e0] text-[10px] py-1.5 rounded hover:bg-[#4a4a4a] transition-colors">+ Link</button>
                    <button onClick={() => addLink('leftLinks', 'interactive')} className="flex-1 bg-[#3c3c3c] text-[#e0e0e0] text-[10px] py-1.5 rounded hover:bg-[#4a4a4a] transition-colors">+ Dropdown</button>
                  </div>
                </div>
              </div>

              {/* Right Links Configuration */}
              <div className="p-4">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-[11px] font-semibold text-[#8a8a8a]">Right Nav Links</span>
                </div>
                <div className="flex flex-col">
                  {selectedData.config.rightLinks?.map((link: any, i: number) => renderLinkEditor(link, i, 'rightLinks'))}
                  
                  <div className="flex gap-2 mt-2">
                    <button onClick={() => addLink('rightLinks', 'normal')} className="flex-1 bg-[#3c3c3c] text-[#e0e0e0] text-[10px] py-1.5 rounded hover:bg-[#4a4a4a] transition-colors">+ Link</button>
                    <button onClick={() => addLink('rightLinks', 'interactive')} className="flex-1 bg-[#3c3c3c] text-[#e0e0e0] text-[10px] py-1.5 rounded hover:bg-[#4a4a4a] transition-colors">+ Dropdown</button>
                  </div>
                </div>
              </div>

            </div>
          ) : (
            <div className="p-6 text-center text-[#8a8a8a] text-[12px]">
              Selecciona una sección en el panel de Layers para editar sus propiedades.
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
