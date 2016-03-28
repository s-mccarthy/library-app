package com.library.app.common.entities;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;

import com.library.app.dto.CategoryDTO;

@Ignore
public class CategoryDtoForDaoTest {

	public static CategoryDTO java() {
		return new CategoryDTO("Java");
	}

	public static CategoryDTO cleanCode() {
		return new CategoryDTO("Clean Code");
	}

	public static CategoryDTO architecture() {
		return new CategoryDTO("Architecture");
	}

	public static CategoryDTO networks() {
		return new CategoryDTO("Networks");
	}

	public static CategoryDTO categoryWithId(final CategoryDTO category, final Long id) {
		category.setId(id);
		return category;
	}

	public static List<CategoryDTO> allCategories() {
		return Arrays.asList(java(), cleanCode(), architecture(), networks());
	}

}