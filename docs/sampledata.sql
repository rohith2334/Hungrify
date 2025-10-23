use hungrify_db;

-- users (15 rows)
INSERT INTO users (user_id, username, password, email, phone, first_name, last_name, roles, profile_image, verified, active, address, last_login, profile_json, created_at, updated_at) VALUES
(1, 'alice.w', '$2y$10$examplehash1', 'alice.w@example.com', '+911000000001', 'Alice', 'Walsh', JSON_ARRAY('ROLE_USER'), '/images/users/alice.jpg', 1, 1, '12 Park Street, Bengaluru, KA, 560001', '2025-10-20 09:12:00', JSON_OBJECT('preferred_cuisine','italian','diet','vegetarian'), '2025-01-10 08:00:00', '2025-10-20 09:20:00'),
(2, 'bob.k', '$2y$10$examplehash2', 'bob.k@example.com', '+911000000002', 'Bob', 'Kumar', JSON_ARRAY('ROLE_DELIVERY'), '/images/users/bob.jpg', 1, 1, '88 MG Road, Bengaluru, KA, 560001', '2025-10-22 18:00:00', JSON_OBJECT('preferred_payment','upi'), '2025-02-14 10:30:00', '2025-10-22 18:05:00'),
(3, 'charlie.s', '$2y$10$examplehash3', 'charlie.s@example.com', '+911000000003', 'Charlie', 'Shah', JSON_ARRAY('ROLE_USER'), '/images/users/charlie.jpg', 0, 1, '34 Church Street, Bengaluru, KA, 560002', NULL, JSON_OBJECT(), '2025-03-03 12:00:00', '2025-09-01 09:00:00'),
(4, 'diana.r', '$2y$10$examplehash4', 'diana.r@example.com', '+911000000004', 'Diana', 'Rao', JSON_ARRAY('ROLE_ADMIN'), '/images/users/diana.jpg', 1, 1, '7 Residency Road, Bengaluru, KA, 560025', '2025-10-21 20:15:00', JSON_OBJECT('diet','gluten_free'), '2025-04-01 11:00:00', '2025-10-21 20:16:00'),
(5, 'eric.m', '$2y$10$examplehash5', 'eric.m@example.com', '+911000000005', 'Eric', 'Menon', JSON_ARRAY('ROLE_USER'), '/images/users/eric.jpg', 1, 1, '90 Brigade Road, Bengaluru, KA, 560001', '2025-09-30 07:45:00', JSON_OBJECT('likes_spicy',true), '2025-05-02 08:00:00', '2025-09-30 07:50:00'),
(6, 'frank.o', '$2y$10$examplehash6', 'frank.o@example.com', '+911000000006', 'Frank', 'Oberoi', JSON_ARRAY('ROLE_RESTAURANT'), '/images/users/frank.jpg', 1, 1, '21 Indiranagar, Bengaluru, KA, 560038', '2025-10-10 13:00:00', JSON_OBJECT('preferred_cuisine','american'), '2025-06-05 09:00:00', '2025-10-10 13:05:00'),
(7, 'gina.t', '$2y$10$examplehash7', 'gina.t@example.com', '+911000000007', 'Gina', 'Thomas', JSON_ARRAY('ROLE_DELIVERY'), '/images/users/gina.jpg', 1, 1, '5 Koramangala, Bengaluru, KA, 560095', '2025-10-23 07:00:00', JSON_OBJECT('vehicle','bike'), '2025-07-12 10:00:00', '2025-10-23 07:02:00'),
(8, 'harry.p', '$2y$10$examplehash8', 'harry.p@example.com', '+911000000008', 'Harry', 'Patel', JSON_ARRAY('ROLE_USER'), '/images/users/harry.jpg', 0, 1, '14 Jayanagar, Bengaluru, KA, 560011', NULL, JSON_OBJECT(), '2025-07-20 09:00:00', '2025-08-01 09:00:00'),
(9, 'irene.v', '$2y$10$examplehash9', 'irene.v@example.com', '+911000000009', 'Irene', 'Varghese', JSON_ARRAY('ROLE_USER','ROLE_OWNER'), '/images/users/irene.jpg', 1, 1, '2 Sahakar Nagar, Bengaluru, KA, 560092', '2025-10-19 19:00:00', JSON_OBJECT('opens_late',true), '2025-08-15 15:00:00', '2025-10-19 19:05:00'),
(10, 'jack.l', '$2y$10$examplehash10', 'jack.l@example.com', '+911000000010', 'Jack', 'Lewis', JSON_ARRAY('ROLE_USER'), '/images/users/jack.jpg', 1, 1, '48 Whitefield, Bengaluru, KA, 560066', '2025-10-18 21:30:00', JSON_OBJECT('favorite','burger'), '2025-09-01 08:00:00', '2025-10-18 21:35:00'),
(11, 'kiran.s', '$2y$10$examplehash11', 'kiran.s@example.com', '+911000000011', 'Kiran', 'Singh', JSON_ARRAY('ROLE_USER'), '/images/users/kiran.jpg', 1, 1, '77 HSR Layout, Bengaluru, KA, 560102', '2025-10-22 12:00:00', JSON_OBJECT('diet','non_veg'), '2025-09-10 10:00:00', '2025-10-22 12:05:00'),
(12, 'latha.b', '$2y$10$examplehash12', 'latha.b@example.com', '+911000000012', 'Latha', 'Bhat', JSON_ARRAY('ROLE_USER'), '/images/users/latha.jpg', 1, 1, '3 Ulsoor, Bengaluru, KA, 560042', '2025-10-17 17:00:00', JSON_OBJECT('allergies',JSON_ARRAY('peanuts')), '2025-04-20 09:00:00', '2025-10-17 17:05:00'),
(13, 'mohit.r', '$2y$10$examplehash13', 'mohit.r@example.com', '+911000000013', 'Mohit', 'Reddy', JSON_ARRAY('ROLE_USER'), '/images/users/mohit.jpg', 1, 1, '11 Electronic City, Bengaluru, KA, 560100', '2025-10-20 11:00:00', JSON_OBJECT('office','Electronic City'), '2025-03-15 08:00:00', '2025-10-20 11:10:00'),
(14, 'neha.c', '$2y$10$examplehash14', 'neha.c@example.com', '+911000000014', 'Neha', 'Chopra', JSON_ARRAY('ROLE_USER'), '/images/users/neha.jpg', 1, 1, '66 Yelahanka, Bengaluru, KA, 560064', '2025-10-21 06:30:00', JSON_OBJECT('likes_salads',true), '2025-02-01 09:00:00', '2025-10-21 06:35:00'),
(15, 'oscar.j', '$2y$10$examplehash15', 'oscar.j@example.com', '+911000000015', 'Oscar', 'James', JSON_ARRAY('ROLE_USER'), '/images/users/oscar.jpg', 0, 1, '9 Kammanahalli, Bengaluru, KA, 560043', NULL, JSON_OBJECT(), '2025-01-20 09:00:00', '2025-06-01 09:00:00');


