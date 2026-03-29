package com.buyPhone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private UUID id;
    private String name;

    private UUID parentId;

    private List<CategoryDTO> children;
}
