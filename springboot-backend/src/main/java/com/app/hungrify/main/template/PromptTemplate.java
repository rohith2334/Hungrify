package com.app.hungrify.main.template;

import java.util.List;

public class PromptTemplate {

    public String searchFood(String query){
        return null;
    }

    public String searchRestaurant(String query){
        return null;
    }

    public String addFood(String foodName, List<String> searchIngredients, List<String> presentIngredients){
        return """
                You are an AI assistant helping a restaurant add a new food item to a food database.
                                
                ### GOAL
                Given:
                - foodName = {{FOOD_NAME}}
                - searchIngredients = {{SEARCH_INGREDIENTS}} (user typed ingredients)
                - presentIngredients = {{PRESENT_INGREDIENTS}} (ingredients already in DB)
                                
                Your tasks:
                1. Search the web or food knowledge (not real DB) to identify the **most essential / common ingredients** for this dish.
                   - NOT every tiny spice
                   - Just core preparation items
                2. Merge those essential ingredients with searchIngredients.
                3. Deduplicate.
                4. For each ingredient:
                   - ingredientName
                   - allergens list (only if included in: milk, eggs, fish, shellfish, tree_nuts, peanuts)
                   - isPresent = true if ingredient appears in presentIngredients, else false
                                
                ### RULES
                - Only return JSON. No explanation.
                - Keep ingredient names generic & clean.
                - Do NOT add irrelevant ingredients.
                - Keep list to essentials only (4–12 ingredients).
                - For allergens, check only:
                  ["milk","eggs","fish","shellfish","tree_nuts","peanuts"]
                - Exclude other categories.
                - If allergen doesn't exist, allergens must be `[]`.
                                
                ### OUTPUT FORMAT
                {
                  "ingredients": [
                    {
                      "ingredientName": "",
                      "allergens": [],
                      "isPresent": true/false
                    }
                  ]
                }
                                
                ### INPUT
                foodName: "{{FOOD_NAME}}"
                searchIngredients: {{SEARCH_INGREDIENTS}}
                presentIngredients: {{PRESENT_INGREDIENTS}}
                                
                """.formatted(foodName,searchIngredients,presentIngredients);
    }
}
