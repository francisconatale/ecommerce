import StorefrontComposer from "@/components/StorefrontComposer";
import { siteContent } from "@/config/siteContent";

export default function Home() {
  return (
    <main>
      {/* 
        The layout array mimics a payload coming from a CMS or Database via fetch().
        StorefrontComposer dynamically resolves which components to render.
      */}
      <StorefrontComposer layout={siteContent.layout} />
    </main>
  );
}