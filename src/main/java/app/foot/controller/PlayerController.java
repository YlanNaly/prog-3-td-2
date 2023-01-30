package app.foot.controller;

import app.foot.controller.rest.Player;
import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class PlayerController {
    private final PlayerRestMapper mapper;
    private final PlayerService service;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return service.getPlayers().stream()
                .map(mapper::toRest).toList();
    }

    @PostMapping("/players")
    public List<Player> addPlayers(@RequestBody List<Player> toCreate) {
        List<app.foot.model.Player> domain = toCreate.stream()
                .map(mapper::toDomain).toList();
        return service.createPlayers(domain).stream()
                .map(mapper::toRest).toList();
    }

    @PutMapping("/players")
    public List<Player> changePlayer(
            @RequestBody List<Player> toChange
    ) {
        List<app.foot.model.Player> domain = toChange.stream()
                .map(mapper::toDomain).toList();
        return service.changePlayer(domain).stream()
                .map(mapper::toRest).toList();
    }
    //TODO: add PUT /players where you can modify the name and the guardian status of a player ---DONE---
    // Don't forget to add integration tests for this ---DONE---
}
