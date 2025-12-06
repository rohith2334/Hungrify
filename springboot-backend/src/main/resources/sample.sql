-- ==========================================================
-- HUNGRIFY DATABASE - DATA INSERTS
-- Insert users, admin, restaurants, ingredients, food items,
-- profiles, and food_item_ingredients (junction).
-- ==========================================================

USE hungrify_db1;

-- -----------------------
-- 1) USERS (8 users)
-- We'll set explicit user_id so admin/admin.admin_id mapping is deterministic.
-- -----------------------
INSERT INTO users (user_id, username, password, email, roles, verified, active, created_at)
VALUES
  (1, 'admin1', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'admin1@gmail.com', 'ROLE_ADMIN', TRUE, TRUE, NOW()),
  (2, 'user1', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'user1@gmail.com', 'ROLE_USER', TRUE, TRUE, NOW()),
  (3, 'user2', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'user2@gmail.com', 'ROLE_USER', TRUE, TRUE, NOW()),
  (4, 'restaurant1', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'restaurant1@gmail.com', 'ROLE_RESTAURANT', TRUE, TRUE, NOW()),
  (5, 'restaurant2', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'restaurant2@gmail.com', 'ROLE_RESTAURANT', TRUE, TRUE, NOW()),
  (6, 'restaurant3', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'restaurant3@gmail.com', 'ROLE_RESTAURANT', TRUE, TRUE, NOW()),
  (7, 'delivery1', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'delivery1@gmail.com', 'ROLE_DELIVERY_AGENT', TRUE, TRUE, NOW()),
  (8, 'delivery2', '$2a$10$oZsynn5EszgNH.jccmkumeINg15muEOg4YFTEGJ0mDJrP/8prZj8O', 'delivery2@gmail.com', 'ROLE_DELIVERY_AGENT', TRUE, TRUE, NOW());

-- -----------------------
-- 2) ADMIN (1 admin mapping to users.user_id = 1)
-- -----------------------
INSERT INTO admin (admin_id, username, full_name, email, profile_json)
VALUES
  (1, 'admin1', 'Platform Admin', 'admin1@gmail.com', JSON_OBJECT('permissions', JSON_ARRAY('manage_users','manage_restaurants','view_reports')));

-- -----------------------
-- 3) RESTAURANTS (3 restaurants owned by user_ids 4,5,6)
-- Add restaurant_meta JSON with attributes and a few tags/specialties
-- -----------------------
INSERT INTO restaurants (restaurant_id, owner_user_id, name, cuisine, address, city, state, postal_code, latitude, longitude, restaurant_meta, is_active)
VALUES
  (1, 4, 'The Hungry Spoon', 'American', '123 Main St', 'New Delhi', 'Delhi', '110001', 28.613939, 77.209021,
    JSON_OBJECT(
      'documents', JSON_ARRAY(),
      'ratingSummary', JSON_OBJECT('rating',4.5,'reviews',124),
      'openHours', JSON_ARRAY('Mon-Fri 10:00-22:00','Sat-Sun 09:00-23:00'),
      'adminNote','Flag for seasonal menu',
      'attributes', JSON_OBJECT('isPureVeg', FALSE,'isVeganFriendly', TRUE,'isGlutenFreeFriendly', TRUE,'isHalalCertified', FALSE,'isCloudKitchen', FALSE,'isOrganicIngredients', TRUE,'isNutFreeFriendly', FALSE),
      'specialties', JSON_ARRAY('Smash Burgers','Truffle Fries'),
      'tags', JSON_ARRAY('family_friendly','fast_casual')
    ), TRUE),

  (2, 5, 'Green Fork Bistro', 'International', '45 Oak Ave', 'Bengaluru', 'Karnataka', '560001', 12.971599, 77.594566,
    JSON_OBJECT(
      'documents', JSON_ARRAY(),
      'ratingSummary', JSON_OBJECT('rating',4.2,'reviews',89),
      'openHours', JSON_ARRAY('Daily 11:00-23:00'),
      'adminNote','Focus on plant-forward options',
      'attributes', JSON_OBJECT('isPureVeg', FALSE,'isVeganFriendly', TRUE,'isGlutenFreeFriendly', TRUE,'isHalalCertified', TRUE,'isCloudKitchen', FALSE,'isOrganicIngredients', TRUE,'isNutFreeFriendly', FALSE),
      'specialties', JSON_ARRAY('Salads','Cold-pressed juices'),
      'tags', JSON_ARRAY('healthy','aesthetic')
    ), TRUE),

  (3, 6, 'Pasta & Pie Co.', 'Italian', '88 River Rd', 'Mumbai', 'Maharashtra', '400001', 19.075983, 72.877655,
    JSON_OBJECT(
      'documents', JSON_ARRAY(),
      'ratingSummary', JSON_OBJECT('rating',4.0,'reviews',56),
      'openHours', JSON_ARRAY('Tue-Sun 12:00-22:00'),
      'adminNote','Seasonal pizza specials',
      'attributes', JSON_OBJECT('isPureVeg', FALSE,'isVeganFriendly', FALSE,'isGlutenFreeFriendly', FALSE,'isHalalCertified', FALSE,'isCloudKitchen', TRUE,'isOrganicIngredients', FALSE,'isNutFreeFriendly', FALSE),
      'specialties', JSON_ARRAY('Hand-tossed Pizzas','Fresh Pasta'),
      'tags', JSON_ARRAY('italian','pizza_pasta')
    ), TRUE);

