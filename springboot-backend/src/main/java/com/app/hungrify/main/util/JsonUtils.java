package com.app.hungrify.main.util;

// package com.hungrify.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.time.OffsetDateTime;

/**
 * Small helpers to read/write users.profile_json safely.
 * We use Jackson JsonNode to avoid mismatches between DB JSON storage and Java objects.
 */
public class JsonUtils {
    public static final ObjectMapper M = new ObjectMapper();

    /**
     * Ensures a cart object exists under profileJson and returns it (mutable).
     */
    public static ObjectNode ensureCart(ObjectNode profileJson) {
        if (profileJson == null) {
            profileJson = M.createObjectNode();
        }
        JsonNode cartNode = profileJson.get("cart");
        if (cartNode == null || cartNode.isNull() || !cartNode.isObject()) {
            ObjectNode cart = M.createObjectNode();
            cart.put("restaurant_id", (Long) null);
            cart.set("items", M.createArrayNode());
            cart.put("subtotal", 0.0);
            cart.put("delivery_fee", 0.0);
            cart.put("taxes", 0.0);
            cart.put("total", 0.0);
            cart.put("saved_at", OffsetDateTime.now().toString());
            profileJson.set("cart", cart);
            return cart;
        } else {
            return (ObjectNode) cartNode;
        }
    }

    /** Convert POJO to JsonNode */
    public static JsonNode toJsonNode(Object obj) {
        return M.valueToTree(obj);
    }

    /** Convert JsonNode to POJO of target class */
    public static <T> T fromJsonNode(JsonNode node, Class<T> clazz) {
        return M.convertValue(node, clazz);
    }
}