package com.eduardomallmann.studies.elasticsearch.users;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("user")
public class UserController {

    private UserRepository userRepository;
    private UserResourceAssembler resourceAssembler;

    public UserController(final UserRepository userRepository,
                          final UserResourceAssembler resourceAssembler) {
        this.userRepository = userRepository;
        this.resourceAssembler = resourceAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable("id") final String id) {
        return ResponseEntity.ok(this.resourceAssembler.toResource(this.userRepository.findById(id).orElseThrow()));
    }

    @GetMapping
    public ResponseEntity findAll(final Pageable pageable) {
        return ResponseEntity.ok(this.resourceAssembler.toResource(this.userRepository.findAll(pageable).get()
                                                                           .map(resourceAssembler::toResource)
                                                                           .collect(Collectors.toList())));
    }

    @PostMapping
    public ResponseEntity save(@RequestBody final User user) {
        if (null == user.getId()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                           .body(this.resourceAssembler.toResource(this.userRepository.save(user)));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/all")
    public ResponseEntity saveAll(@RequestBody final List<User> users) {
        if (users.stream().allMatch(user -> null == user.getId())) {
            return ResponseEntity.status(HttpStatus.CREATED)
                           .body(this.resourceAssembler.toResource(
                                   StreamSupport.stream(this.userRepository.saveAll(users).spliterator(), false)
                                           .map(resourceAssembler::toResource)
                                           .collect(Collectors.toList())));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") final String id,
                                 @RequestBody final User user) {
        if (id.equals(user.getId())) {
            try {
                this.userRepository.findById(id).orElseThrow();
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.resourceAssembler.toResource(this.userRepository.save(user)));
    }

    @PutMapping
    public ResponseEntity updateAll(@RequestBody final List<User> users) {
        if (users.stream().allMatch(user -> null != user.getId())) {
            return ResponseEntity.ok(this.resourceAssembler.toResource(
                    StreamSupport.stream(this.userRepository.saveAll(users).spliterator(), false)
                            .map(resourceAssembler::toResource)
                            .collect(Collectors.toList())));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") final String id) {
        try {
            this.userRepository.delete(this.userRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        this.userRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
