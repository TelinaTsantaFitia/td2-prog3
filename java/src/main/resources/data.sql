INSERT INTO dish (id, name, type) VALUES
(1, 'Salade fraîche', 'START'),
(2, 'Poulet grillé', 'MAIN'),
(4, 'Gâteau au chocolat', 'DESSERT');

INSERT INTO ingredient (id, name, price, category, id_dish) VALUES
(1, 'Laitue', 800.00, 'VEGETABLE', 1),
(2, 'Tomate', 600.00, 'VEGETABLE', 1),
(3, 'Poulet', 4500.00, 'ANIMAL', 2),
(4, 'Chocolat', 3000.00, 'OTHER', 4);