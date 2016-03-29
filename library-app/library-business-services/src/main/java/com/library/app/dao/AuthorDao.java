package com.library.app.dao;

import com.library.app.common.filtering.AuthorFilter;
import com.library.app.common.filtering.PaginatedData;
import com.library.app.dto.AuthorDTO;

public interface AuthorDao {

	PaginatedData<AuthorDTO> findByFilter(final AuthorFilter filter);

	boolean existsById(final long id);

	void update(final AuthorDTO author);

	AuthorDTO findById(final Long id);

	AuthorDTO add(final AuthorDTO author);

}
