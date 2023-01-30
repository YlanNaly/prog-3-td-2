package app.foot.controller;

import app.foot.controller.rest.Player;
import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.model.PostPlayer;
import app.foot.repository.entity.PlayerEntity;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
//TODO: add POST /players and add integration test ok and ko

@RestController
@AllArgsConstructor
public class PlayerController {
    private final PlayerRestMapper mapper;
    private final PlayerService service;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return service.getPlayers().stream()
                .map(mapper::toRest)
                .toList();
    }
    @PostMapping("/players")
    public List<Player> postPlayers(
            @RequestBody PostPlayer player
    ) {
        return service.postPlayer(PlayerEntity.builder()
                        .name(player.getName())
                        .guardian(player.getIsGuardian())
                        .team(service.getTeam(player.getTeam()))
                        .build()).stream()
                .map(mapper::toRest)
                .toList();
    }
}
