CREATE TABLE IF NOT EXISTS permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS users (
	id bigserial NOT NULL,
	username varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	role_id int8 NOT NULL,
	created_at timestamp DEFAULT now() NOT NULL,
	updated_at timestamp NULL,
	created_id int4 NULL,
	is_enabled bool NULL,
	deleted_at timestamp(6) NULL,
	created_by int8 NULL,
	deleted_id int8 NULL,
	updated_by int8 NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_username_key UNIQUE (username),
	CONSTRAINT fkci7xr690rvyv3bnfappbyh8x0 FOREIGN KEY (updated_by) REFERENCES public.users(id),
	CONSTRAINT fkibk1e3kaxy5sfyeekp8hbhnim FOREIGN KEY (created_by) REFERENCES public.users(id),
	CONSTRAINT fkopmdrpym60rfbpjdfnjpi5av6 FOREIGN KEY (deleted_id) REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS roles (
	id SERIAL,
	"name" varchar(255) NOT NULL,
	description text NULL,
	created_at timestamp DEFAULT now() NOT NULL,
	updated_at timestamp NULL,
	deleted_at timestamp(6) NULL,
	created_by int8 NULL,
	deleted_id int8 NULL,
	updated_by int8 NULL,
	CONSTRAINT role_name_key UNIQUE (name),
	CONSTRAINT role_pkey PRIMARY KEY (id),
    CONSTRAINT role_updated_by_fkey FOREIGN KEY (updated_by) REFERENCES public.users(id),
    CONSTRAINT role_deleted_id_fkey FOREIGN KEY (deleted_id) REFERENCES public.users(id),
    CONSTRAINT role_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS role_permissions (
    id SERIAL PRIMARY KEY,
    role_id INTEGER NOT NULL REFERENCES roles(id),
    permission_id INTEGER NOT NULL REFERENCES permissions(id),
    CONSTRAINT role_permissions_unique UNIQUE (role_id, permission_id)
);

-- Create index for product code
CREATE UNIQUE INDEX IF NOT EXISTS idx_product_code ON products(code) WHERE deleted_at IS NULL;

-- Insert admin role
INSERT INTO roles (id, name, created_at) VALUES (1, 'Admin', NOW()) ON CONFLICT (id) DO NOTHING;

-- Insert admin user
INSERT INTO public.users
(id, username, email, "password", role_id, created_at, updated_at, is_enabled, deleted_at, created_by, deleted_id, updated_by)VALUES
(1, 'Admin', 'admin@pdv.com', '$2a$10$RYkzfpuWEDYzLBdCaS6wLeg45Kp78uq6Eh8dty3W9Vx7ZM0eI/pxO', 1,  NOW(), NULL, true, NULL, 1, NULL, NULL) 
ON CONFLICT (id) DO NOTHING;


-- Product Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('Product.active', 'Listar productos activos', 'Productos'),
    ('Product.delete', 'Eliminar producto', 'Productos'),
    ('Product.create', 'Crear producto', 'Productos'),
    ('Product.update', 'Actualizar producto', 'Productos'),
    ('Product.all', 'Permisos completos de producto', 'Productos'),
    ('Product.read', 'Ver producto', 'Productos')
ON CONFLICT (name) DO NOTHING;

-- Supplier Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('Supplier.active', 'Listar proveedores activos', 'Proveedores'),
    ('Supplier.delete', 'Eliminar proveedor', 'Proveedores'),
    ('Supplier.create', 'Crear proveedor', 'Proveedores'),
    ('Supplier.update', 'Actualizar proveedor', 'Proveedores'),
    ('Supplier.all', 'Permisos completos de proveedor', 'Proveedores'),
    ('Supplier.read', 'Ver proveedor', 'Proveedores')
ON CONFLICT (name) DO NOTHING;

-- Role Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('Role.active', 'Listar roles activos', 'Roles'),
    ('Role.delete', 'Eliminar rol', 'Roles'),
    ('Role.create', 'Crear rol', 'Roles'),
    ('Role.update', 'Actualizar rol', 'Roles'),
    ('Role.all', 'Permisos completos de rol', 'Roles'),
    ('Role.read', 'Ver rol', 'Roles')
ON CONFLICT (name) DO NOTHING;

-- User Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('User.active', 'Listar usuarios activos', 'Usuarios'),
    ('User.delete', 'Eliminar usuario', 'Usuarios'),
    ('User.create', 'Crear usuario', 'Usuarios'),
    ('User.update', 'Actualizar usuario', 'Usuarios'),
    ('User.all', 'Permisos completos de usuario', 'Usuarios'),
    ('User.read', 'Ver usuario', 'Usuarios')
ON CONFLICT (name) DO NOTHING;

-- Client Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('Client.active', 'Listar clientes activos', 'Clientes'),
    ('Client.delete', 'Eliminar cliente', 'Clientes'),
    ('Client.create', 'Crear cliente', 'Clientes'),
    ('Client.update', 'Actualizar cliente', 'Clientes'),
    ('Client.all', 'Permisos completos de cliente', 'Clientes'),
    ('Client.read', 'Ver cliente', 'Clientes')
ON CONFLICT (name) DO NOTHING;

-- Category Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('Category.active', 'Listar categorías activas', 'Categorias'),
    ('Category.delete', 'Eliminar categoría', 'Categorias'),
    ('Category.create', 'Crear categoría', 'Categorias'),
    ('Category.update', 'Actualizar categoría', 'Categorias'),
    ('Category.all', 'Permisos completos de categoría', 'Categorias'),
    ('Category.read', 'Ver categoría', 'Categorias')
ON CONFLICT (name) DO NOTHING;

-- ProductSale Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('ProductSale.active', 'Listar ventas activas de productos', 'Venta de Productos'),
    ('ProductSale.delete', 'Eliminar venta de producto', 'Venta de Productos'),
    ('ProductSale.create', 'Crear venta de producto', 'Venta de Productos'),
    ('ProductSale.update', 'Actualizar venta de producto', 'Venta de Productos'),
    ('ProductSale.all', 'Permisos completos de venta de productos', 'Venta de Productos'),
    ('ProductSale.read', 'Ver venta de producto', 'Venta de Productos')
ON CONFLICT (name) DO NOTHING;

-- ProductPurchase Permissions
INSERT INTO permissions (name, description, module) 
VALUES 
    ('ProductPurchase.active', 'Listar compras activas de productos', 'Compra de Productos'),
    ('ProductPurchase.delete', 'Eliminar compra de producto', 'Compra de Productos'),
    ('ProductPurchase.create', 'Crear compra de producto', 'Compra de Productos'),
    ('ProductPurchase.update', 'Actualizar compra de producto', 'Compra de Productos'),
    ('ProductPurchase.all', 'Permisos completos de compra de productos', 'Compra de Productos'),
    ('ProductPurchase.read', 'Ver compra de producto', 'Compra de Productos')
ON CONFLICT (name) DO NOTHING;


-- Add all permissions to admin role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id 
FROM roles r
JOIN permissions p ON true
WHERE r.name = 'Admin'
ON CONFLICT (role_id, permission_id) DO NOTHING;





