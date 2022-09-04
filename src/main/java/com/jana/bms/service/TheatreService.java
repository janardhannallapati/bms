package com.jana.bms.service;

import com.jana.bms.domain.Theatre;
import com.jana.bms.repository.TheatreRepository;
import com.jana.bms.service.dto.TheatreDTO;
import com.jana.bms.service.mapper.TheatreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Theatre}.
 */
@Service
@Transactional
public class TheatreService {

    private final Logger log = LoggerFactory.getLogger(TheatreService.class);

    private final TheatreRepository theatreRepository;

    private final TheatreMapper theatreMapper;

    public TheatreService(TheatreRepository theatreRepository, TheatreMapper theatreMapper) {
        this.theatreRepository = theatreRepository;
        this.theatreMapper = theatreMapper;
    }

    /**
     * Save a theatre.
     *
     * @param theatreDTO the entity to save.
     * @return the persisted entity.
     */
    public TheatreDTO save(TheatreDTO theatreDTO) {
        log.debug("Request to save Theatre : {}", theatreDTO);
        Theatre theatre = theatreMapper.toEntity(theatreDTO);
        theatre = theatreRepository.save(theatre);
        return theatreMapper.toDto(theatre);
    }

    /**
     * Update a theatre.
     *
     * @param theatreDTO the entity to save.
     * @return the persisted entity.
     */
    public TheatreDTO update(TheatreDTO theatreDTO) {
        log.debug("Request to save Theatre : {}", theatreDTO);
        Theatre theatre = theatreMapper.toEntity(theatreDTO);
        theatre = theatreRepository.save(theatre);
        return theatreMapper.toDto(theatre);
    }

    /**
     * Partially update a theatre.
     *
     * @param theatreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TheatreDTO> partialUpdate(TheatreDTO theatreDTO) {
        log.debug("Request to partially update Theatre : {}", theatreDTO);

        return theatreRepository
            .findById(theatreDTO.getTheatreId())
            .map(existingTheatre -> {
                theatreMapper.partialUpdate(existingTheatre, theatreDTO);

                return existingTheatre;
            })
            .map(theatreRepository::save)
            .map(theatreMapper::toDto);
    }

    /**
     * Get all the theatres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TheatreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Theatres");
        return theatreRepository.findAll(pageable).map(theatreMapper::toDto);
    }

    /**
     * Get one theatre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TheatreDTO> findOne(Long id) {
        log.debug("Request to get Theatre : {}", id);
        return theatreRepository.findById(id).map(theatreMapper::toDto);
    }

    /**
     * Delete the theatre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Theatre : {}", id);
        theatreRepository.deleteById(id);
    }
}
