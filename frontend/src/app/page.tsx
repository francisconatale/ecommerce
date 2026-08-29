"use client";

import { useState, useEffect } from "react";

type Category = {
  id: string;
  name: string;
  parentId: string | null;
  pathNames: string;
};

type Product = {
  id: string;
  name: string;
  priceBuy: number;
  priceSell: number;
  categoryId: string;
};

export default function Home() {
  const [activeTab, setActiveTab] = useState<"categories" | "products">("categories");

  const [categories, setCategories] = useState<Category[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // Form states for categories
  const [catName, setCatName] = useState("");
  const [catParentId, setCatParentId] = useState("");
  const [editingCatId, setEditingCatId] = useState<string | null>(null);

  // Form states for products
  const [prodName, setProdName] = useState("");
  const [prodPriceBuy, setProdPriceBuy] = useState("");
  const [prodPriceSell, setProdPriceSell] = useState("");
  const [prodCategoryId, setProdCategoryId] = useState("");
  const [editingProdId, setEditingProdId] = useState<string | null>(null);

  const fetchCategories = async () => {
    try {
      const response = await fetch("/api/categories");
      if (response.ok) {
        const data = await response.json();
        setCategories(data);
      }
    } catch (error) {
      console.error("Error fetching categories", error);
    }
  };

  const fetchProducts = async () => {
    try {
      const response = await fetch("/api/products");
      if (response.ok) {
        const data = await response.json();
        setProducts(data);
      }
    } catch (error) {
      console.error("Error fetching products", error);
    }
  };

  useEffect(() => {
    fetchCategories();
    fetchProducts();
  }, []);

  const showMessage = (msg: string) => {
    setMessage(msg);
    setTimeout(() => setMessage(""), 3000);
  };

  // CATEGORY HANDLERS
  const handleCategorySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    const payload = {
      name: catName,
      parentId: catParentId.trim() === "" ? null : catParentId,
    };

    try {
      let response;
      if (editingCatId) {
        response = await fetch("/api/categories/" + editingCatId, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
      } else {
        response = await fetch("/api/categories", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
      }

      if (response.ok) {
        showMessage(editingCatId ? "Categoría actualizada!" : "Categoría creada!");
        setCatName("");
        setCatParentId("");
        setEditingCatId(null);
        fetchCategories();
      } else {
        showMessage("Error al guardar categoría.");
      }
    } catch (error) {
      showMessage("Error de red.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteCategory = async (id: string) => {
    if (!confirm("¿Seguro que deseas eliminar esta categoría? Se eliminarán los enlaces con los productos.")) return;
    try {
      const response = await fetch("/api/categories/" + id, { method: "DELETE" });
      if (response.ok) {
        showMessage("Categoría eliminada.");
        fetchCategories();
        fetchProducts(); // refresh products since categoryId could be nullified
      } else {
        showMessage("Error al eliminar categoría.");
      }
    } catch (error) {
      showMessage("Error de red.");
    }
  };

  const handleEditCategory = (cat: Category) => {
    setEditingCatId(cat.id);
    setCatName(cat.name);
    setCatParentId(cat.parentId || "");
  };

  // PRODUCT HANDLERS
  const handleProductSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    const payload = {
      name: prodName,
      priceBuy: parseFloat(prodPriceBuy),
      priceSell: parseFloat(prodPriceSell),
      categoryId: prodCategoryId.trim() === "" ? null : prodCategoryId,
    };

    try {
      let response;
      if (editingProdId) {
        response = await fetch("/api/products/" + editingProdId, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
      } else {
        response = await fetch("/api/products", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
      }

      if (response.ok) {
        showMessage(editingProdId ? "Producto actualizado!" : "Producto creado!");
        setProdName("");
        setProdPriceBuy("");
        setProdPriceSell("");
        setProdCategoryId("");
        setEditingProdId(null);
        fetchProducts();
      } else {
        showMessage("Error al guardar producto.");
      }
    } catch (error) {
      showMessage("Error de red.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteProduct = async (id: string) => {
    if (!confirm("¿Seguro que deseas eliminar este producto?")) return;
    try {
      const response = await fetch("/api/products/" + id, { method: "DELETE" });
      if (response.ok) {
        showMessage("Producto eliminado.");
        fetchProducts();
      } else {
        showMessage("Error al eliminar producto.");
      }
    } catch (error) {
      showMessage("Error de red.");
    }
  };

  const handleEditProduct = (prod: Product) => {
    setEditingProdId(prod.id);
    setProdName(prod.name);
    setProdPriceBuy(prod.priceBuy.toString());
    setProdPriceSell(prod.priceSell.toString());
    setProdCategoryId(prod.categoryId || "");
  };

  const cancelEdit = () => {
    setEditingCatId(null);
    setCatName("");
    setCatParentId("");
    
    setEditingProdId(null);
    setProdName("");
    setProdPriceBuy("");
    setProdPriceSell("");
    setProdCategoryId("");
  };

  return (
    <main className="min-h-screen bg-zinc-50 dark:bg-zinc-900 text-zinc-800 dark:text-zinc-200 font-sans p-6">
      <div className="max-w-6xl mx-auto flex flex-col gap-8">
        
        {/* Header */}
        <header className="flex justify-between items-center">
          <h1 className="text-3xl font-bold">Catalog Management</h1>
          <div className="flex gap-4">
            <button 
              onClick={() => setActiveTab("categories")}
              className={"px-4 py-2 font-semibold rounded-lg " + (activeTab === "categories" ? "bg-blue-600 text-white" : "bg-zinc-200 dark:bg-zinc-800")}
            >
              Categorías
            </button>
            <button 
              onClick={() => setActiveTab("products")}
              className={"px-4 py-2 font-semibold rounded-lg " + (activeTab === "products" ? "bg-green-600 text-white" : "bg-zinc-200 dark:bg-zinc-800")}
            >
              Productos
            </button>
          </div>
        </header>

        {message && (
          <div className="bg-zinc-800 text-white p-4 rounded-lg shadow-md text-center font-medium animate-pulse">
            {message}
          </div>
        )}

        {/* Categories Tab */}
        {activeTab === "categories" && (
          <div className="flex flex-col md:flex-row gap-8">
            <div className="flex-1">
              <h2 className="text-2xl font-bold mb-4">{editingCatId ? "Editar" : "Crear"} Categoría</h2>
              <form onSubmit={handleCategorySubmit} className="bg-white dark:bg-zinc-800 p-6 rounded-2xl shadow-lg flex flex-col gap-4">
                <div className="flex flex-col gap-2">
                  <label className="font-semibold text-zinc-700 dark:text-zinc-300">Nombre *</label>
                  <input type="text" required value={catName} onChange={(e) => setCatName(e.target.value)} className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 focus:ring-2 focus:ring-blue-500" />
                </div>
                <div className="flex flex-col gap-2">
                  <label className="font-semibold text-zinc-700 dark:text-zinc-300">ID Padre (Opcional)</label>
                  <select value={catParentId} onChange={(e) => setCatParentId(e.target.value)} className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 focus:ring-2 focus:ring-blue-500">
                    <option value="">Ninguno (Raíz)</option>
                    {categories.map(c => <option key={c.id} value={c.id}>{c.pathNames || c.name}</option>)}
                  </select>
                </div>
                <div className="flex gap-2 mt-2">
                  <button type="submit" disabled={isLoading} className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg">
                    {isLoading ? "..." : (editingCatId ? "Actualizar" : "Guardar")}
                  </button>
                  {editingCatId && (
                    <button type="button" onClick={cancelEdit} className="flex-1 bg-zinc-400 hover:bg-zinc-500 text-white font-bold py-3 px-4 rounded-lg">Cancelar</button>
                  )}
                </div>
              </form>
            </div>
            <div className="flex-[2]">
              <h2 className="text-2xl font-bold mb-4">Lista de Categorías</h2>
              <div className="bg-white dark:bg-zinc-800 p-6 rounded-2xl shadow-lg h-[600px] overflow-auto">
                {categories.length === 0 ? <p>No hay categorías.</p> : (
                  <ul className="flex flex-col gap-3">
                    {categories.map((cat) => (
                      <li key={cat.id} className="p-4 border dark:border-zinc-700 rounded-lg flex justify-between items-center hover:bg-zinc-50 dark:hover:bg-zinc-700/50">
                        <div>
                          <div className="font-bold text-lg">{cat.name}</div>
                          <div className="text-xs text-zinc-500 dark:text-zinc-400 font-mono">ID: {cat.id}</div>
                          <div className="text-xs text-blue-600 dark:text-blue-400 mt-1">Ruta: {cat.pathNames || cat.name}</div>
                        </div>
                        <div className="flex gap-2">
                          <button onClick={() => handleEditCategory(cat)} className="px-3 py-1 bg-zinc-200 dark:bg-zinc-600 rounded text-sm font-semibold">Editar</button>
                          <button onClick={() => handleDeleteCategory(cat.id)} className="px-3 py-1 bg-red-100 text-red-600 dark:bg-red-900/30 dark:text-red-400 rounded text-sm font-semibold">Borrar</button>
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Products Tab */}
        {activeTab === "products" && (
          <div className="flex flex-col md:flex-row gap-8">
            <div className="flex-1">
              <h2 className="text-2xl font-bold mb-4">{editingProdId ? "Editar" : "Crear"} Producto</h2>
              <form onSubmit={handleProductSubmit} className="bg-white dark:bg-zinc-800 p-6 rounded-2xl shadow-lg flex flex-col gap-4">
                <div className="flex flex-col gap-2">
                  <label className="font-semibold text-zinc-700 dark:text-zinc-300">Nombre *</label>
                  <input type="text" required value={prodName} onChange={(e) => setProdName(e.target.value)} className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 focus:ring-2 focus:ring-green-500" />
                </div>
                <div className="flex gap-4">
                  <div className="flex flex-col gap-2 flex-1">
                    <label className="font-semibold text-zinc-700 dark:text-zinc-300">Precio Compra *</label>
                    <input type="number" step="0.01" required value={prodPriceBuy} onChange={(e) => setProdPriceBuy(e.target.value)} className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 focus:ring-2 focus:ring-green-500" />
                  </div>
                  <div className="flex flex-col gap-2 flex-1">
                    <label className="font-semibold text-zinc-700 dark:text-zinc-300">Precio Venta *</label>
                    <input type="number" step="0.01" required value={prodPriceSell} onChange={(e) => setProdPriceSell(e.target.value)} className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 focus:ring-2 focus:ring-green-500" />
                  </div>
                </div>
                <div className="flex flex-col gap-2">
                  <label className="font-semibold text-zinc-700 dark:text-zinc-300">Categoría (Opcional)</label>
                  <select value={prodCategoryId} onChange={(e) => setProdCategoryId(e.target.value)} className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 focus:ring-2 focus:ring-green-500">
                    <option value="">Ninguna</option>
                    {categories.map(c => <option key={c.id} value={c.id}>{c.pathNames || c.name}</option>)}
                  </select>
                </div>
                <div className="flex gap-2 mt-2">
                  <button type="submit" disabled={isLoading} className="flex-1 bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-4 rounded-lg">
                    {isLoading ? "..." : (editingProdId ? "Actualizar" : "Guardar")}
                  </button>
                  {editingProdId && (
                    <button type="button" onClick={cancelEdit} className="flex-1 bg-zinc-400 hover:bg-zinc-500 text-white font-bold py-3 px-4 rounded-lg">Cancelar</button>
                  )}
                </div>
              </form>
            </div>
            <div className="flex-[2]">
              <h2 className="text-2xl font-bold mb-4">Lista de Productos</h2>
              <div className="bg-white dark:bg-zinc-800 p-6 rounded-2xl shadow-lg h-[600px] overflow-auto">
                {products.length === 0 ? <p>No hay productos.</p> : (
                  <ul className="flex flex-col gap-3">
                    {products.map((prod) => (
                      <li key={prod.id} className="p-4 border dark:border-zinc-700 rounded-lg flex justify-between items-center hover:bg-zinc-50 dark:hover:bg-zinc-700/50">
                        <div>
                          <div className="font-bold text-lg">{prod.name}</div>
                          <div className="text-xs text-zinc-500 dark:text-zinc-400 font-mono">ID: {prod.id}</div>
                          <div className="text-sm text-green-600 dark:text-green-400 mt-1 font-semibold">
                            Compra:  | Venta: 
                          </div>
                          {prod.categoryId && (
                            <div className="text-xs text-blue-500 mt-1">Cat ID: {prod.categoryId}</div>
                          )}
                        </div>
                        <div className="flex gap-2">
                          <button onClick={() => handleEditProduct(prod)} className="px-3 py-1 bg-zinc-200 dark:bg-zinc-600 rounded text-sm font-semibold">Editar</button>
                          <button onClick={() => handleDeleteProduct(prod.id)} className="px-3 py-1 bg-red-100 text-red-600 dark:bg-red-900/30 dark:text-red-400 rounded text-sm font-semibold">Borrar</button>
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </main>
  );
}