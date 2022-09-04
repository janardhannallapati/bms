package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Address;
import com.jana.bms.repository.AddressRepository;
import com.jana.bms.service.criteria.AddressCriteria;
import com.jana.bms.service.dto.AddressDTO;
import com.jana.bms.service.mapper.AddressMapper;
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
 * Service for executing complex queries for {@link Address} entities in the database.
 * The main input is a {@link AddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AddressDTO} or a {@link Page} of {@link AddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AddressQueryService extends QueryService<Address> {

    private final Logger log = LoggerFactory.getLogger(AddressQueryService.class);

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    public AddressQueryService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    /**
     * Return a {@link List} of {@link AddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AddressDTO> findByCriteria(AddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Address> specification = createSpecification(criteria);
        return addressMapper.toDto(addressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> findByCriteria(AddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Address> specification = createSpecification(criteria);
        return addressRepository.findAll(specification, page).map(addressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Address> specification = createSpecification(criteria);
        return addressRepository.count(specification);
    }

    /**
     * Function to convert {@link AddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Address> createSpecification(AddressCriteria criteria) {
        Specification<Address> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddressId(), Address_.addressId));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Address_.address));
            }
            if (criteria.getAddress2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress2(), Address_.address2));
            }
            if (criteria.getDistrict() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDistrict(), Address_.district));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Address_.postalCode));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Address_.phone));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Address_.location));
            }
            if (criteria.getCityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCityId(), root -> root.join(Address_.city, JoinType.LEFT).get(City_.cityId))
                    );
            }
        }
        return specification;
    }
}
