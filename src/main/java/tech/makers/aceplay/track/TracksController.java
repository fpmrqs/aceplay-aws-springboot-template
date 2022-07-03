package tech.makers.aceplay.track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class TracksController {
  @Autowired private TrackRepository trackRepository;

  @GetMapping("/api/tracks")
  public Iterable<Track> index() {
    return trackRepository.findAll();
  }

  @PostMapping("/api/tracks")
  public Track create(@RequestBody Track track) {
    return trackRepository.save(track);
  }

  @PatchMapping("/api/tracks/{id}")
  public Track update(@PathVariable Long id, @RequestBody Track newTrack) {
    Track track = trackRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No track exists with id " + id));
    track.setTitle(newTrack.getTitle());
    track.setArtist(newTrack.getArtist());
    trackRepository.save(track);
    return track;
  }

  @DeleteMapping("/api/tracks/{id}")
  public void delete(@PathVariable Long id) {
    Track track = trackRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No track exists with id " + id));
    trackRepository.delete(track);
  }
}
