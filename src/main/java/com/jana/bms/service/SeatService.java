package com.jana.bms.service;

import com.jana.bms.domain.Seat;
import com.jana.bms.repository.SeatRepository;
import com.jana.bms.service.dto.SeatDTO;
import com.jana.bms.service.mapper.SeatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Seat}.
 */
@Service
@Transactional
public class SeatService {

    private final Logger log = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;

    private final SeatMapper seatMapper;

    public SeatService(SeatRepository seatRepository, SeatMapper seatMapper) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
    }

    /**
     * Save a seat.
     *
     * @param seatDTO the entity to save.
     * @return the persisted entity.
     */
    public SeatDTO save(SeatDTO seatDTO) {
        log.debug("Request to save Seat : {}", seatDTO);
        Seat seat = seatMapper.toEntity(seatDTO);
        seat = seatRepository.save(seat);
        return seatMapper.toDto(seat);
    }

    /**
     * Update a seat.
     *
     * @param seatDTO the entity to save.
     * @return the persisted entity.
     */
    public SeatDTO update(SeatDTO seatDTO) {
        log.debug("Request to save Seat : {}", seatDTO);
        Seat seat = seatMapper.toEntity(seatDTO);
        seat = seatRepository.save(seat);
        return seatMapper.toDto(seat);
    }

    /**
     * Partially update a seat.
     *
     * @param seatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SeatDTO> partialUpdate(SeatDTO seatDTO) {
        log.debug("Request to partially update Seat : {}", seatDTO);

        return seatRepository
            .findById(seatDTO.getSeatId())
            .map(existingSeat -> {
                seatMapper.partialUpdate(existingSeat, seatDTO);

                return existingSeat;
            })
            .map(seatRepository::save)
            .map(seatMapper::toDto);
    }

    /**
     * Get all the seats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SeatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Seats");
        return seatRepository.findAll(pageable).map(seatMapper::toDto);
    }

    /**
     * Get one seat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SeatDTO> findOne(Long id) {
        log.debug("Request to get Seat : {}", id);
        return seatRepository.findById(id).map(seatMapper::toDto);
    }

    /**
     * Delete the seat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Seat : {}", id);
        seatRepository.deleteById(id);
    }
}