-- admin (2 rows) - admin_id must map to existing users (use users 1 and 4)
INSERT INTO admin (admin_id, username, full_name, email, phone, profile_json, created_at, updated_at) VALUES
(1, 'alice.admin', 'Alice Walsh', 'alice.admin@example.com', '+911000000001', JSON_OBJECT('permissions', JSON_ARRAY('manage_users','view_reports')), '2025-01-10 08:05:00', '2025-10-20 09:20:00'),
(4, 'diana.admin', 'Diana Rao', 'diana.admin@example.com', '+911000000004', JSON_OBJECT('permissions', JSON_ARRAY('manage_restaurants','manage_menus')), '2025-04-01 11:05:00', '2025-10-21 20:16:00');

-- restaurants (6 rows)
INSERT INTO restaurants (restaurant_id, owner_user_id, name, cuisine, address, city, state, postal_code, latitude, longitude, restaurant_meta, is_active, created_at, updated_at) VALUES
(1, 4, 'Diana''s Deli', 'Italian', '742 Market Street', 'San Francisco', 'California', '94103', 37.786350, -122.404000, JSON_OBJECT('seats', 36, 'hours', JSON_ARRAY('11:00-15:00','17:30-22:00')), 1, '2025-04-05 09:00:00', '2025-10-21 20:00:00'),
(2, 6, 'Frank''s Burgers', 'American', '1242 Sunset Boulevard', 'Los Angeles', 'California', '90026', 34.081400, -118.260700, JSON_OBJECT('seats', 24, 'delivery_partner','FastWheels'), 1, '2025-06-06 10:00:00', '2025-10-10 13:00:00'),
(3, 9, 'Irene''s Spice Corner', 'Indian', '5195 Mission Street', 'San Francisco', 'California', '94112', 37.721300, -122.438100, JSON_OBJECT('spice_level_default','medium'), 1, '2025-08-20 12:00:00', '2025-10-19 19:00:00'),
(4, 1, 'Alice''s Greens', 'Vegetarian', '1200 Grand Avenue', 'Los Angeles', 'California', '90015', 34.043800, -118.265000, JSON_OBJECT('garden_kitchen',true), 1, '2025-01-11 09:30:00', '2025-10-20 09:10:00'),
(5, 6, 'Frank''s Late Night', 'American', '1024 Garnet Avenue', 'San Diego', 'California', '92109', 32.802900, -117.241700, JSON_OBJECT('open_24_hours', false, 'closes','02:00'), 1, '2025-06-10 22:00:00', '2025-10-10 13:00:00'),
(6, 11, 'Kiran''s Kitchen', 'South Indian', '8930 Mira Mesa Blvd', 'San Diego', 'California', '92126', 32.915400, -117.138600, JSON_OBJECT('tiffin_service', true), 1, '2025-09-11 08:00:00', '2025-10-22 12:00:00');


