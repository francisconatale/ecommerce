import { Product } from "@/types/product";

export const siteContent = {
  "layout": [
    {
      "id": "section-nav",
      "type": "navbar",
      "config": {
        "logoUrl": "https://acdn-us.mitiendanube.com/stores/006/133/691/products/nike-logo-24edf4f26f5a40025b17503629706845-1024-1024.webp",
        "leftLinks": [
          {
            "type": "normal",
            "name": "Home",
            "href": "/"
          },
          {
            "type": "normal",
            "name": "Sobre nosotros",
            "href": "/about"
          },
          {
            "type": "interactive",
            "name": "Productos",
            "onHoverProps": {
              "component": "ProductsMenu",
              "links": [
                {
                  "name": "Zapatillas",
                  "href": "/products/v1"
                },
                {
                  "name": "Remeras",
                  "href": "/products/v2"
                },
                {
                  "name": "Buzos",
                  "href": "/products/v3"
                }
              ]
            }
          },
          {
            "type": "normal",
            "name": "New Link",
            "href": "#"
          }
        ],
        "rightLinks": [
          {
            "type": "interactive",
            "name": "Pages",
            "onHoverProps": {
              "component": "MegaMenu",
              "menuGroups": [
                {
                  "title": "Menu",
                  "links": [
                    {
                      "name": "Home",
                      "href": "/"
                    },
                    {
                      "name": "About",
                      "href": "/about"
                    },
                    {
                      "name": "Contact",
                      "href": "/contact"
                    }
                  ]
                },
                {
                  "title": "Utility Pages",
                  "links": [
                    {
                      "name": "Style guide",
                      "href": "/style-guide"
                    },
                    {
                      "name": "Licenses",
                      "href": "/licenses"
                    },
                    {
                      "name": "Changelog",
                      "href": "/changelog"
                    }
                  ]
                }
              ]
            }
          },
          {
            "type": "normal",
            "name": "Contact",
            "href": "/contact"
          }
        ],
        "mobileLinks": [
          {
            "name": "Home",
            "href": "/"
          },
          {
            "name": "About",
            "href": "/about"
          },
          {
            "name": "Products",
            "href": "/products"
          },
          {
            "name": "Contact",
            "href": "/contact"
          }
        ]
      }
    },
    {
      "id": "section-1",
      "type": "featuredProducts",
      "config": {
        "title": "Latest Looks for Your Living Space",
        "description": "Explore our latest arrivals designed to refresh your home with modern elegance and cozy functionality",
        "cta": {
          "label": "View All",
          "href": "/shop"
        },
        "products": [

          {
            "id": "1",
            "name": "Harlow Mid-Century Sideboard",
            "image": "https://cdn.prod.website-files.com/69647cc9523f892773d85bbb/699447ea10c58ba976cbd82b_69661d06edca6951ec4a8ed2_main%20image.png",
            "price": 749,
            "originalPrice": 849,
            "isOnSale": true
          },
          {
            "id": "2",
            "name": "Elowen Lounge Chair",
            "image": "https://cdn.prod.website-files.com/69647cc9523f892773d85bbb/6994455df46b656c446ba07c_69661bdf09d209fcff393f81_Image%20Main.png",
            "price": 189
          },
          {
            "id": "3",
            "name": "Finn Modular Sofa",
            "image": "https://cdn.prod.website-files.com/69647cc9523f892773d85bbb/699447f88cf883514a29f697_69661ac027aa16d7dc964ea8_Main%20Image%202.png",
            "price": 899,
            "originalPrice": 1099,
            "isOnSale": true
          },
          {
            "id": "4",
            "name": "Elan Velvet Dining Chairs",
            "image": "https://cdn.prod.website-files.com/69647cc9523f892773d85bbb/69944578f876e11062013f75_696619372eb692253e42dff3_Main%20Image.png",
            "price": 299
          },
          {
            "id": "5",
            "name": "Juno Oak Coffee Table",
            "image": "https://cdn.prod.website-files.com/69647cc9523f892773d85bbb/69944588af32c7e65328fe7a_6966173f7a0d8d27a95153fe_Image%20Main.png",
            "price": 245
          },
          {
            "id": "6",
            "name": "Arin Ceramic Table Lamp",
            "image": "https://cdn.prod.website-files.com/69647cc9523f892773d85bbb/699445a8fcf8f4effaf08410_69661607416a754b89b1bcdf_Image.png",
            "price": 72
          }
        ] as Product[]
      }
    },
    {
      "id": "section-2",
      "type": "hero",
      "variant": "hero-1",
      "config": {
        "title": "New Collections Have Arrived",
        "description": "Explore furniture designed to transform your space — now with up to 30% off",
        "image": "/hero-bg.avif",
        "align": "right",
        "cta": {
          "label": "Explore the Collection",
          "href": "/"
        }
      }
    }
  ]
};