-- -----------------------
-- 4) INGREDIENTS (a reasonable catalogue of common ingredients + allergens array)
-- ingredient_id set explicitly so we can reference deterministically in junction table.
-- -----------------------
INSERT INTO ingredients (ingredient_id, name, display_name, allergens, meta)
VALUES
  (1, 'chicken_breast', 'Chicken Breast', JSON_ARRAY(), JSON_OBJECT('type','meat','unit','g')),
  (2, 'beef_patty', 'Beef Patty', JSON_ARRAY(), JSON_OBJECT('type','meat','unit','g')),
  (3, 'bacon', 'Bacon', JSON_ARRAY(), JSON_OBJECT('type','meat','unit','g')),
  (4, 'tomato', 'Tomato', JSON_ARRAY(), JSON_OBJECT('type','veg','unit','g')),
  (5, 'lettuce', 'Lettuce', JSON_ARRAY(), JSON_OBJECT('type','veg','unit','g')),
  (6, 'cheddar_cheese', 'Cheddar Cheese', JSON_ARRAY('milk'), JSON_OBJECT('type','dairy','unit','g')),
  (7, 'mozzarella', 'Mozzarella', JSON_ARRAY('milk'), JSON_OBJECT('type','dairy','unit','g')),
  (8, 'parmesan', 'Parmesan', JSON_ARRAY('milk'), JSON_OBJECT('type','dairy','unit','g')),
  (9, 'wheat_flour', 'Wheat Flour', JSON_ARRAY('wheat'), JSON_OBJECT('type','grain','unit','g')),
  (10, 'egg', 'Egg', JSON_ARRAY('eggs'), JSON_OBJECT('type','protein','unit','pcs')),
  (11, 'milk', 'Milk', JSON_ARRAY('milk'), JSON_OBJECT('type','dairy','unit','ml')),
  (12, 'olive_oil', 'Olive Oil', JSON_ARRAY(), JSON_OBJECT('type','oil','unit','ml')),
  (13, 'butter', 'Butter', JSON_ARRAY('milk'), JSON_OBJECT('type','dairy','unit','g')),
  (14, 'garlic', 'Garlic', JSON_ARRAY(), JSON_OBJECT('type','spice','unit','g')),
  (15, 'onion', 'Onion', JSON_ARRAY(), JSON_OBJECT('type','veg','unit','g')),
  (16, 'shrimp', 'Shrimp', JSON_ARRAY('shellfish'), JSON_OBJECT('type','seafood','unit','g')),
  (17, 'salmon', 'Salmon', JSON_ARRAY('fish'), JSON_OBJECT('type','seafood','unit','g')),
  (18, 'peanut', 'Peanut', JSON_ARRAY('peanuts'), JSON_OBJECT('type','nut','unit','g')),
  (19, 'almond', 'Almond', JSON_ARRAY('tree_nuts'), JSON_OBJECT('type','nut','unit','g')),
  (20, 'soy_sauce', 'Soy Sauce', JSON_ARRAY('soy','wheat'), JSON_OBJECT('type','condiment','unit','ml')),
  (21, 'basil', 'Basil', JSON_ARRAY(), JSON_OBJECT('type','herb','unit','g')),
  (22, 'mushroom', 'Mushroom', JSON_ARRAY(), JSON_OBJECT('type','veg','unit','g')),
  (23, 'potato', 'Potato', JSON_ARRAY(), JSON_OBJECT('type','veg','unit','g')),
  (24, 'sugar', 'Sugar', JSON_ARRAY(), JSON_OBJECT('type','pantry','unit','g')),
  (25, 'cocoa', 'Cocoa Powder', JSON_ARRAY(), JSON_OBJECT('type','pantry','unit','g')),
  (26, 'cream', 'Fresh Cream', JSON_ARRAY('milk'), JSON_OBJECT('type','dairy','unit','ml')),
  (27, 'pasta_noodles', 'Pasta Noodles', JSON_ARRAY('wheat'), JSON_OBJECT('type','grain','unit','g')),
  (28, 'pizza_sauce', 'Pizza Sauce', JSON_ARRAY(), JSON_OBJECT('type','condiment','unit','g')),
  (29, 'spinach', 'Spinach', JSON_ARRAY(), JSON_OBJECT('type','veg','unit','g')),
  (30, 'cilantro', 'Cilantro', JSON_ARRAY(), JSON_OBJECT('type','herb','unit','g')),
  (31, 'paneer', 'Paneer (Cottage Cheese)', JSON_ARRAY('milk'), JSON_OBJECT('type','dairy','unit','g')),
  (32, 'capsicum', 'Capsicum / Bell Pepper', JSON_ARRAY(), JSON_OBJECT('type','veg','unit','g')),
  (33, 'cashew', 'Cashew Nuts', JSON_ARRAY('tree_nuts'), JSON_OBJECT('type','nut','unit','g')),
  (34, 'basmati_rice', 'Basmati Rice', JSON_ARRAY(), JSON_OBJECT('type','grain','unit','g')),
  (35, 'biryani_spices', 'Biryani Spices', JSON_ARRAY(), JSON_OBJECT('type','spice_mix')),
  (36, 'mushroom_king_oyster', 'Mushroom (Button/King Oyster)', JSON_ARRAY(), JSON_OBJECT('type','fungi','unit','g')),
  (37, 'tofu', 'Tofu', JSON_ARRAY('soy'), JSON_OBJECT('type','soy_protein','unit','g')),
  (38, 'oats', 'Rolled Oats', JSON_ARRAY(), JSON_OBJECT('type','grain','unit','g')),
  (39, 'chickpea', 'Chickpeas', JSON_ARRAY(), JSON_OBJECT('type','legume','unit','g')),
  (40, 'coconut_milk', 'Coconut Milk', JSON_ARRAY(), JSON_OBJECT('type','dairy_alternative','unit','ml')),
  (41, 'avocado', 'Avocado', JSON_ARRAY(), JSON_OBJECT('type','fruit','unit','g')),
  (42, 'jaggery', 'Jaggery', JSON_ARRAY(), JSON_OBJECT('type','sweetener','unit','g')),
  (43, 'millet', 'Millet', JSON_ARRAY(), JSON_OBJECT('type','grain','unit','g')),
  (44, 'quinoa', 'Quinoa', JSON_ARRAY(), JSON_OBJECT('type','grain','unit','g')),
  (45, 'green_chili', 'Green Chili', JSON_ARRAY(), JSON_OBJECT('type','spice','unit','g')),
  (46, 'street_spice_mix', 'Street Spice Mix', JSON_ARRAY(), JSON_OBJECT('type','spice','unit','g'));

-- -----------------------
-- 5) FOOD ITEMS (45 items total).
-- We explicitly set item_id values 1..45. Each row includes an image_urls JSON array using 1.jpg..10.jpg randomly.
-- For price, prep_time, category assignment, see profiles inserted below for category_name mapping.
-- -----------------------
INSERT INTO food_items (item_id, restaurant_id, canonical_name, display_name, short_description, long_description, price, quantity, is_available, prep_time_minutes, image_urls, rating, created_at)
VALUES
  -- Restaurant 1 (20 items) items 1..20
  (1, 1, 'classic_cheeseburger', 'Classic Cheeseburger', 'Juicy beef patty, cheddar, lettuce, tomato, house sauce', 'A classic American cheeseburger with a melted cheddar, crisp lettuce and vine-ripe tomato.', 12.99, 7, TRUE, 15, JSON_ARRAY('3.jpg','7.jpg'), 4.5, NOW()),
(2, 1, 'truffle_fries', 'Truffle Fries', 'Crispy fries tossed with truffle oil and parmesan', 'Hand-cut fries tossed in truffle oil and grated parmesan.', 8.99, 5, TRUE, 12, JSON_ARRAY('2.jpg'), 4.6, NOW()),
(3, 1, 'caesar_salad', 'Caesar Salad', 'Romaine, parmesan, croutons, caesar dressing', 'Classic Caesar with shaved parmesan and crunchy croutons.', 11.99, 8, TRUE, 10, JSON_ARRAY('5.jpg'), 4.3, NOW()),
(4, 1, 'chicken_wings', 'Buffalo Chicken Wings', '8 pcs spicy wings with blue cheese dip', 'Crispy wings tossed in buffalo sauce served with a blue cheese dip.', 14.99, 6, TRUE, 20, JSON_ARRAY('4.jpg'), 4.4, NOW()),
(5, 1, 'margherita_pizza', 'Margherita Pizza', 'Fresh mozzarella, basil, tomato sauce', 'Stone-baked pizza with fresh mozzarella and basil.', 15.99, 9, TRUE, 18, JSON_ARRAY('8.jpg'), 4.2, NOW()),
(6, 1, 'spaghetti_alfredo', 'Spaghetti Alfredo', 'Creamy parmesan sauce, garlic, parsley', 'Creamy Alfredo pasta finished with parmesan and garlic.', 16.99, 4, TRUE, 20, JSON_ARRAY('6.jpg'), 4.1, NOW()),
(7, 1, 'chicken_alfredo_pasta', 'Chicken Alfredo Pasta', 'White sauce pasta with chicken and vegetables', 'Tender chicken with alfredo sauce and seasonal vegetables.', 18.99, 7, TRUE, 22, JSON_ARRAY('1.jpg'), 4.5, NOW()),
(8, 1, 'garden_salad', 'Garden Salad', 'Mixed greens, tomato, cucumber, lemon vinaigrette', 'A fresh garden salad with a light lemon vinaigrette.', 9.99, 10, TRUE, 8, JSON_ARRAY('9.jpg'), 4.0, NOW()),
(9, 1, 'onion_rings', 'Crispy Onion Rings', 'Beer-battered onion rings', 'Crispy, golden beer-battered onion rings.', 7.99, 6, TRUE, 10, JSON_ARRAY('10.jpg'), 4.1, NOW()),
(10, 1, 'chocolate_lava_cake', 'Chocolate Lava Cake', 'Warm chocolate cake with molten center', 'Decadent warm lava cake served with vanilla ice cream.', 9.99, 5, TRUE, 12, JSON_ARRAY('2.jpg','5.jpg'), 4.7, NOW()),
(11, 1, 'grilled_chicken_sandwich', 'Grilled Chicken Sandwich', 'Marinated chicken breast, lettuce, mayo', 'Grilled chicken on brioche with lettuce and mayo.', 13.99, 8, TRUE, 14, JSON_ARRAY('6.jpg'), 4.3, NOW()),
(12, 1, 'bbq_bacon_burger', 'BBQ Bacon Burger', 'Beef, smoked bacon, BBQ glaze', 'Smoky BBQ glaze and crispy bacon on a premium beef patty.', 15.99, 4, TRUE, 16, JSON_ARRAY('7.jpg'), 4.5, NOW()),
(13, 1, 'clam_chowder', 'New England Clam Chowder', 'Creamy chowder with clams and potatoes', 'Rich and creamy chowder loaded with clams and potatoes.', 11.99, 7, TRUE, 18, JSON_ARRAY('3.jpg'), 4.0, NOW()),
(14, 1, 'veg_burger', 'Veggie Burger', 'House patty, lettuce, tomato, vegan mayo', 'Flavorful house-made veggie patty with vegan mayo.', 11.99, 9, TRUE, 15, JSON_ARRAY('4.jpg'), 4.0, NOW()),
(15, 1, 'garlic_bread', 'Garlic Bread', 'Toasted baguette with garlic butter', 'Buttery garlic bread toasted until golden.', 5.99, 6, TRUE, 8, JSON_ARRAY('8.jpg'), 4.2, NOW()),
(16, 1, 'soda_coke', 'Coca-Cola (330ml)', 'Classic Coke', 'Chilled Coke in a can.', 2.99, 10, TRUE, 0, JSON_ARRAY('1.jpg'), 4.0, NOW()),
(17, 1, 'fruit_salad', 'Fresh Fruit Salad', 'Seasonal fresh fruits', 'A medley of seasonal fruits.', 8.99, 5, TRUE, 5, JSON_ARRAY('9.jpg'), 4.1, NOW()),
(18, 1, 'fish_tacos', 'Fish Tacos', 'Grilled salmon, slaw, lime', 'Soft tacos with grilled salmon and citrus slaw.', 16.99, 8, TRUE, 18, JSON_ARRAY('10.jpg'), 4.3, NOW()),
(19, 1, 'mac_and_cheese', 'Mac & Cheese', 'Creamy cheddar macaroni bake', 'Baked macaroni in creamy cheddar sauce.', 12.99, 4, TRUE, 14, JSON_ARRAY('5.jpg'), 4.4, NOW()),
(20, 1, 'vanilla_milkshake', 'Vanilla Milkshake', 'Creamy shake with whipped cream', 'Rich vanilla milkshake topped with whipped cream.', 6.99, 7, TRUE, 6, JSON_ARRAY('7.jpg'), 4.2, NOW()),

