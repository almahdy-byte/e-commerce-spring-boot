package com.nazer.e_commerce.category.schema;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {

    @Id
    @Getter(AccessLevel.NONE)
    private ObjectId id;

    @JsonIgnore
    public ObjectId getId() {
        return id;
    }

    @JsonProperty("_id")
    public String get_id() {
        return id != null ? id.toHexString() : null;
    }

    @NotBlank
    @Indexed(unique = true)
    private String categoryName;

    @Indexed(unique = true)
    private String slug;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JsonIgnore
    private ObjectId createdBy;

    @JsonIgnore
    public ObjectId getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public String getCreatedByHex() {
        return createdBy != null ? createdBy.toHexString() : null;
    }
    
}
