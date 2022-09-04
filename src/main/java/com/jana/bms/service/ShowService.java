package com.jana.bms.service;

import com.jana.bms.domain.Show;
import com.jana.bms.repository.ShowRepository;
import com.jana.bms.service.dto.ShowDTO;
import com.jana.bms.service.mapper.ShowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Show}.
 */
@Service
@Transactional
public class ShowService {

    private final Logger log = LoggerFactory.getLogger(ShowService.class);

    private final ShowRepository showRepository;

    private final ShowMapper showMapper;

    public ShowService(ShowRepository showRepository, ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
    }

    /**
     * Save a show.
     *
     * @param showDTO the entity to save.
     * @return the persisted entity.
     */
    public ShowDTO save(ShowDTO showDTO) {
        log.debug("Request to save Show : {}", showDTO);
        Show show = showMapper.toEntity(showDTO);
        show = showRepository.save(show);
        return showMapper.toDto(show);
    }

    /**
     * Update a show.
     *
     * @param showDTO the entity to save.
     * @return the persisted entity.
     */
    public ShowDTO update(ShowDTO showDTO) {
        log.debug("Request to save Show : {}", showDTO);
        Show show = showMapper.toEntity(showDTO);
        show = showRepository.save(show);
        return showMapper.toDto(show);
    }

    /**
     * Partially update a show.
     *
     * @param showDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShowDTO> partialUpdate(ShowDTO showDTO) {
        log.debug("Request to partially update Show : {}", showDTO);

        return showRepository
            .findById(showDTO.getShowId())
            .map(existingShow -> {
                showMapper.partialUpdate(existingShow, showDTO);

                return existingShow;
            })
            .map(showRepository::save)
            .map(showMapper::toDto);
    }

    /**
     * Get all the shows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Shows");
        return showRepository.findAll(pageable).map(showMapper::toDto);
    }

    /**
     * Get all the shows with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShowDTO> findAllWithEagerRelationships(Pageable pageable) {
        return showRepository.findAllWithEagerRelationships(pageable).map(showMapper::toDto);
    }

    /**
     * Get one show by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShowDTO> findOne(Long id) {
        log.debug("Request to get Show : {}", id);
        return showRepository.findOneWithEagerRelationships(id).map(showMapper::toDto);
    }

    /**
     * Delete the show by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Show : {}", id);
        showRepository.deleteById(id);
    }
}