-- ingredients (15 rows)
INSERT INTO ingredients (ingredient_id, name, display_name, allergens, meta, created_at) VALUES
(1, 'tomato', 'Tomato', JSON_ARRAY(), JSON_OBJECT('type','vegetable'), '2025-01-05 09:00:00'),
(2, 'mozzarella', 'Mozzarella Cheese', JSON_ARRAY('milk'), JSON_OBJECT('origin','buffalo'), '2025-01-06 09:00:00'),
(3, 'basil', 'Fresh Basil', JSON_ARRAY(), JSON_OBJECT('use','garnish'), '2025-01-07 09:00:00'),
(4, 'wheat_flour', 'Wheat Flour', JSON_ARRAY('wheat'), JSON_OBJECT('gluten', true), '2025-01-08 09:00:00'),
(5, 'chicken', 'Chicken', JSON_ARRAY(), JSON_OBJECT('type','poultry'), '2025-01-09 09:00:00'),
(6, 'garlic', 'Garlic', JSON_ARRAY(), JSON_OBJECT(), '2025-01-10 09:00:00'),
(7, 'peanuts', 'Peanuts', JSON_ARRAY('peanuts'), JSON_OBJECT('roasted', true), '2025-01-11 09:00:00'),
(8, 'soy_sauce', 'Soy Sauce', JSON_ARRAY('soy'), JSON_OBJECT('contains','soy'), '2025-01-12 09:00:00'),
(9, 'egg', 'Egg', JSON_ARRAY('eggs'), JSON_OBJECT('type','chicken'), '2025-01-13 09:00:00'),
(10, 'fish_sauce', 'Fish Sauce', JSON_ARRAY('fish'), JSON_OBJECT('origin','thai'), '2025-01-14 09:00:00'),
(11, 'shrimp', 'Shrimp', JSON_ARRAY('shellfish'), JSON_OBJECT('frozen', false), '2025-01-15 09:00:00'),
(12, 'lettuce', 'Lettuce', JSON_ARRAY(), JSON_OBJECT('variety','romaine'), '2025-01-16 09:00:00'),
(13, 'beef_patty', 'Beef Patty', JSON_ARRAY(), JSON_OBJECT('cooked','medium'), '2025-01-17 09:00:00'),
(14, 'salt', 'Salt', JSON_ARRAY(), JSON_OBJECT('iodised', true), '2025-01-18 09:00:00'),
(15, 'peanut_oil', 'Peanut Oil', JSON_ARRAY('peanuts'), JSON_OBJECT('refined', true), '2025-01-19 09:00:00');


-- food_items (20 rows)
INSERT INTO food_items (item_id, restaurant_id, canonical_name, display_name, short_description, long_description, price, quantity, is_available, prep_time_minutes, image_urls, rating, created_at, updated_at) VALUES
(1, 1, 'margherita_pizza', 'Margherita Pizza', 'Classic margherita with fresh basil and mozzarella', 'Stone-baked pizza with tomato sauce, fresh mozzarella and basil.', 59.00, 1, 1, 18, JSON_ARRAY('/images/items/margherita1.jpg'), 4.50, '2025-04-06 10:00:00', '2025-10-21 20:00:00'),
(2, 1, 'pasta_arrabbiata', 'Pasta Arrabbiata', 'Penne in spicy tomato sauce', 'Penne pasta tossed in a spicy garlic-tomato sauce with basil.', 34.00, 1, 1, 12, JSON_ARRAY('/images/items/arrabbiata.jpg'), 4.20, '2025-04-06 10:05:00', '2025-10-21 20:00:00'),
(3, 2, 'classic_cheeseburger', 'Classic Cheeseburger', 'Beef burger with cheddar', 'Grilled beef patty, cheddar cheese, lettuce, tomato, and house sauce.', 39.00, 1, 1, 15, JSON_ARRAY('/images/items/cheeseburger.jpg'), 4.30, '2025-06-06 11:00:00', '2025-10-10 13:00:00'),
(4, 2, 'fries_large', 'Large Fries', 'Crispy golden fries', 'Double-fried potatoes served with ketchup.', 19.00, 1, 1, 6, JSON_ARRAY('/images/items/fries.jpg'), 4.00, '2025-06-06 11:05:00', '2025-10-10 13:00:00'),
(5, 3, 'butter_chicken', 'Butter Chicken', 'Creamy tomato based butter chicken', 'Tender chicken cooked in a rich tomato and butter sauce.', 49.00, 1, 1, 25, JSON_ARRAY('/images/items/butter_chicken.jpg'), 4.60, '2025-08-21 12:00:00', '2025-10-19 19:00:00'),
(6, 3, 'spicy_shrimp', 'Spicy Shrimp Curry', 'Prawns in spicy coconut gravy', 'Fresh shrimp simmered in spicy coconut curry.', 549.00, 1, 1, 20, JSON_ARRAY('/images/items/spicy_shrimp.jpg'), 4.25, '2025-08-21 12:10:00', '2025-10-19 19:00:00'),
(7, 4, 'quinoa_salad', 'Quinoa Salad', 'Protein-packed quinoa salad', 'Quinoa, roasted vegetables, lettuce and lemon dressing.', 29.00, 1, 1, 10, JSON_ARRAY('/images/items/quinoa_salad.jpg'), 4.10, '2025-01-12 09:40:00', '2025-10-20 09:10:00'),
(8, 4, 'paneer_wrap', 'Paneer Wrap', 'Grilled paneer and veggies wrap', 'Soft flatbread filled with spiced paneer and salad.', 69.00, 1, 1, 8, JSON_ARRAY('/images/items/paneer_wrap.jpg'), 4.05, '2025-01-12 09:45:00', '2025-10-20 09:10:00'),
(9, 5, 'late_night_burger', 'Late Night Burger', 'Midnight special beef burger', 'Hearty burger with double patties and special sauce.', 449.00, 1, 1, 18, JSON_ARRAY('/images/items/late_burger.jpg'), 4.35, '2025-06-11 23:00:00', '2025-10-10 23:50:00'),
(10, 5, 'chicken_wings', 'Chicken Wings (6 pcs)', 'Spicy glazed wings', 'Six pieces of spicy glazed chicken wings.', 32.00, 1, 1, 14, JSON_ARRAY('/images/items/wings.jpg'), 4.00, '2025-06-11 23:05:00', '2025-10-10 23:50:00'),
(11, 6, 'idli_sambar', 'Idli & Sambar (2 pcs)', 'Soft steamed idlis with sambar', 'Traditional South Indian breakfast with chutney.', 19.00, 1, 1, 8, JSON_ARRAY('/images/items/idli.jpg'), 4.40, '2025-09-12 08:00:00', '2025-10-22 12:00:00'),
(12, 6, 'masala_dosa', 'Masala Dosa', 'Crispy dosa with potato masala', 'Large dosa served with chutney and sambar.', 19.00, 1, 1, 12, JSON_ARRAY('/images/items/dosa.jpg'), 4.50, '2025-09-12 08:05:00', '2025-10-22 12:00:00'),
(13, 1, 'bruschetta', 'Tomato Bruschetta', 'Grilled bread with tomato and basil', 'Toasted bread topped with marinated tomatoes and basil.', 19.00, 1, 1, 6, JSON_ARRAY('/images/items/bruschetta.jpg'), 4.15, '2025-04-06 10:10:00', '2025-10-21 20:00:00'),
(14, 3, 'naan', 'Butter Naan', 'Soft buttered naan', 'Freshly baked naan with butter.', 49.00, 1, 1, 6, JSON_ARRAY('/images/items/naan.jpg'), 4.00, '2025-08-21 12:15:00', '2025-10-19 19:00:00'),
(15, 2, 'veggie_burger', 'Veggie Burger', 'Grilled vegetable patty', 'House-made veggie patty with lettuce and tomato.', 32.00, 1, 1, 15, JSON_ARRAY('/images/items/veggie_burger.jpg'), 4.05, '2025-06-06 11:10:00', '2025-10-10 13:00:00'),
(16, 4, 'green_smoothie', 'Green Smoothie', 'Spinach & apple smoothie', 'Fresh spinach blended with apple and banana.', 17.00, 1, 1, 5, JSON_ARRAY('/images/items/smoothie.jpg'), 4.20, '2025-01-12 09:50:00', '2025-10-20 09:10:00'),
(17, 3, 'garlic_naan', 'Garlic Naan', 'Naan with garlic butter', 'Garlic butter topped naan.', 59.00, 1, 1, 6, JSON_ARRAY('/images/items/garlic_naan.jpg'), 4.10, '2025-08-21 12:20:00', '2025-10-19 19:00:00'),
(18, 5, 'midnight_fries', 'Midnight Fries', 'Seasoned fries for night owls', 'Large seasoned fries with garlic powder.', 14.00, 1, 1, 7, JSON_ARRAY('/images/items/midnight_fries.jpg'), 3.95, '2025-06-11 23:10:00', '2025-10-10 23:50:00'),
(19, 6, 'rava_upma', 'Rava Upma', 'Semolina upma with vegetables', 'Comforting upma with peas and carrots.', 11.00, 1, 1, 10, JSON_ARRAY('/images/items/upma.jpg'), 4.00, '2025-09-12 08:10:00', '2025-10-22 12:00:00'),
(20, 2, 'chicken_combo', 'Chicken Combo Meal', 'Burger + Fries + Drink', 'Combo pack with classic burger, fries and soda.', 59.00, 1, 1, 20, JSON_ARRAY('/images/items/chicken_combo.jpg'), 4.30, '2025-06-06 11:15:00', '2025-10-10 13:00:00');