-- Restaurant 2 (15 items) items 21..35
(21, 2, 'greek_salad', 'Greek Salad', 'Cucumber, tomato, feta, olives', 'Crisp salad with feta and Kalamata olives.', 12.99, 6, TRUE, 10, JSON_ARRAY('2.jpg'), 4.4, NOW()),
(22, 2, 'avocado_toast', 'Avocado Toast', 'Sourdough, smashed avocado, chili', 'Smashed avocado on rustic sourdough.', 10.99, 9, TRUE, 8, JSON_ARRAY('3.jpg'), 4.3, NOW()),
(23, 2, 'quinoa_bowl', 'Quinoa Power Bowl', 'Quinoa, spinach, roasted veg, tahini', 'Protein-rich quinoa bowl with roasted vegetables.', 14.99, 5, TRUE, 15, JSON_ARRAY('1.jpg'), 4.5, NOW()),
(24, 2, 'grilled_salmon_plate', 'Grilled Salmon Plate', 'Salmon fillet, lemon, vegetables', 'Seared salmon with seasonal vegetables.', 22.99, 8, TRUE, 20, JSON_ARRAY('6.jpg'), 4.6, NOW()),
(25, 2, 'vegan_burger', 'Vegan Burger', 'Plant patty, lettuce, vegan aioli', 'Plant-based burger with homemade vegan patty.', 13.99, 4, TRUE, 16, JSON_ARRAY('4.jpg'), 4.2, NOW()),
(26, 2, 'sweet_potato_fries', 'Sweet Potato Fries', 'Crispy sweet potato fries', 'Served with a tangy dip.', 8.99, 7, TRUE, 12, JSON_ARRAY('5.jpg'), 4.1, NOW()),
(27, 2, 'lentil_soup', 'Lentil Soup', 'Hearty lentil soup with herbs', 'Comforting soup with spiced lentils.', 7.99, 10, TRUE, 12, JSON_ARRAY('7.jpg'), 4.0, NOW()),
(28, 2, 'berry_smoothie', 'Berry Smoothie', 'Mixed berries, yogurt, honey', 'Refreshing smoothie with seasonal berries.', 7.99, 6, TRUE, 5, JSON_ARRAY('9.jpg'), 4.3, NOW()),
(29, 2, 'caprese_sandwich', 'Caprese Sandwich', 'Mozzarella, tomato, basil', 'Mozzarella and basil on ciabatta.', 11.99, 5, TRUE, 10, JSON_ARRAY('8.jpg'), 4.1, NOW()),
(30, 2, 'roasted_veg_pasta', 'Roasted Veg Pasta', 'Pasta, roasted vegetables, pesto', 'Pasta tossed with seasonal roasted veg and pesto.', 15.99, 9, TRUE, 18, JSON_ARRAY('10.jpg'), 4.2, NOW()),
(31, 2, 'hummus_plate', 'Hummus Plate', 'Hummus, pita, olives, veggies', 'Creamy hummus served with warm pita.', 9.99, 8, TRUE, 7, JSON_ARRAY('2.jpg'), 4.0, NOW()),
(32, 2, 'chocolate_mousse', 'Chocolate Mousse', 'Light chocolate mousse', 'Silky chocolate mousse topped with cocoa.', 8.99, 4, TRUE, 10, JSON_ARRAY('3.jpg'), 4.5, NOW()),
(33, 2, 'grilled_halloumi', 'Grilled Halloumi', 'Sliced halloumi, lemon, herbs', 'Char-grilled halloumi with lemon and herbs.', 11.99, 7, TRUE, 12, JSON_ARRAY('1.jpg'), 4.2, NOW()),
(34, 2, 'iced_tea', 'Iced Tea (Lemon)', 'Cold brewed lemon iced tea', 'Refreshing cold-brewed iced tea with lemon.', 3.99, 10, TRUE, 0, JSON_ARRAY('4.jpg'), 4.0, NOW()),
(35, 2, 'peach_pie', 'Peach Pie', 'Warm peach pie with crumble', 'Homemade peach pie served warm.', 9.99, 6, TRUE, 14, JSON_ARRAY('5.jpg'), 4.3, NOW()),

-- Restaurant 3 (10 items) items 36..45
(36, 3, 'pepperoni_pizza', 'Pepperoni Pizza', 'Pepperoni, mozzarella, tomato sauce', 'Classic pepperoni with generous cheese.', 17.99, 5, TRUE, 18, JSON_ARRAY('6.jpg'), 4.4, NOW()),
(37, 3, 'four_cheese_pizza', 'Four Cheese Pizza', 'Mozzarella, parmesan, cheddar, gorgonzola', 'Cheese lover\'s pizza with premium cheeses.', 19.99, 9, TRUE, 18, JSON_ARRAY('7.jpg'), 4.5, NOW()),
(38, 3, 'fettuccine_alfredo', 'Fettuccine Alfredo', 'Creamy alfredo with parmesan', 'Fettuccine pasta in rich alfredo sauce.', 16.99, 8, TRUE, 20, JSON_ARRAY('8.jpg'), 4.1, NOW()),
(39, 3, 'pesto_pasta', 'Pesto Pasta', 'Basil pesto, pine nuts, parmesan', 'Fresh pesto sauce tossed with pasta.', 15.99, 4, TRUE, 15, JSON_ARRAY('9.jpg'), 4.2, NOW()),
(40, 3, 'garlic_parmesan_bread', 'Garlic Parmesan Bread', 'Toasted bread with garlic and parmesan', 'Buttery garlic bread topped with parmesan.', 6.99, 7, TRUE, 8, JSON_ARRAY('10.jpg'), 4.0, NOW()),
(41, 3, 'antipasto_plate', 'Antipasto Plate', 'Cured meats, cheese, olives', 'Selection of cured meats and cheeses.', 14.99, 10, TRUE, 12, JSON_ARRAY('1.jpg'), 4.1, NOW()),
(42, 3, 'tiramisu', 'Tiramisu', 'Classic tiramisu with espresso', 'Layered tiramisu with espresso-soaked ladyfingers.', 10.99, 6, TRUE, 10, JSON_ARRAY('2.jpg'), 4.6, NOW()),
(43, 3, 'bruschetta', 'Bruschetta', 'Tomato, basil, garlic on toast', 'Toasted bread with tomato and basil mix.', 7.99, 5, TRUE, 8, JSON_ARRAY('3.jpg'), 4.2, NOW()),
(44, 3, 'calzone', 'Calzone', 'Folded pizza with ricotta & salami', 'Stuffed calzone with ricotta cheese and salami.', 16.99, 8, TRUE, 20, JSON_ARRAY('4.jpg'), 4.0, NOW()),
(45, 3, 'espresso', 'Espresso Shot', 'Freshly brewed espresso', 'Single shot of rich espresso.', 3.50, 9, TRUE, 0, JSON_ARRAY('5.jpg'), 4.3, NOW()),

