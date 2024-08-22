#Product
INSERT INTO permissions (name) VALUES ('Product.active');
INSERT INTO permissions (name) VALUES ('Product.delete');
INSERT INTO permissions (name) VALUES ('Product.create');
INSERT INTO permissions (name) VALUES ('Product.update');
INSERT INTO permissions (name) VALUES ('Product.all');
INSERT INTO permissions (name) VALUES ('Product.active');
INSERT INTO permissions (name) VALUES ('Product.read');

#Supplier
INSERT INTO permissions (name) VALUES ('Supplier.active');
INSERT INTO permissions (name) VALUES ('Supplier.delete');
INSERT INTO permissions (name) VALUES ('Supplier.create');
INSERT INTO permissions (name) VALUES ('Supplier.update');
INSERT INTO permissions (name) VALUES ('Supplier.all');
INSERT INTO permissions (name) VALUES ('Supplier.active');
INSERT INTO permissions (name) VALUES ('Supplier.read');

#Role
INSERT INTO permissions (name) VALUES ('Role.active');
INSERT INTO permissions (name) VALUES ('Role.delete');
INSERT INTO permissions (name) VALUES ('Role.create');
INSERT INTO permissions (name) VALUES ('Role.update');
INSERT INTO permissions (name) VALUES ('Role.all');
INSERT INTO permissions (name) VALUES ('Role.active');
INSERT INTO permissions (name) VALUES ('Role.read');

#User
INSERT INTO permissions (name) VALUES ('User.active');
INSERT INTO permissions (name) VALUES ('User.delete');
INSERT INTO permissions (name) VALUES ('User.create');
INSERT INTO permissions (name) VALUES ('User.update');
INSERT INTO permissions (name) VALUES ('User.all');
INSERT INTO permissions (name) VALUES ('User.active');
INSERT INTO permissions (name) VALUES ('User.read');

#Client
INSERT INTO permissions (name) VALUES ('Client.active');
INSERT INTO permissions (name) VALUES ('Client.delete');
INSERT INTO permissions (name) VALUES ('Client.create');
INSERT INTO permissions (name) VALUES ('Client.update');
INSERT INTO permissions (name) VALUES ('Client.all');
INSERT INTO permissions (name) VALUES ('Client.active');
INSERT INTO permissions (name) VALUES ('Client.read');

#category
INSERT INTO permissions (name) VALUES ('Category.create');
INSERT INTO permissions (name) VALUES ('Category.update');
INSERT INTO permissions (name) VALUES ('Category.delete');
INSERT INTO permissions (name) VALUES ('Category.all');
INSERT INTO permissions (name) VALUES ('Category.active');
INSERT INTO permissions (name) VALUES ('Category.read');

#ProductSale
INSERT INTO permissions (name) VALUES ('ProductSale.create');
INSERT INTO permissions (name) VALUES ('ProductSale.update');
INSERT INTO permissions (name) VALUES ('ProductSale.delete');
INSERT INTO permissions (name) VALUES ('ProductSale.all');
INSERT INTO permissions (name) VALUES ('ProductSale.active');
INSERT INTO permissions (name) VALUES ('ProductSale.read');

#ProductPurchase
INSERT INTO permissions (name) VALUES ('ProductPurchase.create');
INSERT INTO permissions (name) VALUES ('ProductPurchase.update');
INSERT INTO permissions (name) VALUES ('ProductPurchase.delete');
INSERT INTO permissions (name) VALUES ('ProductPurchase.all');
INSERT INTO permissions (name) VALUES ('ProductPurchase.active');
INSERT INTO permissions (name) VALUES ('ProductPurchase.read');



--add all permission to admin
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id 
FROM roles r
JOIN permissions p ON true
WHERE r.name = 'Admin'
ON CONFLICT DO NOTHING;




