package com.library.app.dao;

import java.util.List;

import com.library.app.dto.CategoryDTO;

public interface CategoryDao {

	boolean existsById(final Long id);

	boolean alreadyExists(final CategoryDTO category);

	List<CategoryDTO> findAll(final String orderField);

	void update(final CategoryDTO category);

	CategoryDTO findById(final Long id);

	CategoryDTO add(final CategoryDTO category);

}
