package com.jana.bms.service;

import com.jana.bms.domain.ShowSeat;
import com.jana.bms.repository.ShowSeatRepository;
import com.jana.bms.service.dto.ShowSeatDTO;
import com.jana.bms.service.mapper.ShowSeatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShowSeat}.
 */
@Service
@Transactional
public class ShowSeatService {

    private final Logger log = LoggerFactory.getLogger(ShowSeatService.class);

    private final ShowSeatRepository showSeatRepository;

    private final ShowSeatMapper showSeatMapper;

    public ShowSeatService(ShowSeatRepository showSeatRepository, ShowSeatMapper showSeatMapper) {
        this.showSeatRepository = showSeatRepository;
        this.showSeatMapper = showSeatMapper;
    }

    /**
     * Save a showSeat.
     *
     * @param showSeatDTO the entity to save.
     * @return the persisted entity.
     */
    public ShowSeatDTO save(ShowSeatDTO showSeatDTO) {
        log.debug("Request to save ShowSeat : {}", showSeatDTO);
        ShowSeat showSeat = showSeatMapper.toEntity(showSeatDTO);
        showSeat = showSeatRepository.save(showSeat);
        return showSeatMapper.toDto(showSeat);
    }

    /**
     * Update a showSeat.
     *
     * @param showSeatDTO the entity to save.
     * @return the persisted entity.
     */
    public ShowSeatDTO update(ShowSeatDTO showSeatDTO) {
        log.debug("Request to save ShowSeat : {}", showSeatDTO);
        ShowSeat showSeat = showSeatMapper.toEntity(showSeatDTO);
        showSeat = showSeatRepository.save(showSeat);
        return showSeatMapper.toDto(showSeat);
    }

    /**
     * Partially update a showSeat.
     *
     * @param showSeatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShowSeatDTO> partialUpdate(ShowSeatDTO showSeatDTO) {
        log.debug("Request to partially update ShowSeat : {}", showSeatDTO);

        return showSeatRepository
            .findById(showSeatDTO.getShowSeatId())
            .map(existingShowSeat -> {
                showSeatMapper.partialUpdate(existingShowSeat, showSeatDTO);

                return existingShowSeat;
            })
            .map(showSeatRepository::save)
            .map(showSeatMapper::toDto);
    }

    /**
     * Get all the showSeats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShowSeatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShowSeats");
        return showSeatRepository.findAll(pageable).map(showSeatMapper::toDto);
    }

    /**
     * Get one showSeat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShowSeatDTO> findOne(Long id) {
        log.debug("Request to get ShowSeat : {}", id);
        return showSeatRepository.findById(id).map(showSeatMapper::toDto);
    }

    /**
     * Delete the showSeat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShowSeat : {}", id);
        showSeatRepository.deleteById(id);
    }
}
