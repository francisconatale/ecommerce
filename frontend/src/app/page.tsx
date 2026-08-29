"use client";

import { useState, useEffect } from "react";

type Category = {
  id: string;
  name: string;
  parentId: string | null;
  pathNames: string;
};

export default function Home() {
  const [name, setName] = useState("");
  const [parentId, setParentId] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [categories, setCategories] = useState<Category[]>([]);

  const fetchCategories = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/categories");
      if (response.ok) {
        const data = await response.json();
        setCategories(data);
      }
    } catch (error) {
      console.error("Error fetching categories", error);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setMessage("");

    const payload = {
      name,
      parentId: parentId.trim() === "" ? null : parentId,
    };

    try {
      const response = await fetch("http://localhost:8080/api/categories", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        setMessage("✅ Categoría creada con éxito!");
        setName("");
        setParentId("");
        fetchCategories();
      } else {
        setMessage("❌ Error al crear la categoría.");
      }
    } catch (error) {
      console.error(error);
      setMessage("❌ Error de red. ¿Está encendido el backend?");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main className="flex min-h-screen flex-col md:flex-row p-8 gap-8 bg-zinc-50 dark:bg-zinc-900">
      <div className="flex-1 max-w-md items-center font-sans text-sm">
        <h1 className="text-3xl font-bold mb-8 text-zinc-800 dark:text-zinc-100">
          ➕ Crear Categoría
        </h1>

        <form
          onSubmit={handleSubmit}
          className="bg-white dark:bg-zinc-800 p-8 rounded-2xl shadow-lg flex flex-col gap-4"
        >
          <div className="flex flex-col gap-2">
            <label className="font-semibold text-zinc-700 dark:text-zinc-300">
              Nombre de la Categoría *
            </label>
            <input
              type="text"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Ej: Electrónica"
              className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="flex flex-col gap-2">
            <label className="font-semibold text-zinc-700 dark:text-zinc-300">
              ID Padre (Opcional)
            </label>
            <input
              type="text"
              value={parentId}
              onChange={(e) => setParentId(e.target.value)}
              placeholder="UUID de la categoría superior"
              className="p-3 border rounded-lg dark:bg-zinc-700 dark:border-zinc-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="mt-4 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-bold py-3 px-4 rounded-lg transition-colors"
          >
            {isLoading ? "Creando..." : "Guardar Categoría"}
          </button>

          {message && (
            <p className="mt-4 text-center font-medium text-zinc-800 dark:text-zinc-200">
              {message}
            </p>
          )}
        </form>
      </div>

      <div className="flex-1 flex flex-col font-sans text-sm">
        <h1 className="text-3xl font-bold mb-8 text-zinc-800 dark:text-zinc-100">
          📂 Lista de Categorías
        </h1>
        <div className="bg-white dark:bg-zinc-800 p-6 rounded-2xl shadow-lg flex-1 overflow-auto">
          {categories.length === 0 ? (
            <p className="text-zinc-500 dark:text-zinc-400">No hay categorías registradas.</p>
          ) : (
            <ul className="flex flex-col gap-4">
              {categories.map((cat) => (
                <li key={cat.id} className="p-4 border border-zinc-200 dark:border-zinc-700 rounded-lg">
                  <div className="font-bold text-lg text-zinc-800 dark:text-zinc-100">{cat.name}</div>
                  <div className="text-zinc-500 dark:text-zinc-400 text-xs mt-1">ID: {cat.id}</div>
                  <div className="text-zinc-500 dark:text-zinc-400 text-xs mt-1">Path: {cat.pathNames || cat.name}</div>
                  {cat.parentId && (
                    <div className="text-blue-500 text-xs mt-1">Padre ID: {cat.parentId}</div>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </main>
  );
}