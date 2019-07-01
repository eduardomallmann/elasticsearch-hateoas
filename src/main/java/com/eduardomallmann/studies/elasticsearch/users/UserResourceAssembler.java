package com.eduardomallmann.studies.elasticsearch.users;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    /**
     * Converts the given entity into an {@link ResourceSupport}.
     *
     * @param user entity to be converted
     *
     * @return {@link Resource} of {@link User}
     */
    @Override
    public Resource<User> toResource(final User user) {
        return new Resource<>(user,
                linkTo(methodOn(UserController.class).findOne(user.getId())).withSelfRel().withType(HttpMethod.GET.name()),
                linkTo(methodOn(UserController.class).save(user)).withSelfRel().withType(HttpMethod.POST.name()),
                linkTo(methodOn(UserController.class).update(user.getId(), user)).withSelfRel().withType(HttpMethod.PUT.name()),
                linkTo(methodOn(UserController.class).delete(user.getId())).withSelfRel().withType(HttpMethod.DELETE.name()));
    }

    /**
     * Converts the given list of entities into an {@link ResourceSupport}.
     *
     * @param users list of entities to be converted
     *
     * @return {@link Resources} of {@link Resource<User>}
     */
    public Resources<Resource<User>> toResource(final Iterable<Resource<User>> users) {
        return new Resources<>(StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList()),
                linkTo(methodOn(UserController.class).findAll(null)).withSelfRel().withType(HttpMethod.GET.name()),
                linkTo(methodOn(UserController.class).saveAll(null)).withSelfRel().withType(HttpMethod.POST.name()),
                linkTo(methodOn(UserController.class).updateAll(null)).withSelfRel().withType(HttpMethod.PUT.name()),
                linkTo(methodOn(UserController.class).deleteAll()).withSelfRel().withType(HttpMethod.DELETE.name()));
    }
}
