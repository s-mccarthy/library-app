package com.library.app.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.*;

import com.library.app.dao.CategoryDao;
import com.library.app.dto.CategoryDTO;
import com.library.app.entities.Category;

@Stateless
public class CategoryDaoImpl implements CategoryDao {

	@PersistenceContext
	public EntityManager em;

	@Override
	public CategoryDTO add(final CategoryDTO categoryDto) {
		Category category = new Category(categoryDto);
		em.persist(category);

		return category.getCategory();
	}

	@Override
	public CategoryDTO findById(final Long id) {
		if (id == null)
			return null;

		Category category = em.find(Category.class, id);

		return null == category ? null : category.getCategory();
	}

	@Override
	public void update(final CategoryDTO categoryDto) {
		Category category = new Category(categoryDto);
		em.merge(category);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CategoryDTO> findAll(final String orderField) {
		List<Category> result = em.createQuery("Select e From Category e Order by e." + orderField).getResultList();
		if (result.isEmpty())
			return null;

		List<CategoryDTO> returnedResult = new ArrayList<>();
		for (Category category : result) {
			returnedResult.add(category.getCategory());
		}

		return returnedResult;
	}

	@Override
	public boolean alreadyExists(final CategoryDTO category) {
		final StringBuilder jpql = new StringBuilder();
		jpql.append("Select 1 From Category e where e.name = :name");
		if (category.getId() != null) {
			jpql.append(" And e.id != :id");
		}

		final Query query = em.createQuery(jpql.toString());
		query.setParameter("name", category.getName());
		if (category.getId() != null) {
			query.setParameter("id", category.getId());
		}

		return query.setMaxResults(1).getResultList().size() > 0;
	}

	@Override
	public boolean existsById(final Long id) {
		return em.createQuery("Select 1 From Category e where e.id = :id").setParameter("id", id).setMaxResults(1)
				.getResultList().size() > 0;
	}

}