(46, 2, 'paneer_capsicum_tikka', 'Paneer Capsicum Tikka', 'Grilled paneer & capsicum in tikka masala', 'Charred capsicum with marinated paneer in a light tikka glaze.', 13.99, 4, TRUE, 18, JSON_ARRAY('1.jpg','3.jpg'), 4.6, NOW()),
(47, 1, 'egg_bhurji_dry', 'Egg Bhurji (No Dairy)', 'Spiced scrambled eggs – strictly no milk or cream', 'Dry egg bhurji with onion, tomato, green chili. No dairy added.', 8.99, 7, TRUE, 10, JSON_ARRAY('2.jpg'), 4.2, NOW()),
(48, 3, 'artisan_cheese_platter', 'Artisan Cheese Platter', 'Assortment of cheeses, no tomato', 'Variety of cheeses served with nuts and crackers. No tomato used.', 24.99, 6, TRUE, 5, JSON_ARRAY('4.jpg'), 4.7, NOW()),
(49, 1, 'shrimp_schezwan', 'Shrimp Schezwan (garlic removable)', 'Schezwan-style shrimp stir-fry (garlic optional)', 'Spicy-sweet schezwan shrimp; garlic can be left out on request.', 17.99, 5, TRUE, 16, JSON_ARRAY('5.jpg'), 4.5, NOW()),
(50, 2, 'veg_cashew_biryani', 'Veg Cashew Biryani', 'Fragrant biryani with roasted cashews', 'Basmati layered biryani, whole spices and roasted cashews on top.', 16.99, 9, TRUE, 35, JSON_ARRAY('6.jpg'), 4.4, NOW()),
(51, 2, 'mushroom_tandoori_fixed', 'Mushroom Tandoori (non-removable)', 'Tandoori mushrooms in a house marinade', 'Marinated mushrooms - preparation requires mushrooms (non-removable).', 14.99, 8, TRUE, 20, JSON_ARRAY('7.jpg'), 4.3, NOW()),
(52, 2, 'oats_fruit_bowl', 'Oats & Fruit Bowl (Low Fat)', 'Rolled oats with fruit and honey - low fat', 'Healthy bowl with oats, seasonal fruit, minimal oil.', 10.99, 4, TRUE, 6, JSON_ARRAY('8.jpg'), 4.2, NOW()),
(53, 1, 'keto_chicken_salad', 'Keto Chicken Salad', 'Leafy bowl with grilled chicken, avocado', 'Low-carb, high-fat salad suitable for keto.', 18.99, 7, TRUE, 12, JSON_ARRAY('9.jpg'), 4.6, NOW()),
(54, 2, 'chickpea_power_bowl', 'Chickpea Power Bowl', 'High-fiber vegetarian bowl with millet & chickpeas', 'Loaded with chickpeas, millet, greens — great fiber content.', 13.99, 10, TRUE, 15, JSON_ARRAY('10.jpg'), 4.3, NOW()),
(55, 3, 'avocado_choco_mousse', 'Avocado Chocolate Mousse (no milk)', 'Dark cocoa with avocado — low-carb and dairy-free', 'Creamy cocoa mousse using avocado and jaggery (or erythritol).', 11.99, 6, TRUE, 6, JSON_ARRAY('1.jpg'), 4.4, NOW()),
(56, 1, 'kids_paneer_nuggets', 'Kids Paneer Nuggets (Mild)', 'Soft paneer bites, kid-friendly mild spice', 'Breaded paneer bites baked, very mild seasoning for kids.', 9.99, 5, TRUE, 10, JSON_ARRAY('2.jpg'), 4.5, NOW()),
(57, 1, 'aloo_chaat_street', 'Street Aloo Chaat', 'Sour & spicy potato chaat', 'Tangy, spicy potato chaat with chutneys and chaat masala.', 6.99, 9, TRUE, 5, JSON_ARRAY('3.jpg'), 4.1, NOW()),
(58, 1, 'egg_sandwich_quick', 'Quick Egg Sandwich', 'Egg sandwich, ready fast under ₹150', 'Toasted sandwich with spiced egg filling; quick prep.', 7.99, 8, TRUE, 8, JSON_ARRAY('4.jpg'), 4.0, NOW()),
(59, 3, 'black_truffle_tagliolini', 'Black Truffle Tagliolini (Premium)', 'Handmade pasta with black truffle shavings', 'Luxury pasta dish with shaved black truffle and parmesan.', 42.99, 4, TRUE, 25, JSON_ARRAY('5.jpg'), 4.8, NOW()),
(60, 3, 'coconut_mango_panna', 'Coconut Mango Panna (GF, Dairy-free)', 'Coconut milk panna with mango', 'Gluten-free, dairy-free dessert using coconut milk and agar.', 10.99, 7, TRUE, 6, JSON_ARRAY('6.jpg'), 4.2, NOW()),
(61, 1, 'spicy_tangy_chicken', 'Spicy Tangy Chicken Starter', 'Tangy & spicy chicken bites', 'Marinated spicy chicken with tangy sauce - medium-high heat.', 15.99, 6, TRUE, 14, JSON_ARRAY('7.jpg'), 4.4, NOW()),
(62, 2, 'vegan_protein_quinoa', 'Vegan Protein Quinoa Bowl', 'Quinoa, tofu, chickpeas - high protein, <400 kcal', 'Vegan bowl designed for protein with quinoa and tofu.', 16.99, 5, TRUE, 15, JSON_ARRAY('8.jpg'), 4.5, NOW());
-- -----------------------
-- 6) FOOD ITEM PROFILES (one profile per item_id). Set profile_id explicitly 1..45
-- category_code chosen from your categories; calories and macros are illustrative and valid.
-- allergens arrays reference strings like 'milk','eggs','wheat','peanuts','shellfish','fish','tree_nuts','soy','sesame'
-- -----------------------
INSERT INTO food_item_profiles (profile_id, item_id, category_code, category_name, calories_kcal, carbs_g, protein_g, fats_g, spice_score, spice_level, allergens, tags, taste_profile)
VALUES
  -- Restaurant 1 profiles (items 1..20)
  (1, 1, 'burgers_sandwiches', 'Burgers & Sandwiches', 820, 45.00, 40.00, 48.00, 3, 'medium', JSON_ARRAY('wheat','milk'), JSON_ARRAY('hearty','classic'), JSON_OBJECT('savory',0.8,'umami',0.6)),
  (2, 2, 'sides', 'Sides', 420, 45.00, 6.00, 24.00, 1, 'mild', JSON_ARRAY('milk'), JSON_ARRAY('truffle','savory'), JSON_OBJECT('salty',0.7)),
  (3, 3, 'salads', 'Salads', 250, 12.00, 8.00, 18.00, 0, 'none', JSON_ARRAY('wheat','milk'), JSON_ARRAY('fresh'), JSON_OBJECT('fresh',0.9)),
  (4, 4, 'appetizers', 'Appetizers', 650, 30.00, 30.00, 34.00, 4, 'spicy', JSON_ARRAY('milk'), JSON_ARRAY('spicy'), JSON_OBJECT('spicy',0.8)),
  (5, 5, 'pizza_pasta', 'Pizza & Pasta', 900, 80.00, 30.00, 42.00, 2, 'mild', JSON_ARRAY('wheat','milk'), JSON_ARRAY('cheesy'), JSON_OBJECT('cheesy',0.9)),
  (6, 6, 'pizza_pasta', 'Pizza & Pasta', 820, 65.00, 25.00, 38.00, 1, 'mild', JSON_ARRAY('milk'), JSON_ARRAY('creamy'), JSON_OBJECT('creamy',0.85)),
  (7, 7, 'pizza_pasta', 'Pizza & Pasta', 860, 70.00, 35.00, 40.00, 1, 'mild', JSON_ARRAY('milk'), JSON_ARRAY('chicken'), JSON_OBJECT('creamy',0.8)),
  (8, 8, 'salads', 'Salads', 160, 8.00, 3.00, 12.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('light'), JSON_OBJECT('fresh',0.9)),
  (9, 9, 'sides', 'Sides', 320, 35.00, 5.00, 18.00, 1, 'mild', JSON_ARRAY('wheat'), JSON_ARRAY('crispy'), JSON_OBJECT('salty',0.6)),
  (10, 10, 'desserts', 'Desserts', 480, 55.00, 6.00, 22.00, 0, 'none', JSON_ARRAY('milk','eggs','wheat'), JSON_ARRAY('chocolate'), JSON_OBJECT('sweet',0.95)),
  (11, 11, 'burgers_sandwiches', 'Burgers & Sandwiches', 650, 45.00, 38.00, 28.00, 2, 'mild', JSON_ARRAY('wheat','milk'), JSON_ARRAY('grilled'), JSON_OBJECT('savory',0.8)),
  (12, 12, 'burgers_sandwiches', 'Burgers & Sandwiches', 880, 48.00, 42.00, 50.00, 3, 'medium', JSON_ARRAY('wheat','milk'), JSON_ARRAY('bbq','smoky'), JSON_OBJECT('smoky',0.85)),
  (13, 13, 'soups', 'Soups', 360, 18.00, 20.00, 16.00, 0, 'none', JSON_ARRAY('milk','shellfish'), JSON_ARRAY('comfort'), JSON_OBJECT('savory',0.7)),
  (14, 14, 'burgers_sandwiches', 'Burgers & Sandwiches', 520, 38.00, 20.00, 22.00, 1, 'mild', JSON_ARRAY('wheat'), JSON_ARRAY('vegan'), JSON_OBJECT('umami',0.6)),
  (15, 15, 'sides', 'Sides', 240, 28.00, 4.00, 12.00, 0, 'none', JSON_ARRAY('milk'), JSON_ARRAY('garlicky'), JSON_OBJECT('buttery',0.7)),
  (16, 16, 'drinks', 'Drinks / Beverages', 150, 35.00, 0.00, 0.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('refreshing'), JSON_OBJECT('sweet',0.4)),
  (17, 17, 'salads', 'Salads', 120, 20.00, 1.00, 2.00, 0, 'none', JSON_ARRAY('fish'), JSON_ARRAY('fresh'), JSON_OBJECT('fresh',0.8)),
  (18, 18, 'entrees', 'Entrées / Mains', 480, 36.00, 28.00, 20.00, 2, 'mild', JSON_ARRAY('fish'), JSON_ARRAY('tacos'), JSON_OBJECT('zesty',0.6)),
  (19, 19, 'sides', 'Sides', 560, 60.00, 20.00, 26.00, 1, 'mild', JSON_ARRAY('milk','wheat'), JSON_ARRAY('comfort'), JSON_OBJECT('cheesy',0.8)),
  (20, 20, 'drinks', 'Drinks / Beverages', 420, 60.00, 6.00, 12.00, 0, 'none', JSON_ARRAY('milk'), JSON_ARRAY('dessert_drink'), JSON_OBJECT('sweet',0.9)),

  -- Restaurant 2 profiles (21..35)
  (21, 21, 'salads', 'Salads', 300, 12.00, 7.00, 22.00, 0, 'none', JSON_ARRAY('milk'), JSON_ARRAY('mediteranean'), JSON_OBJECT('fresh',0.9)),
  (22, 22, 'sides', 'Sides', 320, 30.00, 6.00, 18.00, 1, 'mild', JSON_ARRAY('wheat'), JSON_ARRAY('brunch'), JSON_OBJECT('savory',0.7)),
  (23, 23, 'entrees', 'Entrées / Mains', 520, 60.00, 14.00, 18.00, 1, 'mild', JSON_ARRAY(), JSON_ARRAY('protein'), JSON_OBJECT('nutty',0.3)),
  (24, 24, 'entrees', 'Entrées / Mains', 540, 8.00, 40.00, 30.00, 1, 'mild', JSON_ARRAY('fish'), JSON_ARRAY('seafood'), JSON_OBJECT('umami',0.7)),
  (25, 25, 'burgers_sandwiches', 'Burgers & Sandwiches', 480, 46.00, 24.00, 18.00, 2, 'mild', JSON_ARRAY('wheat'), JSON_ARRAY('plant_based'), JSON_OBJECT('earthy',0.6)),
  (26, 26, 'sides', 'Sides', 320, 50.00, 4.00, 12.00, 1, 'mild', JSON_ARRAY(), JSON_ARRAY('sweet'), JSON_OBJECT('crispy',0.6)),
  (27, 27, 'soups', 'Soups', 280, 30.00, 14.00, 6.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('hearty'), JSON_OBJECT('savory',0.6)),
  (28, 28, 'drinks', 'Drinks / Beverages', 210, 40.00, 6.00, 3.00, 0, 'none', JSON_ARRAY('milk'), JSON_ARRAY('smoothie'), JSON_OBJECT('sweet',0.8)),
  (29, 29, 'burgers_sandwiches', 'Burgers & Sandwiches', 540, 50.00, 22.00, 20.00, 1, 'mild', JSON_ARRAY('milk'), JSON_ARRAY('italian'), JSON_OBJECT('fresh',0.7)),
  (30, 30, 'pizza_pasta', 'Pizza & Pasta', 700, 80.00, 20.00, 28.00, 2, 'mild', JSON_ARRAY('wheat','milk','tree_nuts'), JSON_ARRAY('pesto'), JSON_OBJECT('herby',0.8)),
  (31, 31, 'appetizers', 'Appetizers', 420, 45.00, 8.00, 18.00, 0, 'none', JSON_ARRAY('wheat','sesame'), JSON_ARRAY('mediteranean'), JSON_OBJECT('creamy',0.6)),
  (32, 32, 'desserts', 'Desserts', 380, 45.00, 5.00, 18.00, 0, 'none', JSON_ARRAY('milk','eggs'), JSON_ARRAY('chocolate'), JSON_OBJECT('sweet',0.9)),
  (33, 33, 'appetizers', 'Appetizers', 360, 6.00, 14.00, 22.00, 1, 'mild', JSON_ARRAY('milk'), JSON_ARRAY('grilled'), JSON_OBJECT('salty',0.6)),
  (34, 34, 'drinks', 'Drinks / Beverages', 90, 24.00, 0.00, 0.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('refreshing'), JSON_OBJECT('bitter',0.2)),
  (35, 35, 'desserts', 'Desserts', 410, 60.00, 5.00, 18.00, 0, 'none', JSON_ARRAY('wheat','milk','eggs'), JSON_ARRAY('pie'), JSON_OBJECT('sweet',0.9)),

  -- Restaurant 3 profiles (36..45)
  (36, 36, 'pizza_pasta', 'Pizza & Pasta', 950, 80.00, 40.00, 48.00, 3, 'medium', JSON_ARRAY('wheat','milk'), JSON_ARRAY('classic'), JSON_OBJECT('savory',0.9)),
  (37, 37, 'pizza_pasta', 'Pizza & Pasta', 980, 82.00, 42.00, 50.00, 2, 'mild', JSON_ARRAY('milk','tree_nuts'), JSON_ARRAY('cheesy'), JSON_OBJECT('rich',0.95)),
  (38, 38, 'pizza_pasta', 'Pizza & Pasta', 860, 75.00, 22.00, 34.00, 1, 'mild', JSON_ARRAY('milk','wheat'), JSON_ARRAY('creamy'), JSON_OBJECT('creamy',0.85)),
  (39, 39, 'pizza_pasta', 'Pizza & Pasta', 700, 60.00, 15.00, 22.00, 1, 'mild', JSON_ARRAY('tree_nuts','wheat'), JSON_ARRAY('herby'), JSON_OBJECT('herbal',0.8)),
  (40, 40, 'sides', 'Sides', 280, 25.00, 6.00, 14.00, 0, 'none', JSON_ARRAY('milk'), JSON_ARRAY('garlic'), JSON_OBJECT('buttery',0.7)),
  (41, 41, 'appetizers', 'Appetizers', 520, 6.00, 20.00, 36.00, 2, 'mild', JSON_ARRAY('milk','wheat'), JSON_ARRAY('charcuterie'), JSON_OBJECT('savory',0.8)),
  (42, 42, 'desserts', 'Desserts', 460, 55.00, 6.00, 20.00, 0, 'none', JSON_ARRAY('milk','eggs','wheat'), JSON_ARRAY('coffee'), JSON_OBJECT('sweet',0.9)),
  (43, 43, 'appetizers', 'Appetizers', 220, 20.00, 3.00, 10.00, 0, 'none', JSON_ARRAY('wheat'), JSON_ARRAY('bruschetta'), JSON_OBJECT('fresh',0.8)),
  (44, 44, 'pizza_pasta', 'Pizza & Pasta', 980, 85.00, 30.00, 46.00, 3, 'medium', JSON_ARRAY('wheat','milk','eggs'), JSON_ARRAY('stuffed'), JSON_OBJECT('rich',0.9)),
  (45, 45, 'drinks', 'Drinks / Beverages', 5, 1.00, 0.00, 0.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('coffee'), JSON_OBJECT('bitter',0.2)),

  -- 46 Paneer Capsicum Tikka (contains milk)
  (46, 46, 'entrées', 'Entrées / Mains', 420, 18.00, 22.00, 24.00, 2, 'mild', JSON_ARRAY('milk'), JSON_ARRAY('vegetarian','tikka','paneer','capsicum'), JSON_OBJECT('savory',0.8,'smoky',0.6)),

  -- 47 Egg Bhurji (eggs but no dairy)
  (47, 47, 'breakfast', 'Breakfast', 180, 6.00, 14.00, 10.00, 1, 'mild', JSON_ARRAY('eggs'), JSON_ARRAY('egg','breakfast','no_dairy'), JSON_OBJECT('savory',0.7)),

  -- 48 Artisan Cheese Platter (cheese but without tomato)
  (48, 48, 'sides', 'Sides', 620, 12.00, 18.00, 46.00, 0, 'none', JSON_ARRAY('milk','tree_nuts'), JSON_ARRAY('cheese','platter','no_tomato'), JSON_OBJECT('rich',0.9)),

  -- 49 Shrimp Schezwan (garlic removable)
  (49, 49, 'entrees', 'Entrées / Mains', 420, 20.00, 30.00, 18.00, 4, 'spicy', JSON_ARRAY('shellfish','soy'), JSON_ARRAY('shrimp','schezwan','garlic_optional'), JSON_OBJECT('spicy',0.9,'umami',0.6)),

  -- 50 Veg Cashew Biryani (contains cashew)
  (50, 50, 'entrees', 'Entrées / Mains', 560, 75.00, 10.00, 18.00, 2, 'mild', JSON_ARRAY('tree_nuts'), JSON_ARRAY('biryani','cashew'), JSON_OBJECT('aromatic',0.8)),

  -- 51 Mushroom Tandoori (mushrooms non-removable)
  (51, 51, 'appetizers', 'Appetizers', 320, 10.00, 8.00, 22.00, 1, 'mild', JSON_ARRAY(), JSON_ARRAY('mushroom','tandoori','non_removable'), JSON_OBJECT('smoky',0.7)),

  -- 52 Oats Fruit Bowl (low-fat <10g fat)
  (52, 52, 'breakfast', 'Breakfast', 320, 45.00, 8.00, 6.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('low_fat','healthy'), JSON_OBJECT('sweet',0.6)),

  -- 53 Keto Chicken Salad (keto)
  (53, 53, 'salads', 'Salads', 420, 6.00, 40.00, 28.00, 1, 'mild', JSON_ARRAY(), JSON_ARRAY('keto','low_carb','high_protein'), JSON_OBJECT('fresh',0.6,'creamy',0.4)),

  -- 54 Chickpea Power Bowl (high-fiber vegetarian)
  (54, 54, 'rice_item', 'Rice Item / Bowls', 380, 48.00, 16.00, 10.00, 1, 'mild', JSON_ARRAY(), JSON_ARRAY('high_fiber','vegetarian'), JSON_OBJECT('earthy',0.6)),

  -- 55 Avocado Chocolate Mousse (low-carb, no milk)
  (55, 55, 'desserts', 'Desserts', 210, 8.00, 3.00, 16.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('low_carb','dairy_free'), JSON_OBJECT('sweet',0.8,'mint_hint',0.1)),

  -- 56 Kids Paneer Nuggets (mild)
  (56, 56, 'sides', 'Sides', 260, 18.00, 18.00, 14.00, 0, 'none', JSON_ARRAY('milk','wheat'), JSON_ARRAY('kid_friendly','mild'), JSON_OBJECT('mild',0.9)),

  -- 57 Aloo Chaat (street food - sour & spicy)
  (57, 57, 'street_food', 'Street Food', 220, 30.00, 4.00, 8.00, 4, 'spicy', JSON_ARRAY(), JSON_ARRAY('street_food','sour','spicy'), JSON_OBJECT('tangy',0.9,'spicy',0.8)),

  -- 58 Quick Egg Sandwich (fast, cheap)
  (58, 58, 'breakfast', 'Breakfast', 320, 30.00, 12.00, 14.00, 1, 'mild', JSON_ARRAY('wheat','eggs'), JSON_ARRAY('quick','value'), JSON_OBJECT('savory',0.6)),

  -- 59 Black Truffle Tagliolini (premium > ₹700)
  (59, 59, 'pizza_pasta', 'Pizza & Pasta', 820, 60.00, 18.00, 40.00, 1, 'mild', JSON_ARRAY('milk','wheat'), JSON_ARRAY('premium','truffle'), JSON_OBJECT('rich',0.95)),

  -- 60 Coconut Mango Panna (GF, dairy-free)
  (60, 60, 'desserts', 'Desserts', 190, 18.00, 2.00, 10.00, 0, 'none', JSON_ARRAY(), JSON_ARRAY('gluten_free','dairy_free'), JSON_OBJECT('sweet',0.8,'tropical',0.6)),

  -- 61 Spicy Tangy Chicken Starter (spicy-tangy)
  (61, 61, 'appetizers', 'Appetizers', 380, 12.00, 30.00, 18.00, 4, 'spicy', JSON_ARRAY(), JSON_ARRAY('spicy','tangy','starter'), JSON_OBJECT('spicy',0.9,'tangy',0.7)),

  -- 62 Vegan Protein Quinoa Bowl (vegan & <400 kcal)
  (62, 62, 'rice_item', 'Rice Item / Bowls', 380, 36.00, 22.00, 8.00, 1, 'mild', JSON_ARRAY('soy'), JSON_ARRAY('vegan','high_protein','low_calorie'), JSON_OBJECT('earthy',0.6));



