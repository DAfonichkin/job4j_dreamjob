package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private AtomicInteger nextId = new AtomicInteger(1);

    private final ConcurrentHashMap<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Ivan", "Learning java for 1 month", LocalDateTime.now(), 1));
        save(new Candidate(0, "Petr", "Learning java for 6 month", LocalDateTime.now(), 2));
        save(new Candidate(0, "Alex", "Learning java for 1 year", LocalDateTime.now(), 3));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldVacancy) ->
                new Candidate(oldVacancy.getId(), candidate.getName(),
                        candidate.getDescription(), candidate.getCreationDate(), candidate.getCityId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }

}