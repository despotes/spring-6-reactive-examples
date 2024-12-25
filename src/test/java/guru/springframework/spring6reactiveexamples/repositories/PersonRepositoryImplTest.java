package guru.springframework.spring6reactiveexamples.repositories;

import guru.springframework.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

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


}