-- food_item_profiles (20 rows) - one-to-one mapping to food_items
INSERT INTO food_item_profiles (profile_id, item_id, category_code, category_name, calories_kcal, carbs_g, protein_g, fats_g, spice_score, spice_level, allergens, tags, taste_profile, created_at, updated_at) VALUES
(1, 1, 'pizza', 'Pizza', 850, 95.00, 32.00, 30.00, 1, 'mild', JSON_ARRAY('milk','wheat'), JSON_ARRAY('vegetarian','classic'), JSON_OBJECT('sweet',1,'savory',4), '2025-04-06 10:01:00', '2025-10-21 20:00:00'),
(2, 2, 'pasta', 'Pasta', 620, 90.00, 18.00, 22.00, 3, 'medium', JSON_ARRAY('wheat'), JSON_ARRAY('spicy','comfort'), JSON_OBJECT('spicy',3,'umami',3), '2025-04-06 10:06:00', '2025-10-21 20:00:00'),
(3, 3, 'burger', 'Burger', 780, 60.00, 40.00, 45.00, 2, 'mild', JSON_ARRAY(), JSON_ARRAY('meat','classic'), JSON_OBJECT('savory',5,'fatty',4), '2025-06-06 11:01:00', '2025-10-10 13:00:00'),
(4, 4, 'sides', 'Sides', 350, 40.00, 4.00, 18.00, 1, 'none', JSON_ARRAY('wheat'), JSON_ARRAY('snack'), JSON_OBJECT('salty',4), '2025-06-06 11:06:00', '2025-10-10 13:00:00'),
(5, 5, 'curry', 'Curry', 700, 30.00, 35.00, 40.00, 2, 'medium', JSON_ARRAY('milk'), JSON_ARRAY('rich','popular'), JSON_OBJECT('creamy',5,'spicy',3), '2025-08-21 12:01:00', '2025-10-19 19:00:00'),
(6, 6, 'seafood', 'Seafood', 520, 20.00, 30.00, 25.00, 4, 'spicy', JSON_ARRAY('shellfish'), JSON_ARRAY('seafood','spicy'), JSON_OBJECT('spicy',4,'umami',4), '2025-08-21 12:11:00', '2025-10-19 19:00:00'),
(7, 7, 'salad', 'Salad', 420, 45.00, 12.00, 14.00, 1, 'none', JSON_ARRAY(), JSON_ARRAY('healthy','vegan'), JSON_OBJECT('fresh',5,'tangy',3), '2025-01-12 09:41:00', '2025-10-20 09:10:00'),
(8, 8, 'wrap', 'Wrap', 480, 50.00, 18.00, 20.00, 2, 'mild', JSON_ARRAY('milk','wheat'), JSON_ARRAY('vegetarian'), JSON_OBJECT('savory',3,'fresh',3), '2025-01-12 09:46:00', '2025-10-20 09:10:00'),
(9, 9, 'burger', 'Late Night Burger', 900, 70.00, 45.00, 48.00, 3, 'medium', JSON_ARRAY(), JSON_ARRAY('late_night','hearty'), JSON_OBJECT('savory',5,'spicy',3), '2025-06-11 23:01:00', '2025-10-10 23:50:00'),
(10, 10, 'starter', 'Starter', 560, 20.00, 28.00, 35.00, 3, 'medium', JSON_ARRAY(), JSON_ARRAY('spicy'), JSON_OBJECT('spicy',4,'savory',4), '2025-06-11 23:06:00', '2025-10-10 23:50:00'),
(11, 11, 'breakfast', 'Breakfast', 250, 30.00, 8.00, 6.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('light','traditional'), JSON_OBJECT('savory',3,'comfort',4), '2025-09-12 08:01:00', '2025-10-22 12:00:00'),
(12, 12, 'breakfast', 'Breakfast', 450, 60.00, 10.00, 12.00, 2, 'mild', JSON_ARRAY('wheat'), JSON_ARRAY('classic'), JSON_OBJECT('crispy',4,'savory',3), '2025-09-12 08:06:00', '2025-10-22 12:00:00'),
(13, 13, 'starter', 'Starter', 180, 20.00, 4.00, 8.00, 0, 'none', JSON_ARRAY('wheat'), JSON_ARRAY('appetizer'), JSON_OBJECT('fresh',4), '2025-04-06 10:11:00', '2025-10-21 20:00:00'),
(14, 14, 'bread', 'Bread', 220, 30.00, 5.00, 8.00, 0, 'none', JSON_ARRAY('wheat'), JSON_ARRAY('side'), JSON_OBJECT('buttery',4), '2025-08-21 12:16:00', '2025-10-19 19:00:00'),
(15, 15, 'burger', 'Veggie Burger', 630, 55.00, 18.00, 22.00, 1, 'mild', JSON_ARRAY('wheat'), JSON_ARRAY('vegetarian','grilled'), JSON_OBJECT('savory',4,'fresh',3), '2025-06-06 11:11:00', '2025-10-10 13:00:00'),
(16, 16, 'smoothie', 'Smoothie', 220, 40.00, 4.00, 3.00, 0, 'none', JSON_ARRAY('milk'), JSON_ARRAY('beverage','healthy'), JSON_OBJECT('fresh',5,'sweet',3), '2025-01-12 09:51:00', '2025-10-20 09:10:00'),
(17, 17, 'garlic_naan', 'Garlic Naan', 260, 35.00, 6.00, 10.00, 1, 'none', JSON_ARRAY('wheat','milk'), JSON_ARRAY('bread','garlicy'), JSON_OBJECT('buttery',4,'garlic',5), '2025-08-21 12:21:00', '2025-10-19 19:00:00'),
(18, 18, 'midnight_fries', 'Midnight Fries', 410, 45.00, 5.00, 20.00, 2, 'mild', JSON_ARRAY(), JSON_ARRAY('late_night','snack'), JSON_OBJECT('salty',5), '2025-06-11 23:11:00', '2025-10-10 23:50:00'),
(19, 19, 'rava_upma', 'Rava Upma', 330, 45.00, 8.00, 10.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('traditional','light'), JSON_OBJECT('savory',4), '2025-09-12 08:11:00', '2025-10-22 12:00:00'),
(20, 20, 'chicken_combo', 'Chicken Combo Meal', 1100, 95.00, 55.00, 45.00, 2, 'medium', JSON_ARRAY(), JSON_ARRAY('combo','value'), JSON_OBJECT('savory',5,'filling',5), '2025-06-06 11:16:00', '2025-10-10 13:00:00');


