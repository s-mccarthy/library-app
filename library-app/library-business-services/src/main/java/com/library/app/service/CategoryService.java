package com.library.app.service;

import java.util.List;

import javax.ejb.Local;

import com.library.app.dto.CategoryDTO;
import com.library.app.exceptions.*;

@Local
public interface CategoryService {

	CategoryDTO add(CategoryDTO category) throws FieldNotValidException, CategoryAlreadyExistsException;

	void update(CategoryDTO category) throws FieldNotValidException, CategoryNotFoundException;

	CategoryDTO findById(Long id) throws CategoryNotFoundException;

	List<CategoryDTO> findAll();

}