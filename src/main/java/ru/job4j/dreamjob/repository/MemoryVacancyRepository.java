package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {

    private AtomicInteger nextId = new AtomicInteger(1);

    private final ConcurrentHashMap<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        LocalDateTime now = LocalDateTime.now();
        save(new Vacancy(0, "Intern Java Developer", "Стажер Java разработчик", now, true, 1, 0));
        save(new Vacancy(0, "Junior Java Developer", "Младший Java разработчик", now, true, 1, 0));
        save(new Vacancy(0, "Junior+ Java Developer", "Java разработчик", now, true, 2, 0));
        save(new Vacancy(0, "Middle Java Developer", "Старший Java разработчик", now, true, 2, 0));
        save(new Vacancy(0, "Middle+ Java Developer", "Ведущий Java разработчик", now, true, 2, 0));
        save(new Vacancy(0, "Senior Java Developer", "Главный Java разработчик", now, true, 3, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) ->
                new Vacancy(oldVacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        vacancy.getCreationDate(),
                        vacancy.getVisible(),
                        vacancy.getCityId(),
                        vacancy.getFileId())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }

}