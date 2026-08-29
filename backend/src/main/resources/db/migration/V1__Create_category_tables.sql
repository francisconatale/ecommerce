CREATE TABLE category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id UUID REFERENCES category(id),
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    path_names VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE category_closure (
    ancestor_id UUID REFERENCES category(id),
    descendant_id UUID REFERENCES category(id),
    depth INT NOT NULL,
    PRIMARY KEY (ancestor_id, descendant_id)
);

CREATE INDEX idx_closure_descendant ON category_closure(descendant_id);
CREATE INDEX idx_closure_depth ON category_closure(ancestor_id, depth);

CREATE TABLE product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price_buy DECIMAL(19, 4) NOT NULL,
    price_sell DECIMAL(19, 4) NOT NULL,
    category_id UUID REFERENCES category(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_product_category ON product(category_id);
