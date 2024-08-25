package co.edu.escuelaing.controller.user;

import co.edu.escuelaing.exception.UserNotFoundException;
import co.edu.escuelaing.repository.user.User;
import co.edu.escuelaing.repository.user.UserDto;
import co.edu.escuelaing.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
public class UsersController {

    private final UsersService usersService;

    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = usersService.save(new User(userDto));
        URI createdUserUri = URI.create("/v1/users/" + user.getId());
        return ResponseEntity.created(createdUserUri).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usersService.all();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        Optional<User> user = usersService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        Optional<User> existingUser = usersService.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.update(userDto);
            usersService.save(user); // Asegúrate de que se llama a save
            return ResponseEntity.ok(user);
        } else {
            throw new UserNotFoundException(id); // Se lanzó UserNotFoundException con ID como parámetro
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        Optional<User> existingUser = usersService.findById(id);
        if (existingUser.isPresent()) {
            usersService.deleteById(id); // Asegúrate de que se llama a deleteById
            return ResponseEntity.ok().build();
        } else {
            throw new UserNotFoundException(id); // Se lanzó UserNotFoundException con ID como parámetro
        }
    }

}
