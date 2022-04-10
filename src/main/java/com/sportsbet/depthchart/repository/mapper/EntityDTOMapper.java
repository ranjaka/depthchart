package com.sportsbet.depthchart.repository.mapper;

import com.sportsbet.depthchart.model.Player;
import com.sportsbet.depthchart.model.Position;
import com.sportsbet.depthchart.model.Sport;
import com.sportsbet.depthchart.repository.dto.CreatePlayerDTO;
import com.sportsbet.depthchart.repository.dto.DeletePlayerDTO;
import com.sportsbet.depthchart.repository.dto.PlayerDTO;
import com.sportsbet.depthchart.repository.dto.PositionDTO;
import com.sportsbet.depthchart.repository.dto.SportDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring", builder = @Builder(disableBuilder = true))
public abstract class EntityDTOMapper {
  // --- Player ---
  @Mapping(target = "position", ignore = true)
  public abstract PlayerDTO playerToDTO(Player player);

  //  @Mapping(target = "id", ignore = true)
  //  @Mapping(target = "position", ignore = true)
  //  public abstract Player playerToEntity(PlayerDTO playerDTO);

  public abstract Player createPlayerToEntity(CreatePlayerDTO createPlayerDTO);

  public abstract Player deletePlayerDTOToEntity(DeletePlayerDTO deletePlayerDTO);

  //  @AfterMapping
  //  public void playerToDTOAfterMapping(Player source, @MappingTarget PlayerDTO target) {
  //    target.setPosition(source.getPosition().getName());
  //  }

  //  @AfterMapping
  //  public void createPlayerDTOToEntity(CreatePlayerDTO source, @MappingTarget Player target) {
  //    Position position = Position.builder().name(source.getPosition()).build();
  //    target.setPosition(position);
  //  }

  //  @AfterMapping
  //  public void deletePlayerDTOToEntityAfterMapping(
  //      DeletePlayerDTO source, @MappingTarget Player target) {
  //    Position position = Position.builder().name(source.getPosition()).build();
  //    target.setPosition(position);
  //  }

  // position

  @Mapping(target = "playerIds", ignore = true)
  public abstract PositionDTO positionToDTO(Position position);

  @AfterMapping
  public void positionToDTOAfterMapping(Position source, @MappingTarget PositionDTO target) {
    List<Player> players = source.getPlayers();
    List<Integer> playerIds = new ArrayList<>();
    for (Player player : players) {
      playerIds.add(player.getId());
    }
    target.setPlayerIds(playerIds);
  }

  // --- Sport ---
  @Mapping(target = "positions", ignore = true)
  public abstract Sport sportDTOToEntity(SportDTO sportDTO);

  @Mapping(target = "positions", ignore = true)
  public abstract SportDTO sportToDTO(Sport sport);

  @AfterMapping
  public void sportToDTOAfterMapping(Sport source, @MappingTarget SportDTO target) {
    List<String> positionNames = new ArrayList<>();
    source
        .getPositions()
        .forEach(
            position -> {
              positionNames.add(position.getName());
            });
    target.setPositions(positionNames);
  }
}
