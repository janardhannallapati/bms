package com.jana.bms.repository;

import com.jana.bms.domain.Show;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ShowRepositoryWithBagRelationshipsImpl implements ShowRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Show> fetchBagRelationships(Optional<Show> show) {
        return show.map(this::fetchSeats);
    }

    @Override
    public Page<Show> fetchBagRelationships(Page<Show> shows) {
        return new PageImpl<>(fetchBagRelationships(shows.getContent()), shows.getPageable(), shows.getTotalElements());
    }

    @Override
    public List<Show> fetchBagRelationships(List<Show> shows) {
        return Optional.of(shows).map(this::fetchSeats).orElse(Collections.emptyList());
    }

    Show fetchSeats(Show result) {
        return entityManager
            .createQuery("select show from Show show left join fetch show.seats where show is :show", Show.class)
            .setParameter("show", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Show> fetchSeats(List<Show> shows) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, shows.size()).forEach(index -> order.put(shows.get(index).getShowId(), index));
        List<Show> result = entityManager
            .createQuery("select distinct show from Show show left join fetch show.seats where show in :shows", Show.class)
            .setParameter("shows", shows)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getShowId()), order.get(o2.getShowId())));
        return result;
    }
}
