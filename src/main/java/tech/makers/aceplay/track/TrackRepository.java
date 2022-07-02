package tech.makers.aceplay.track;

import org.springframework.data.repository.CrudRepository;

public interface TrackRepository extends CrudRepository<Track, Long> {
  Track findFirstByOrderByIdAsc();
}