-- food_item_ingredients (40 rows) - junctions linking items to ingredients
INSERT INTO food_item_ingredients (fi_id, item_id, ingredient_id, removable, removal_effects, created_at) VALUES
(1, 1, 1, FALSE, JSON_OBJECT(), '2025-04-06 10:02:00'),
(2, 1, 2, FALSE, JSON_OBJECT('taste','less_creamy'), '2025-04-06 10:02:00'),
(3, 1, 3, TRUE, JSON_OBJECT('taste','less_herbal'), '2025-04-06 10:02:00'),
(4, 1, 4, TRUE, JSON_OBJECT('texture','less_crusty'), '2025-04-06 10:02:00'),
(5, 2, 1, FALSE, JSON_OBJECT(), '2025-04-06 10:07:00'),
(6, 2, 6, FALSE, JSON_OBJECT(), '2025-04-06 10:07:00'),
(7, 3, 13, FALSE, JSON_OBJECT(), '2025-06-06 11:02:00'),
(8, 3, 12, TRUE, JSON_OBJECT('freshness','reduced'), '2025-06-06 11:02:00'),
(9, 4, 14, FALSE, JSON_OBJECT(), '2025-06-06 11:07:00'),
(10, 5, 5, FALSE, JSON_OBJECT(), '2025-08-21 12:02:00'),
(11, 5, 2, FALSE, JSON_OBJECT('taste','creamier'), '2025-08-21 12:02:00'),
(12, 6, 11, FALSE, JSON_OBJECT(), '2025-08-21 12:12:00'),
(13, 6, 10, FALSE, JSON_OBJECT('umami','increased'), '2025-08-21 12:12:00'),
(14, 7, 12, FALSE, JSON_OBJECT(), '2025-01-12 09:42:00'),
(15, 7, 1, TRUE, JSON_OBJECT('acidity','reduced'), '2025-01-12 09:42:00'),
(16, 8, 2, TRUE, JSON_OBJECT('cheese','removed'), '2025-01-12 09:47:00'),
(17, 8, 12, FALSE, JSON_OBJECT(), '2025-01-12 09:47:00'),
(18, 9, 13, FALSE, JSON_OBJECT(), '2025-06-11 23:02:00'),
(19, 9, 4, TRUE, JSON_OBJECT('bun','swap_gluten_free'), '2025-06-11 23:02:00'),
(20, 10, 5, FALSE, JSON_OBJECT(), '2025-06-11 23:07:00'),
(21, 10, 14, TRUE, JSON_OBJECT('salt','reduced'), '2025-06-11 23:07:00'),
(22, 11, 12, FALSE, JSON_OBJECT(), '2025-09-12 08:02:00'),
(23, 11, 6, TRUE, JSON_OBJECT('flavor','milder'), '2025-09-12 08:02:00'),
(24, 12, 4, FALSE, JSON_OBJECT(), '2025-09-12 08:07:00'),
(25, 12, 9, TRUE, JSON_OBJECT('egg','removed'), '2025-09-12 08:07:00'),
(26, 13, 1, FALSE, JSON_OBJECT(), '2025-04-06 10:12:00'),
(27, 14, 4, FALSE, JSON_OBJECT(), '2025-08-21 12:17:00'),
(28, 15, 12, FALSE, JSON_OBJECT(), '2025-06-06 11:12:00'),
(29, 15, 4, TRUE, JSON_OBJECT('bun','gluten_free_option'), '2025-06-06 11:12:00'),
(30, 16, 12, FALSE, JSON_OBJECT(), '2025-01-12 09:52:00'),
(31, 16, 1, TRUE, JSON_OBJECT('tomato','optional'), '2025-01-12 09:52:00'),
(32, 17, 4, FALSE, JSON_OBJECT(), '2025-08-21 12:22:00'),
(33, 17, 6, TRUE, JSON_OBJECT('garlic','remove_for_mild'), '2025-08-21 12:22:00'),
(34, 18, 14, FALSE, JSON_OBJECT(), '2025-06-11 23:12:00'),
(35, 18, 6, TRUE, JSON_OBJECT('garlic','remove_for_less_flavor'), '2025-06-11 23:12:00'),
(36, 19, 4, FALSE, JSON_OBJECT(), '2025-09-12 08:12:00'),
(37, 19, 6, TRUE, JSON_OBJECT('garlic','optional'), '2025-09-12 08:12:00'),
(38, 20, 13, FALSE, JSON_OBJECT(), '2025-06-06 11:17:00'),
(39, 20, 4, TRUE, JSON_OBJECT('bun','swap'), '2025-06-06 11:17:00'),
(40, 20, 3, TRUE, JSON_OBJECT('basil','optional'), '2025-06-06 11:17:00');

