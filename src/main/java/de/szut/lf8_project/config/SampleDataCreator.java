package de.szut.lf8_project.config;

import de.szut.lf8_project.entities.HelloEntity;
import de.szut.lf8_project.repositories.HelloRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleDataCreator implements ApplicationRunner {

    private HelloRepository repository;

    public SampleDataCreator(HelloRepository repository) {
        this.repository = repository;
    }

    public void run(ApplicationArguments args) {
        repository.save(new HelloEntity("Hallo Welt!"));
        repository.save(new HelloEntity("Schöner Tag heute"));
        repository.save(new HelloEntity("FooBar"));

    }

}
