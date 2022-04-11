package com.exercise.depthchart.service;

import com.exercise.depthchart.exceptions.ApplicationException;
import com.exercise.depthchart.exceptions.BadRequestException;
import com.exercise.depthchart.exceptions.ResourceNotFoundException;
import com.exercise.depthchart.model.Player;
import com.exercise.depthchart.model.Position;
import com.exercise.depthchart.model.Sport;
import com.exercise.depthchart.repository.dao.PlayerDao;
import com.exercise.depthchart.repository.dao.PositionDao;
import com.exercise.depthchart.repository.dao.SportDao;
import com.exercise.depthchart.repository.dto.PlayerDTO;
import com.exercise.depthchart.repository.dto.PositionDTO;
import com.exercise.depthchart.repository.dto.SportDTO;
import com.exercise.depthchart.repository.mapper.EntityDTOMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessLogic {

  @Autowired SportDao sportDao;

  @Autowired PlayerDao playerDao;

  @Autowired PositionDao positionDao;

  @Autowired EntityDTOMapper entityDTOMapper;

  /**
   * Creates a new sport and associated positions
   *
   * @param sportDTO minimum data required from client for constructing Sport object
   * @return new Sport data
   */
  public SportDTO createSport(SportDTO sportDTO) {
    // check whether the sport already exists in db
    Sport sportToCreate = entityDTOMapper.sportDTOToEntity(sportDTO);
    var checkExisting = sportDao.getSportsByName(sportToCreate.getName());
    if (!checkExisting.isEmpty()) {
      throw new BadRequestException("Sport already exists!");
    }
    List<String> positionsNames = sportDTO.getPositions();
    List<Position> positions = new ArrayList<>();
    positionsNames.forEach(
        pn -> {
          var position = Position.builder().name(pn).sport(sportToCreate).build();
          positions.add(position);
        });
    sportToCreate.setPositions(positions);
    var sport = sportDao.saveSport(sportToCreate);
    return entityDTOMapper.sportToDTO(sport);
  }

  /**
   * Adding a new player to an existing depth chart.
   *
   * @param playerName
   * @param playerPosition
   * @param positionDepth
   * @return
   */
  public PlayerDTO addPlayerToDepthChart(
      String playerName, String playerPosition, Integer positionDepth) {

    String playerFlag = "standard";

    var playerBuilder = Player.builder().name(playerName);
    if (positionDepth != null) {
      playerBuilder.depth(positionDepth);
    }
    var player = playerBuilder.build();

    // get players in the chart
    var position = positionDao.getPositionByName(playerPosition);
    if (position.isEmpty()) {
      throw new ResourceNotFoundException("Position data not found");
    }

    // check duplicates
    var extPlayer = playerDao.getPlayerByName(playerName);
    for (Player pl : extPlayer) {

      for (Player posPl : position.get().getPlayers()) {
        if (posPl.getName().equals(pl.getName())) {
          if (posPl.getDepth().equals(positionDepth)) {
            throw new BadRequestException("Duplicate request");
          } else {
            player.setId(pl.getId());
            playerFlag = "update";
            break;
          }
        }
      }
    }

    // update depth
    var players = position.get().getPlayers();
    var playerCount = players.size();

    if (player.getDepth() == null) {
      player.setDepth(playerCount);
    }

    if (player.getDepth() > playerCount) {
      player.setDepth(playerCount - 1);
    }
    if (playerFlag.equals("standard")) {

      position.get().getPlayers().add(player);

    } else {

      for (Player posPL : players) {
        if (posPL.getId().equals(player.getId())) {
          players.get(players.indexOf(posPL)).setDepth(player.getDepth());

          break;
        }
      }
    }

    if (playerFlag.equals("standard")) {
      if (player.getDepth() >= 0 && player.getDepth() < playerCount) {
        for (int i = 0; i < playerCount; i++) {

          var tmp = players.get(i);
          if (!tmp.getName().equals(player.getName())) {
            tmp.setDepth(i + 1);
            players.set(i, tmp);
          }
        }
      }
    } else {
      for (int i = 0; i < playerCount; i++) {

        var tmp = players.get(i);
        if (!tmp.getId().equals(player.getId())) {
          tmp.setDepth(tmp.getDepth() + 1);
          players.set(i, tmp);
        }
      }
    }

    position.get().setPlayers(players);

    var savedPosition = positionDao.savePosition(position.get());
    var playerData =
        savedPosition.getPlayers().stream().filter(x -> x.getName().equals(playerName)).findFirst();

    if (playerData.isEmpty()) {
      throw new ApplicationException("Error creating new player");
    }

    return entityDTOMapper.playerToDTO(playerData.get());
  }

  /**
   * Removes a player from the depth chart based on position and player name
   *
   * @param playerName
   * @param positionName
   * @return
   */
  public PositionDTO removePlayerFromDepthChart(String playerName, String positionName) {

    var positionData = positionDao.getPositionByName(positionName);
    if (positionData.isEmpty()) {
      throw new BadRequestException(
          String.format("Position data with name [%s] does not exist", positionName));
    }

    var playerData = positionData.get().getPlayers();

    Player playerToRemove = null;
    // expecting result to be 1 here if not do not process
    for (Player pl : playerData) {
      if (pl.getName().equals(playerName)) {
        playerToRemove = pl;
      }
    }

    if (playerToRemove == null) {
      return entityDTOMapper.positionToDTO(positionData.get());
    }

    // process with deletion in position table
    playerDao.deletePlayer(playerToRemove);

    var result = positionDao.getPositionByName(positionName);

    // update depth
    var players = result.get().getPlayers();
    var playerCount = players.size();

    if (playerToRemove.getDepth() == null || playerToRemove.getDepth() > playerCount) {
      playerToRemove.setDepth(playerCount);
    }

    if (playerToRemove.getDepth() >= 0 && playerToRemove.getDepth() < playerCount) {
      for (int i = 0; i < playerCount; i++) {

        var tmp = players.get(i);
        if (tmp.getDepth() != 0) {
          tmp.setDepth(tmp.getDepth() - 1);
        }

        players.set(i, tmp);
      }
    }

    result.get().setPlayers(players);

    var savedPosition = positionDao.savePosition(result.get());

    if (result.isEmpty()) {
      throw new ApplicationException("Error while retrieving position data");
    }
    Collections.sort(savedPosition.getPlayers(), Comparator.comparingInt(Player::getDepth));

    return entityDTOMapper.positionToDTO(savedPosition);
  }

  public List<PositionDTO> getFullDepthChart() {
    var positionList = positionDao.getAllPositionData();

    for (Position p : positionList) {
      int idx = positionList.indexOf(p);
      Collections.sort(p.getPlayers(), Comparator.comparingInt(Player::getDepth));
      positionList.set(idx, p);
    }

    List<PositionDTO> depthChart = new ArrayList<>();

    for (Position p : positionList) {
      depthChart.add(entityDTOMapper.positionToDTO(p));
    }
    return depthChart;
  }

  /**
   * Returns players under a specific player in a depth chart
   *
   * @param playerName
   * @param positionName
   * @return
   */
  public List<Player> getPlayersUnderPlayerInDepthChart(String playerName, String positionName) {

    // get position data
    var positionData = positionDao.getPositionByName(positionName);
    if (positionData.isEmpty()) {
      throw new ResourceNotFoundException("Position not found");
    }
    var playersInChart = positionData.get().getPlayers();
    var filterPlayer =
        playersInChart.stream().filter(x -> x.getName().equals(playerName)).findFirst();
    if (filterPlayer.isEmpty()) {
      throw new ResourceNotFoundException("Player does not exist in the depth chart");
    }

    var result =
        playersInChart.stream()
            .filter(x -> x.getDepth() > filterPlayer.get().getDepth())
            .collect(Collectors.toList());

    Collections.sort(result, Comparator.comparingInt(Player::getDepth));

    return result;
  }
}
