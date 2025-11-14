package com.app.hungrify.main.template;

import com.app.hungrify.main.dto.menu.CreateFoodItemRequestDto;
import com.app.hungrify.main.dto.menu.CreateIngredientDto;
import com.app.hungrify.main.dto.menu.ParseIngredientsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromptTemplate {


    private final ChatService chatService;


    public String searchFood(String query) {
        return null;
    }

    public String searchRestaurant(String query) {
        return null;
    }

    public CreateFoodItemRequestDto addFood(String itemName, String itemShortDescription, String categoryName, List<String> presentIngredients) throws IOException {
        String ingredients = Files.readString(Paths.get("src/main/resources/ingredients"));
        String parseIngredients = Files.readString(Paths.get("src/main/resources/parse-ingredients"));
        String ingredientCsv = String.join(",", presentIngredients);
        String ingredientPrompt = ingredients
                .replace("<<<INPUT_ITEM_NAME>>>", itemName)
                .replace("<<<INPUT_SHORT_DESC>>>", itemShortDescription)
                .replace("<<<INPUT_CATEGORY_NAME>>>", categoryName);

        String ingredientsReponse = chatService.sendPrompt(ingredientPrompt);

        List<CreateIngredientDto> createIngredient = CreateIngredientDto.listFromJson(ingredientsReponse);

        List<String> predictedIngredients = createIngredient.stream().map(CreateIngredientDto::getName).toList();

        log.info("Original ingredients: {}", ingredientCsv);
        log.info("Predicted ingredients: {}", predictedIngredients);
        List<String> finalIngredients = getFinalMatchedIngredients(presentIngredients, predictedIngredients, 0.85);
        log.info("Final ingredients: {}", finalIngredients);
        String finalIngredientsCsv = String.join(",", finalIngredients);

        String prompt = parseIngredients
                .replace("<<<INPUT_ITEM_NAME>>>", itemName)
                .replace("<<<INPUT_SHORT_DESC>>>", itemShortDescription)
                .replace("<<<INPUT_CATEGORY_NAME>>>", categoryName)
                .replace("<<<INPUT_INGREDIENT_CSV>>>", finalIngredientsCsv);

        String response = chatService.sendPrompt(prompt);
        return CreateFoodItemRequestDto.fromJson(response);
    }


    public static List<String> getFinalMatchedIngredients(List<String> original, List<String> predicted, double threshold) {
        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
        List<String> finalList = new ArrayList<>();
        for (String pred : predicted) {
            String bestMatch = pred;
            double bestScore = threshold;
            for (String orig : original) {
                double score = similarity.apply(orig, pred);
                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = orig;
                }
            }
            finalList.add(bestMatch);
        }
        return finalList;
    }
}
