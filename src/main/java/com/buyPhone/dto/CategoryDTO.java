package com.buyPhone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private String id;
    private String name;

    private String parentId;

    private List<CategoryDTO> children;
}
