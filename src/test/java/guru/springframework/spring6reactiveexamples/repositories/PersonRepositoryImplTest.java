package guru.springframework.spring6reactiveexamples.repositories;

import guru.springframework.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepository repository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = repository.findById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void testMonoByIdSubscriber() {
        Mono<Person> personMono = repository.findById(1);

        personMono.subscribe(person -> System.out.println(person.toString()));

    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = repository.findById(1);

        personMono
                .map(Person::getFirstName)
                .subscribe(System.out::println);

    }

    @Test
    void testFluxBlockFirst() {
        Flux<Person> personFlux = repository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = repository.findAll();
        personFlux.subscribe(System.out::println);
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = repository.findAll();
        personFlux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = repository.findAll();
        Mono<List<Person>> listMono = personFlux.collectList();
        listMono.subscribe(list -> {
            list.forEach(p -> {
                System.out.println(p.getFirstName());
            });
        });
    }

    @Test
    void testFilterOnName() {
        repository.findAll()
                .filter(p -> p.getFirstName().equals("Fiona"))
                .map(Person::getFirstName)
                .subscribe(System.out::println);
    }

    @Test
    void testGetById() {
        Mono<Person> fionaMono = repository.findAll().filter(p -> p.getFirstName().equals("Fiona"))
                .next();
        fionaMono.subscribe(person -> System.out.println(person.getFirstName()));
    }
}