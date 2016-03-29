package com.library.app.dao.impl;

import java.util.*;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.*;

import com.library.app.common.filtering.AuthorFilter;
import com.library.app.common.filtering.PaginatedData;
import com.library.app.dao.AuthorDao;
import com.library.app.dto.AuthorDTO;
import com.library.app.entities.Author;

@Stateless
public class AuthorDaoImpl implements AuthorDao {

	@PersistenceContext
	public EntityManager em;

	@Override
	public AuthorDTO add(final AuthorDTO authorDto) {
		Author author = new Author(authorDto);
		em.persist(author);

		return author.getAuthorDto();
	}

	@Override
	public AuthorDTO findById(final Long id) {
		if (id == null)
			return null;

		Author author = em.find(Author.class, id);

		return null == author ? null : author.getAuthorDto();
	}

	@Override
	public void update(final AuthorDTO authorDto) {
		Author author = new Author(authorDto);
		em.merge(author);
	}

	@Override
	public boolean existsById(final long id) {
		return em.createQuery("Select 1 From Author e where e.id = :id").setParameter("id", id).setMaxResults(1)
				.getResultList().size() > 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PaginatedData<AuthorDTO> findByFilter(final AuthorFilter filter) {
		final StringBuilder clause = new StringBuilder("WHERE e.id is not null");
		final Map<String, Object> queryParameters = new HashMap<>();
		if (filter.getName() != null) {
			clause.append(" And UPPER(e.name) Like UPPER(:name)");
			queryParameters.put("name", "%" + filter.getName() + "%");
		}

		final StringBuilder clauseSort = new StringBuilder();
		if (filter.hasOrderField()) {
			clauseSort.append("Order by e." + filter.getPaginationData().getOrderField());
			clauseSort.append(filter.getPaginationData().isAscending() ? " ASC" : " DESC");
		} else {
			clauseSort.append("Order by e.name ASC");
		}

		final Query queryAuthors = em
				.createQuery("Select e From Author e " + clause.toString() + " " + clauseSort.toString());
		applyQueryParametersOnQuery(queryParameters, queryAuthors);
		if (filter.hasPaginationData()) {
			queryAuthors.setFirstResult(filter.getPaginationData().getFirstResult());
			queryAuthors.setMaxResults(filter.getPaginationData().getMaxResults());
		}

		final List<Author> authors = queryAuthors.getResultList();

		List<AuthorDTO> returnedAuthors = new ArrayList<>();
		for (Author author : authors) {
			returnedAuthors.add(author.getAuthorDto());
		}

		final Query queryCount = em.createQuery("Select Count(e) From Author e " + clause.toString());
		applyQueryParametersOnQuery(queryParameters, queryCount);
		final Integer count = ((Long) queryCount.getSingleResult()).intValue();

		return new PaginatedData<AuthorDTO>(count, returnedAuthors);
	}

	private void applyQueryParametersOnQuery(final Map<String, Object> queryParameters, final Query query) {
		for (final Entry<String, Object> entryMap : queryParameters.entrySet()) {
			query.setParameter(entryMap.getKey(), entryMap.getValue());
		}
	}

}