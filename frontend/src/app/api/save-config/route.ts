import { NextResponse } from "next/server";
import fs from "fs";
import path from "path";

export async function POST(request: Request) {
  try {
    const newConfig = await request.json();
    
    // Generar el contenido del archivo TS
    const fileContent = `import { Product } from "@/types/product";

export const siteContent = ${JSON.stringify(newConfig, null, 2).replace(/"(Product\[\])"/g, "$1")};
`;

    // Sin embargo, para no romper el as de typescript ("... as Product[]"), es mejor hacerlo más limpio.
    // Vamos a reemplazar mágicamente el string si es necesario, o lo escribimos como JSON puro y arreglamos siteContent.
    
    // Mejor aún, hagamos una serialización que mantenga el as Product[] en la configuración de products
    const finalContent = fileContent.replace(
      /"products": \[([\s\S]*?)\]\n\s*\}/g, 
      `"products": [\n$1] as Product[]\n      }`
    );

    const filePath = path.join(process.cwd(), "src/config/siteContent.ts");
    
    fs.writeFileSync(filePath, finalContent, "utf8");
    
    return NextResponse.json({ success: true });
  } catch (error) {
    console.error("Error saving config:", error);
    return NextResponse.json({ error: "Failed to save configuration" }, { status: 500 });
  }
}
