package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Movie;
import com.jana.bms.repository.MovieRepository;
import com.jana.bms.service.criteria.MovieCriteria;
import com.jana.bms.service.dto.MovieDTO;
import com.jana.bms.service.mapper.MovieMapper;
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
 * Service for executing complex queries for {@link Movie} entities in the database.
 * The main input is a {@link MovieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MovieDTO} or a {@link Page} of {@link MovieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovieQueryService extends QueryService<Movie> {

    private final Logger log = LoggerFactory.getLogger(MovieQueryService.class);

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    public MovieQueryService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    /**
     * Return a {@link List} of {@link MovieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MovieDTO> findByCriteria(MovieCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Movie> specification = createSpecification(criteria);
        return movieMapper.toDto(movieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MovieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovieDTO> findByCriteria(MovieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.findAll(specification, page).map(movieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovieCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.count(specification);
    }

    /**
     * Function to convert {@link MovieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Movie> createSpecification(MovieCriteria criteria) {
        Specification<Movie> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getMovieId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMovieId(), Movie_.movieId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Movie_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Movie_.description));
            }
            if (criteria.getReleaseYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReleaseYear(), Movie_.releaseYear));
            }
            if (criteria.getLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLength(), Movie_.length));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildSpecification(criteria.getRating(), Movie_.rating));
            }
            if (criteria.getLanguageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLanguageId(),
                            root -> root.join(Movie_.language, JoinType.LEFT).get(Language_.languageId)
                        )
                    );
            }
            if (criteria.getActorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getActorId(), root -> root.join(Movie_.actors, JoinType.LEFT).get(Actor_.actorId))
                    );
            }
            if (criteria.getGenreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGenreId(), root -> root.join(Movie_.genres, JoinType.LEFT).get(Genre_.genreId))
                    );
            }
        }
        return specification;
    }
}
