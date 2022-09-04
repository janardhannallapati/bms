package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Genre;
import com.jana.bms.repository.GenreRepository;
import com.jana.bms.service.criteria.GenreCriteria;
import com.jana.bms.service.dto.GenreDTO;
import com.jana.bms.service.mapper.GenreMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Genre} entities in the database.
 * The main input is a {@link GenreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenreDTO} or a {@link Page} of {@link GenreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenreQueryService extends QueryService<Genre> {

    private final Logger log = LoggerFactory.getLogger(GenreQueryService.class);

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public GenreQueryService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    /**
     * Return a {@link List} of {@link GenreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenreDTO> findByCriteria(GenreCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Genre> specification = createSpecification(criteria);
        return genreMapper.toDto(genreRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenreDTO> findByCriteria(GenreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Genre> specification = createSpecification(criteria);
        return genreRepository.findAll(specification, page).map(genreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenreCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Genre> specification = createSpecification(criteria);
        return genreRepository.count(specification);
    }

    /**
     * Function to convert {@link GenreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Genre> createSpecification(GenreCriteria criteria) {
        Specification<Genre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getGenreId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGenreId(), Genre_.genreId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Genre_.name));
            }
            if (criteria.getMovieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMovieId(), root -> root.join(Genre_.movies, JoinType.LEFT).get(Movie_.movieId))
                    );
            }
        }
        return specification;
    }
}
