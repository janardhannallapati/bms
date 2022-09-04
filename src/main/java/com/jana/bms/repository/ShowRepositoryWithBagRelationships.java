package com.jana.bms.repository;

import com.jana.bms.domain.Show;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ShowRepositoryWithBagRelationships {
    Optional<Show> fetchBagRelationships(Optional<Show> show);

    List<Show> fetchBagRelationships(List<Show> shows);

    Page<Show> fetchBagRelationships(Page<Show> shows);
}