-- orders (12 rows)
INSERT INTO orders (order_id, user_id, restaurant_id, status, total_amount, payment_method, payment_status, payment_transaction_ref, payment_meta, paid_at, delivery_address, delivery_lat, delivery_lon, order_meta, created_at, updated_at) VALUES
(1001, 1, 1, 'delivered', 898.00, 'upi', 'paid', 'TXN1001', JSON_OBJECT('gateway','UPI'), '2025-10-18 12:30:00', '742 Market Street, San Francisco, CA, 94103', 37.786350, -122.404000, JSON_OBJECT('notes','leave_at_door'), '2025-10-18 12:00:00', '2025-10-18 13:05:00'),
(1002, 2, 2, 'out_for_delivery', 528.00, 'wallet', 'paid', 'TXN1002', JSON_OBJECT('wallet','FastPay'), '2025-10-22 18:10:00', '1242 Sunset Boulevard, Los Angeles, CA, 90026', 34.081400, -118.260700, JSON_OBJECT(), '2025-10-22 17:50:00', '2025-10-22 18:12:00'),
(1003, 3, 3, 'preparing', 648.00, 'credit_card', 'pending', 'TXN1003', JSON_OBJECT('card_last4','4242'), NULL, '5195 Mission Street, San Francisco, CA, 94112', 37.721300, -122.438100, JSON_OBJECT('scheduled',false), '2025-10-23 11:00:00', '2025-10-23 11:05:00'),
(1004, 5, 4, 'confirmed', 478.00, 'upi', 'paid', 'TXN1004', JSON_OBJECT(), '2025-10-19 13:00:00', '1200 Grand Avenue, Los Angeles, CA, 90015', 34.043800, -118.265000, JSON_OBJECT('promo_code','SAVE50'), '2025-10-19 12:45:00', '2025-10-19 13:02:00'),
(1005, 6, 2, 'delivered', 728.00, 'debit_card', 'paid', 'TXN1005', JSON_OBJECT(), '2025-10-10 23:30:00', '801 N Alvarado Street, Los Angeles, CA, 90026', 34.078900, -118.262700, JSON_OBJECT(), '2025-10-10 23:00:00', '2025-10-10 23:55:00'),
(1006, 7, 5, 'cancelled', 598.00, 'cod', 'pending', NULL, JSON_OBJECT(), NULL, '1024 Garnet Avenue, San Diego, CA, 92109', 32.802900, -117.241700, JSON_OBJECT(), '2025-10-23 07:10:00', '2025-10-23 07:11:00'),
(1007, 8, 6, 'preparing', 268.00, 'upi', 'paid', 'TXN1007', JSON_OBJECT(), '2025-10-22 08:45:00', '8930 Mira Mesa Blvd, San Diego, CA, 92126', 32.915400, -117.138600, JSON_OBJECT('instructions','less_spicy'), '2025-10-22 08:30:00', '2025-10-22 08:50:00'),
(1008, 9, 3, 'delivered', 548.00, 'upi', 'paid', 'TXN1008', JSON_OBJECT(), '2025-10-19 20:00:00', '475 Geneva Avenue, San Francisco, CA, 94112', 37.718000, -122.444000, JSON_OBJECT(), '2025-10-19 19:20:00', '2025-10-19 20:10:00'),
(1009, 10, 2, 'cancelled', 0.00, 'upi', 'refunded', 'TXN1009', JSON_OBJECT('reason','user_cancelled'), NULL, '1620 Echo Park Avenue, Los Angeles, CA, 90026', 34.082800, -118.250000, JSON_OBJECT(), '2025-10-18 21:00:00', '2025-10-18 21:15:00'),
(1010, 11, 6, 'out_for_delivery', 268.00, 'wallet', 'paid', 'TXN1010', JSON_OBJECT(), '2025-10-22 12:30:00', '7750 Arjons Drive, San Diego, CA, 92126', 32.898500, -117.149200, JSON_OBJECT(), '2025-10-22 12:10:00', '2025-10-22 12:35:00'),
(1011, 12, 4, 'confirmed', 448.00, 'upi', 'pending', NULL, JSON_OBJECT(), NULL, '1425 W Olympic Blvd, Los Angeles, CA, 90015', 34.045000, -118.266000, JSON_OBJECT('delivery_window','18:00-19:00'), '2025-10-17 16:50:00', '2025-10-17 16:55:00'),
(1012, 13, 1, 'preparing', 798.00, 'credit_card', 'paid', 'TXN1012', JSON_OBJECT('card_last4','1111'), '2025-10-20 11:30:00', '88 5th Street, San Francisco, CA, 94103', 37.782900, -122.406700, JSON_OBJECT(), '2025-10-20 11:10:00', '2025-10-20 11:35:00');


