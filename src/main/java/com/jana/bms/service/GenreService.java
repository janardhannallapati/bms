package com.jana.bms.service;

import com.jana.bms.domain.Genre;
import com.jana.bms.repository.GenreRepository;
import com.jana.bms.service.dto.GenreDTO;
import com.jana.bms.service.mapper.GenreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Genre}.
 */
@Service
@Transactional
public class GenreService {

    private final Logger log = LoggerFactory.getLogger(GenreService.class);

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    /**
     * Save a genre.
     *
     * @param genreDTO the entity to save.
     * @return the persisted entity.
     */
    public GenreDTO save(GenreDTO genreDTO) {
        log.debug("Request to save Genre : {}", genreDTO);
        Genre genre = genreMapper.toEntity(genreDTO);
        genre = genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    /**
     * Update a genre.
     *
     * @param genreDTO the entity to save.
     * @return the persisted entity.
     */
    public GenreDTO update(GenreDTO genreDTO) {
        log.debug("Request to save Genre : {}", genreDTO);
        Genre genre = genreMapper.toEntity(genreDTO);
        genre = genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    /**
     * Partially update a genre.
     *
     * @param genreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GenreDTO> partialUpdate(GenreDTO genreDTO) {
        log.debug("Request to partially update Genre : {}", genreDTO);

        return genreRepository
            .findById(genreDTO.getGenreId())
            .map(existingGenre -> {
                genreMapper.partialUpdate(existingGenre, genreDTO);

                return existingGenre;
            })
            .map(genreRepository::save)
            .map(genreMapper::toDto);
    }

    /**
     * Get all the genres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GenreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Genres");
        return genreRepository.findAll(pageable).map(genreMapper::toDto);
    }

    /**
     * Get one genre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GenreDTO> findOne(Long id) {
        log.debug("Request to get Genre : {}", id);
        return genreRepository.findById(id).map(genreMapper::toDto);
    }

    /**
     * Delete the genre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        genreRepository.deleteById(id);
    }
}