-- -----------------------
-- 7) FOOD ITEM INGREDIENTS (junction)
-- For each item we attach 3-5 ingredients chosen from the ingredients table to ensure allergen coverage.
-- We'll create many rows mapping item -> ingredient_id.
-- -----------------------
INSERT INTO food_item_ingredients (fi_id, item_id, ingredient_id, removable, removal_effects)
VALUES
  -- Item 1 Classic Cheeseburger: beef, wheat bun, cheddar, lettuce, tomato
  (1, 1, 2, FALSE, JSON_OBJECT('effect','changes taste')),
  (2, 1, 9, FALSE, JSON_OBJECT('effect','bun removed')),
  (3, 1, 6, TRUE, JSON_OBJECT('effect','less dairy')),
  (4, 1, 5, TRUE, JSON_OBJECT('effect','less veg')),
  (5, 1, 4, TRUE, JSON_OBJECT('effect','less veg')),

  -- Item 2 Truffle Fries: potato, truffle oil (represented by olive_oil), parmesan
  (6, 2, 23, FALSE, JSON_OBJECT()),
  (7, 2, 12, TRUE, JSON_OBJECT()),
  (8, 2, 8, TRUE, JSON_OBJECT()),

  -- Item 3 Caesar Salad: lettuce, parmesan, croutons (wheat), egg (in dressing)
  (9, 3, 5, FALSE, JSON_OBJECT()),
  (10, 3, 8, TRUE, JSON_OBJECT()),
  (11, 3, 9, TRUE, JSON_OBJECT()),
  (12, 3, 10, TRUE, JSON_OBJECT()),

  -- Item 4 Chicken Wings: chicken, garlic, butter (in sauce)
  (13, 4, 1, FALSE, JSON_OBJECT()),
  (14, 4, 14, TRUE, JSON_OBJECT()),
  (15, 4, 13, TRUE, JSON_OBJECT()),

  -- Item 5 Margherita Pizza: wheat flour, mozzarella, tomato, basil
  (16, 5, 9, FALSE, JSON_OBJECT()),
  (17, 5, 7, FALSE, JSON_OBJECT()),
  (18, 5, 4, TRUE, JSON_OBJECT()),
  (19, 5, 21, TRUE, JSON_OBJECT()),

  -- Item 6 Spaghetti Alfredo: pasta noodles, cream, parmesan, garlic
  (20, 6, 27, FALSE, JSON_OBJECT()),
  (21, 6, 26, FALSE, JSON_OBJECT()),
  (22, 6, 8, TRUE, JSON_OBJECT()),
  (23, 6, 14, TRUE, JSON_OBJECT()),

  -- Item 7 Chicken Alfredo Pasta
  (24, 7, 1, FALSE, JSON_OBJECT()),
  (25, 7, 27, FALSE, JSON_OBJECT()),
  (26, 7, 26, FALSE, JSON_OBJECT()),
  (27, 7, 14, TRUE, JSON_OBJECT()),

  -- Item 8 Garden Salad
  (28, 8, 5, FALSE, JSON_OBJECT()),
  (29, 8, 4, TRUE, JSON_OBJECT()),
  (30, 8, 29, TRUE, JSON_OBJECT()),

  -- Item 9 Onion Rings
  (31, 9, 15, FALSE, JSON_OBJECT()),
  (32, 9, 9, TRUE, JSON_OBJECT()),

  -- Item 10 Chocolate Lava Cake
  (33, 10, 24, FALSE, JSON_OBJECT()),
  (34, 10, 25, FALSE, JSON_OBJECT()),
  (35, 10, 10, TRUE, JSON_OBJECT()),
  (36, 10, 11, TRUE, JSON_OBJECT()),

  -- Item 11 Grilled Chicken Sandwich
  (37, 11, 1, FALSE, JSON_OBJECT()),
  (38, 11, 9, FALSE, JSON_OBJECT()),
  (39, 11, 5, TRUE, JSON_OBJECT()),

  -- Item 12 BBQ Bacon Burger
  (40, 12, 2, FALSE, JSON_OBJECT()),
  (41, 12, 3, FALSE, JSON_OBJECT()),
  (42, 12, 9, FALSE, JSON_OBJECT()),
  (43, 12, 6, TRUE, JSON_OBJECT()),

  -- Item 13 Clam Chowder
  (44, 13, 23, FALSE, JSON_OBJECT()),
  (45, 13, 16, FALSE, JSON_OBJECT()),
  (46, 13, 11, TRUE, JSON_OBJECT()),

  -- Item 14 Veg Burger
  (47, 14, 22, FALSE, JSON_OBJECT()),
  (48, 14, 9, FALSE, JSON_OBJECT()),
  (49, 14, 5, TRUE, JSON_OBJECT()),

  -- Item 15 Garlic Bread
  (50, 15, 9, FALSE, JSON_OBJECT()),
  (51, 15, 13, TRUE, JSON_OBJECT()),
  (52, 15, 14, TRUE, JSON_OBJECT()),

  -- Item 16 Coca-Cola
  (53, 16, 24, FALSE, JSON_OBJECT()),

  -- Item 17 Fresh Fruit Salad
  (54, 17, 23, FALSE, JSON_OBJECT()),
  (55, 17, 4, TRUE, JSON_OBJECT()),
  (56, 17, 29, TRUE, JSON_OBJECT()),

  -- Item 18 Fish Tacos
  (57, 18, 17, FALSE, JSON_OBJECT()),
  (58, 18, 9, TRUE, JSON_OBJECT()),
  (59, 18, 4, TRUE, JSON_OBJECT()),

  -- Item 19 Mac & Cheese
  (60, 19, 9, FALSE, JSON_OBJECT()),
  (61, 19, 6, FALSE, JSON_OBJECT()),
  (62, 19, 11, TRUE, JSON_OBJECT()),

  -- Item 20 Vanilla Milkshake
  (63, 20, 11, FALSE, JSON_OBJECT()),
  (64, 20, 24, TRUE, JSON_OBJECT()),

  -- Restaurant 2 junctions items 21..35
  (65, 21, 4, FALSE, JSON_OBJECT()),   -- Greek Salad tomato
  (66, 21, 7, TRUE, JSON_OBJECT()),    -- feta/mozzarella analog
  (67, 21, 21, TRUE, JSON_OBJECT()),

  (68, 22, 9, FALSE, JSON_OBJECT()),   -- Avocado toast uses bread (wheat)
  (69, 22, 23, TRUE, JSON_OBJECT()),

  (70, 23, 27, FALSE, JSON_OBJECT()),  -- Quinoa bowl uses pasta/noodle-ish grain substitute as placeholder (pasta_noodles)
  (71, 23, 29, FALSE, JSON_OBJECT()),
  (72, 23, 12, TRUE, JSON_OBJECT()),

  (73, 24, 17, FALSE, JSON_OBJECT()),
  (74, 24, 12, TRUE, JSON_OBJECT()),
  (75, 24, 29, TRUE, JSON_OBJECT()),

  (76, 25, 22, FALSE, JSON_OBJECT()),
  (77, 25, 9, FALSE, JSON_OBJECT()),
  (78, 25, 12, TRUE, JSON_OBJECT()),

  (79, 26, 23, FALSE, JSON_OBJECT()),
  (80, 26, 12, TRUE, JSON_OBJECT()),

  (81, 27, 23, FALSE, JSON_OBJECT()),
  (82, 27, 14, TRUE, JSON_OBJECT()),

  (83, 28, 11, TRUE, JSON_OBJECT()),
  (84, 28, 24, TRUE, JSON_OBJECT()),

  (85, 29, 7, FALSE, JSON_OBJECT()),
  (86, 29, 4, TRUE, JSON_OBJECT()),

  (87, 30, 27, FALSE, JSON_OBJECT()),
  (88, 30, 21, TRUE, JSON_OBJECT()),
  (89, 30, 19, TRUE, JSON_OBJECT()),

  (90, 31, 20, FALSE, JSON_OBJECT()),
  (91, 31, 9, TRUE, JSON_OBJECT()),

  (92, 32, 25, FALSE, JSON_OBJECT()),
  (93, 32, 24, TRUE, JSON_OBJECT()),

  (94, 33, 7, FALSE, JSON_OBJECT()),
  (95, 33, 12, TRUE, JSON_OBJECT()),

  (96, 34, 24, FALSE, JSON_OBJECT()),

  (97, 35, 24, FALSE, JSON_OBJECT()),
  (98, 35, 9, TRUE, JSON_OBJECT()),
  (99, 35, 10, TRUE, JSON_OBJECT()),

  -- Restaurant 3 junctions items 36..45
  (100, 36, 28, FALSE, JSON_OBJECT()),
  (101, 36, 7, FALSE, JSON_OBJECT()),
  (102, 36, 9, FALSE, JSON_OBJECT()),

  (103, 37, 7, FALSE, JSON_OBJECT()),
  (104, 37, 8, TRUE, JSON_OBJECT()),
  (105, 37, 19, TRUE, JSON_OBJECT()),

  (106, 38, 27, FALSE, JSON_OBJECT()),
  (107, 38, 26, FALSE, JSON_OBJECT()),
  (108, 38, 8, TRUE, JSON_OBJECT()),

  (109, 39, 21, FALSE, JSON_OBJECT()),
  (110, 39, 19, TRUE, JSON_OBJECT()),
  (111, 39, 27, TRUE, JSON_OBJECT()),

  (112, 40, 9, FALSE, JSON_OBJECT()),
  (113, 40, 13, TRUE, JSON_OBJECT()),
  (114, 40, 8, TRUE, JSON_OBJECT()),

  (115, 41, 2, FALSE, JSON_OBJECT()),
  (116, 41, 6, TRUE, JSON_OBJECT()),
  (117, 41, 3, TRUE, JSON_OBJECT()),

  (118, 42, 10, FALSE, JSON_OBJECT()),
  (119, 42, 11, TRUE, JSON_OBJECT()),
  (120, 42, 24, TRUE, JSON_OBJECT()),

  (121, 43, 4, FALSE, JSON_OBJECT()),
  (122, 43, 21, TRUE, JSON_OBJECT()),
  (123, 43, 14, TRUE, JSON_OBJECT()),

  (124, 44, 9, FALSE, JSON_OBJECT()),
  (125, 44, 8, FALSE, JSON_OBJECT()),
  (126, 44, 3, TRUE, JSON_OBJECT()),

  (127, 45, 11, FALSE, JSON_OBJECT()),

  -- 46 Paneer Capsicum Tikka -> paneer (milk), capsicum, green_chili
  (128, 46, 31, FALSE, JSON_OBJECT()),
  (129, 46, 32, FALSE, JSON_OBJECT()),
  (130, 46, 45, TRUE, JSON_OBJECT('effect','less_spicy')),

  -- 47 Egg Bhurji (eggs only; ensure no dairy ingredient)
  (131, 47, 10, FALSE, JSON_OBJECT()),
  (132, 47, 15, TRUE, JSON_OBJECT()),

  -- 48 Artisan Cheese Platter -> cheeses, cashew (optional)
  (133, 48, 6, FALSE, JSON_OBJECT()),
  (134, 48, 7, TRUE, JSON_OBJECT()),
  (135, 48, 33, TRUE, JSON_OBJECT()),

  -- 49 Shrimp Schezwan -> shrimp + garlic removable + soy_sauce (soy/wheat)
  (136, 49, 16, FALSE, JSON_OBJECT()),
  (137, 49, 14, TRUE, JSON_OBJECT('effect','less_garlic')),
  (138, 49, 20, FALSE, JSON_OBJECT()),

  -- 50 Veg Cashew Biryani -> rice + biryani_spices + cashew (non-removable garnish)
  (139, 50, 34, FALSE, JSON_OBJECT()),
  (140, 50, 35, FALSE, JSON_OBJECT()),
  (141, 50, 33, FALSE, JSON_OBJECT()),

  -- 51 Mushroom Tandoori -> mushrooms NON-REMOVABLE
  (142, 51, 36, FALSE, JSON_OBJECT('note','mushroom integral to dish (non-removable)')),
  (143, 51, 21, TRUE, JSON_OBJECT()),

  -- 52 Oats Fruit Bowl (low fat) -> oats, fruit mix
  (144, 52, 38, FALSE, JSON_OBJECT()),
  (145, 52, 4, TRUE, JSON_OBJECT()),

  -- 53 Keto Chicken Salad -> chicken, avocado, olive_oil
  (146, 53, 1, FALSE, JSON_OBJECT()),
  (147, 53, 41, FALSE, JSON_OBJECT()),
  (148, 53, 12, TRUE, JSON_OBJECT()),

  -- 54 Chickpea Power Bowl -> chickpea + millet + quinoa
  (149, 54, 39, FALSE, JSON_OBJECT()),
  (150, 54, 43, FALSE, JSON_OBJECT()),
  (151, 54, 44, TRUE, JSON_OBJECT()),

  -- 55 Avocado Chocolate Mousse -> avocado + cocoa + jaggery (no milk)
  (152, 55, 41, FALSE, JSON_OBJECT()),
  (153, 55, 25, FALSE, JSON_OBJECT()),
  (154, 55, 42, TRUE, JSON_OBJECT()),

  -- 56 Kids Paneer Nuggets -> paneer + wheat + minimal spice (kid-friendly)
  (155, 56, 31, FALSE, JSON_OBJECT()),
  (156, 56, 9, FALSE, JSON_OBJECT()),

  -- 57 Aloo Chaat -> potato + street_spice_mix + tamarind (tamarind not in ingredients table; spice mix used)
  (157, 57, 23, FALSE, JSON_OBJECT()),
  (158, 57, 46, FALSE, JSON_OBJECT()),

  -- 58 Quick Egg Sandwich -> egg + wheat
  (159, 58, 10, FALSE, JSON_OBJECT()),
  (160, 58, 9, FALSE, JSON_OBJECT()),

  -- 59 Black Truffle Tagliolini -> pasta + cream + truffle (cream mapped to 'cream' ingredient 26)
  (161, 59, 27, FALSE, JSON_OBJECT()),
  (162, 59, 26, FALSE, JSON_OBJECT()),
  (163, 59, 21, TRUE, JSON_OBJECT()),

  -- 60 Coconut Mango Panna -> coconut_milk + mango (mango not in ingredients table; use coconut_milk)
  (164, 60, 40, FALSE, JSON_OBJECT()),
  (165, 60, 41, TRUE, JSON_OBJECT()),

  -- 61 Spicy Tangy Chicken Starter -> chicken + street_spice_mix + green_chili
  (166, 61, 1, FALSE, JSON_OBJECT()),
  (167, 61, 46, FALSE, JSON_OBJECT()),
  (168, 61, 45, TRUE, JSON_OBJECT()),

  -- 62 Vegan Protein Quinoa Bowl -> quinoa + tofu + chickpeas (tofu contains soy)
  (169, 62, 44, FALSE, JSON_OBJECT()),
  (170, 62, 37, FALSE, JSON_OBJECT()),
  (171, 62, 39, TRUE, JSON_OBJECT());

-- End of insertions

-- Optional: quick sanity-check selects (uncomment to verify)
-- SELECT COUNT(*) AS users_count FROM users;
-- SELECT COUNT(*) AS restaurants_count FROM restaurants;
-- SELECT COUNT(*) AS ingredients_count FROM ingredients;
-- SELECT COUNT(*) AS food_items_count FROM food_items;
-- SELECT COUNT(*) AS profiles_count FROM food_item_profiles;
-- SELECT COUNT(*) AS fi_junction_count FROM food_item_ingredients;