-- order_items (at least 12 orders, multiple items each -> create 28 rows)
INSERT INTO order_items (order_item_id, order_id, item_id, quantity, unit_price, customization_selected, item_snapshot, created_at) VALUES
(5001, 1001, 1, 1, 599.00, JSON_ARRAY(JSON_OBJECT('extra','basil')), JSON_OBJECT('item_id',1,'display_name','Margherita Pizza','price',599.00), '2025-10-18 12:01:00'),
(5002, 1001, 13, 1, 199.00, JSON_ARRAY(), JSON_OBJECT('item_id',13,'display_name','Tomato Bruschetta','price',199.00), '2025-10-18 12:02:00'),
(5003, 1002, 3, 1, 399.00, JSON_ARRAY(JSON_OBJECT('doneness','medium')), JSON_OBJECT('item_id',3,'display_name','Classic Cheeseburger','price',399.00), '2025-10-22 17:52:00'),
(5004, 1002, 4, 1, 129.00, JSON_ARRAY(), JSON_OBJECT('item_id',4,'display_name','Large Fries','price',129.00), '2025-10-22 17:53:00'),
(5005, 1003, 5, 1, 499.00, JSON_ARRAY(JSON_OBJECT('spice','medium')), JSON_OBJECT('item_id',5,'display_name','Butter Chicken','price',499.00), '2025-10-23 11:01:00'),
(5006, 1003, 14, 1, 49.00, JSON_ARRAY(), JSON_OBJECT('item_id',14,'display_name','Butter Naan','price',49.00), '2025-10-23 11:02:00'),
(5007, 1004, 7, 1, 299.00, JSON_ARRAY(JSON_OBJECT('dressing','lemon')), JSON_OBJECT('item_id',7,'display_name','Quinoa Salad','price',299.00), '2025-10-19 12:46:00'),
(5008, 1004, 16, 1, 179.00, JSON_ARRAY(), JSON_OBJECT('item_id',16,'display_name','Green Smoothie','price',179.00), '2025-10-19 12:47:00'),
(5009, 1005, 9, 1, 449.00, JSON_ARRAY(JSON_OBJECT('extra_cheese',true)), JSON_OBJECT('item_id',9,'display_name','Late Night Burger','price',449.00), '2025-10-10 23:02:00'),
(5010, 1005, 18, 1, 149.00, JSON_ARRAY(), JSON_OBJECT('item_id',18,'display_name','Midnight Fries','price',149.00), '2025-10-10 23:03:00'),
(5011, 1006, 20, 1, 599.00, JSON_ARRAY(), JSON_OBJECT('item_id',20,'display_name','Chicken Combo Meal','price',599.00), '2025-10-23 07:12:00'),
(5012, 1006, 4, 1, 129.00, JSON_ARRAY(), JSON_OBJECT('item_id',4,'display_name','Large Fries','price',129.00), '2025-10-23 07:13:00'),
(5013, 1007, 11, 2, 119.00, JSON_ARRAY(), JSON_OBJECT('item_id',11,'display_name','Idli & Sambar','price',119.00), '2025-10-22 08:31:00'),
(5014, 1008, 6, 1, 549.00, JSON_ARRAY(), JSON_OBJECT('item_id',6,'display_name','Spicy Shrimp Curry','price',549.00), '2025-10-19 19:25:00'),
(5015, 1008, 14, 1, 49.00, JSON_ARRAY(), JSON_OBJECT('item_id',14,'display_name','Butter Naan','price',49.00), '2025-10-19 19:26:00'),
(5016, 1010, 11, 1, 119.00, JSON_ARRAY(), JSON_OBJECT('item_id',11,'display_name','Idli & Sambar','price',119.00), '2025-10-22 12:11:00'),
(5017, 1010, 19, 1, 119.00, JSON_ARRAY(), JSON_OBJECT('item_id',19,'display_name','Rava Upma','price',119.00), '2025-10-22 12:12:00'),
(5018, 1009, 3, 1, 399.00, JSON_ARRAY(), JSON_OBJECT('item_id',3,'display_name','Classic Cheeseburger','price',399.00), '2025-10-18 21:02:00'),
(5019, 1011, 8, 1, 269.00, JSON_ARRAY(), JSON_OBJECT('item_id',8,'display_name','Paneer Wrap','price',269.00), '2025-10-17 16:51:00'),
(5020, 1011, 16, 1, 179.00, JSON_ARRAY(), JSON_OBJECT('item_id',16,'display_name','Green Smoothie','price',179.00), '2025-10-17 16:52:00'),
(5021, 1012, 1, 1, 599.00, JSON_ARRAY(JSON_OBJECT('extra','cheese')), JSON_OBJECT('item_id',1,'display_name','Margherita Pizza','price',599.00), '2025-10-20 11:11:00'),
(5022, 1012, 13, 1, 199.00, JSON_ARRAY(), JSON_OBJECT('item_id',13,'display_name','Tomato Bruschetta','price',199.00), '2025-10-20 11:12:00'),
(5023, 1004, 8, 1, 269.00, JSON_ARRAY(), JSON_OBJECT('item_id',8,'display_name','Paneer Wrap','price',269.00), '2025-10-19 12:48:00'),
(5024, 1002, 20, 1, 599.00, JSON_ARRAY(), JSON_OBJECT('item_id',20,'display_name','Chicken Combo Meal','price',599.00), '2025-10-22 17:54:00'),
(5025, 1001, 7, 1, 299.00, JSON_ARRAY(), JSON_OBJECT('item_id',7,'display_name','Quinoa Salad','price',299.00), '2025-10-18 12:03:00'),
(5026, 1012, 2, 1, 349.00, JSON_ARRAY(), JSON_OBJECT('item_id',2,'display_name','Pasta Arrabbiata','price',349.00), '2025-10-20 11:13:00'),
(5027, 1005, 15, 1, 329.00, JSON_ARRAY(), JSON_OBJECT('item_id',15,'display_name','Veggie Burger','price',329.00), '2025-10-10 23:04:00'),
(5028, 1003, 17, 1, 59.00, JSON_ARRAY(), JSON_OBJECT('item_id',17,'display_name','Garlic Naan','price',59.00), '2025-10-23 11:03:00');


