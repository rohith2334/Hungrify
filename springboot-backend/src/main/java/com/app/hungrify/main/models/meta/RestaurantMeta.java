package com.app.hungrify.main.models.meta;
// src/main/java/com/app/hungrify/main/models/RestaurantMeta.java
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class RestaurantMeta {
    private List<Object> documents;
    private Map<String, Object> ratingSummary;
    private List<Object> openHours;
    private String adminNote;
    private Map<String, Boolean> attributes;
    private List<Object> specialties;
    private List<String> tags;

    // attributes
    private Boolean isPureVeg;
    private Boolean isVeganFriendly;
    private Boolean isGlutenFreeFriendly;
    private Boolean isHalalCertified;
    private Boolean isCloudKitchen;
    private Boolean isOrganicIngredients;
    private Boolean isNutFreeFriendly;

}