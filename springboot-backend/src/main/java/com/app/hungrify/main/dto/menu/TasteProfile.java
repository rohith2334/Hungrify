package com.app.hungrify.main.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TasteProfile {

    private Boolean spicy;        // true if food has noticeable heat
    private Boolean sweet;        // sugary taste
    private Boolean sour;         // acidic/fruity taste
    private Boolean salty;        // savory-saltiness
    private Boolean bitter;       // coffee/chocolate bitterness
    private Boolean umami;        // savory depth (meaty/soy)
    private Boolean smoky;        // grilled/charred flavor
    private Boolean creamy;       // dairy-cream based
    private Boolean tangy;        // citrus/vinegar zing
    private Boolean crunchy;      // texture
    private Boolean soft;         // texture
    private Boolean juicy;        // moistness
    private Boolean rich;         // heavy + flavorful
    private Boolean mild;

    // gentle flavor for kids, etc.

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("sweet", this.getSweet());
        map.put("sour", this.getSour());
        map.put("spicy", this.getSpicy());
        map.put("salty", this.getSalty());
        map.put("bitter", this.getBitter());
        map.put("umami", this.getUmami());
        map.put("smoky", this.getSmoky());
        map.put("creamy", this.getCreamy());
        map.put("tangy", this.getTangy());
        map.put("crunchy", this.getCrunchy());
        map.put("soft", this.getSoft());
        map.put("juicy", this.getJuicy());
        map.put("rich", this.getRich());
        map.put("mild", this.getMild());
        return map;
    }
}
