# Category REST API Contracts

This defines the REST API endpoints exposed by the Web/Delivery layer for managing categories and querying products.

## Create Category
`POST /api/categories`

**Request Body:**
```json
{
  "name": "Software",
  "parentId": null 
}
```

**Response (201 Created):**
```json
{
  "id": "uuid-1234",
  "name": "Software",
  "parentId": null,
  "depth": 0
}
```

## Move Category
`PUT /api/categories/{id}/move`

**Request Body:**
```json
{
  "newParentId": "uuid-5678"
}
```

**Response (200 OK)**
- **400 Bad Request** if a cycle is detected or max depth (10) is exceeded.

## Delete Category
`DELETE /api/categories/{id}`

**Response (204 No Content)**
- **400 Bad Request** if attempting to delete the system default category.

## Assign Product to Category
`PUT /api/products/{productId}/category`

**Request Body:**
```json
{
  "categoryId": "uuid-9012"
}
```

**Response (200 OK)**
- **400 Bad Request** if `categoryId` is not a leaf node.

## Get Products by Category (Including Subcategories)
`GET /api/categories/{id}/products`

**Response (200 OK):**
```json
{
  "products": [
    {
      "id": "prod-1",
      "name": "IntelliJ IDEA",
      "priceBuy": 100.0,
      "priceSell": 150.0,
      "categoryId": "uuid-leaf"
    }
  ]
}
```
