package com.nazer.e_commerce.category.Dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    @JsonProperty("_id")
    private String id;
    private String categoryName;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddedBy addedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddedBy {
        private String name;
    }
}
