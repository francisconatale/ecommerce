// Placeholder — cuando el backend soporte productos por tenancy,
// este componente los recibirá como props
export default function FeaturedProducts() {
  return (
    <div style={{ padding: '40px', background: '#f9f9f9', color: 'black' }}>
      <h2 style={{ marginBottom: '16px' }}>Productos Destacados</h2>
      <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
        {[1, 2, 3].map((i) => (
          <div
            key={i}
            style={{
              width: '180px',
              padding: '16px',
              background: '#fff',
              border: '1px solid #ddd',
              borderRadius: '8px',
              textAlign: 'center',
              color: 'black',
            }}
          >
            <div style={{ fontSize: '2rem' }}>📦</div>
            <p style={{ margin: '8px 0 0' }}>Producto {i}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
