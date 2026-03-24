package com.buyPhone.repository;

import com.buyPhone.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository <Category, UUID>{

    @Query("SELECT DISTINCT c.name FROM Category c")
    List<String> getDistinctCategoryNames();
}
