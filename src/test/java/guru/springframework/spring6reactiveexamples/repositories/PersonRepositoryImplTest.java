package guru.springframework.spring6reactiveexamples.repositories;

import guru.springframework.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepository repository = new PersonRepositoryImpl();


    @Test
    void testFindByIdFound() {
        Mono<Person> personMono = repository.findById(3);

        assertTrue(personMono.hasElement().block());
    }

    @Test
    void testFindByIdFoundStepVerifier() {
        Mono<Person> personMono = repository.findById(3);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(System.out::println);
    }

    @Test
    void testFindByIdNotFound() {
        Mono<Person> personMono = repository.findById(8);

        assertFalse(personMono.hasElement().block());
    }

    @Test
    void testFindByIdNotFoundStepVerifier() {
        Mono<Person> personMono = repository.findById(8);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(System.out::println);
    }

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

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = repository.findAll();
        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).single()
                .doOnError(error -> {
                    System.out.println("Error occurred in Flux");
                    System.out.println(error.toString());
                });
        personMono.subscribe(System.out::println, throwable -> {
            System.out.println("Error occurred in Flux");
            System.out.println(throwable.toString());
        });
    }
}