package com.buyPhone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductAdminDTO extends ProductDetailDTO {
    // Admins need to see the full inventory object
    private InventoryDTO inventory;
    private CategoryDTO category;

}
