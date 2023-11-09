package beerbrewers.brew;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Perhaps this class should be generalized and not exist for each Model.
 */
@Repository
public interface BrewRepository extends JpaRepository<Brew, Long> {
    Brew findByBrewId(long brewId);
    List<Brew> findAllByName(String name);
}