-- deliveries (12 rows) - one per order; partner_user_id uses driver users 2,7 and some NULLs
INSERT INTO deliveries (delivery_id, order_id, partner_user_id, partner_vehicle_type, status, estimated_time_minutes, actual_delivery_time, delivery_meta, created_at) VALUES
(9001, 1001, 2, 'bike', 'delivered', 25, '2025-10-18 13:05:00', JSON_OBJECT('note','contactless'), '2025-10-18 12:05:00'),
(9002, 1002, 7, 'bike', 'picked_up', 15, NULL, JSON_OBJECT('eta','15_min'), '2025-10-22 18:00:00'),
(9003, 1003, NULL, NULL, 'assigned', 20, NULL, JSON_OBJECT(), '2025-10-23 11:02:00'),
(9004, 1004, 2, 'car', 'assigned', 30, NULL, JSON_OBJECT(), '2025-10-19 12:46:00'),
(9005, 1005, 7, 'bike', 'delivered', 28, '2025-10-10 23:55:00', JSON_OBJECT('tip',50), '2025-10-10 23:01:00'),
(9006, 1006, NULL, NULL, 'assigned', 35, NULL, JSON_OBJECT(), '2025-10-23 07:12:00'),
(9007, 1007, 2, 'bike', 'assigned', 18, NULL, JSON_OBJECT(), '2025-10-22 08:32:00'),
(9008, 1008, 7, 'bike', 'delivered', 30, '2025-10-19 20:10:00', JSON_OBJECT(), '2025-10-19 19:30:00'),
(9009, 1009, NULL, NULL, 'cancelled', NULL, NULL, JSON_OBJECT('reason','order_cancelled'), '2025-10-18 21:05:00'),
(9010, 1010, 2, 'bicycle', 'picked_up', 12, NULL, JSON_OBJECT(), '2025-10-22 12:20:00'),
(9011, 1011, NULL, NULL, 'assigned', 25, NULL, JSON_OBJECT(), '2025-10-17 16:56:00'),
(9012, 1012, 7, 'car', 'picked_up', 40, NULL, JSON_OBJECT(), '2025-10-20 11:15:00');

