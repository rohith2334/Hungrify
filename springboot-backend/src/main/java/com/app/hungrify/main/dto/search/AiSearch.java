package com.app.hungrify.main.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AiSearch {
    private String type;
    private String sqlQuery;


}
