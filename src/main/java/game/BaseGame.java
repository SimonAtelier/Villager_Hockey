package game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import context.Context;
import entities.Location;
import entities.Team;
import entities.Teams;
import game.event.GameChangeSupport;
import game.event.GameStateChangeListener;
import game.event.JoinListener;
import game.event.LeaveListener;
import game.event.TeamScoreListener;
import game.event.TeamSelectListener;
import game.loop.GameLoop;
import game.states.GameState;
import game.states.StoppedGameState;
import game.states.WaitingGameState;
import gateways.PlayerDataGateway;
import gateways.impl.PlayerDataGatewayYaml;
import usecases.api.loadinventory.LoadInventoryController;

public class BaseGame implements Game {

	private final Object PLAYERS_LOCK = new Object();

	private int minimumPlayersToStart;
	private Location lobby;	
	private boolean started;
	private int playingTimeInSeconds;
	private String name;
	private GameState gameState;
	private GameLoop gameLoop;
	private Teams teams;
	private GameCycle gameCycle;
	protected List<UUID> players;
	private GameChangeSupport gameChangeSupport;

	public BaseGame(String name) {
		this.name = name;
		gameState = new StoppedGameState();
		gameState.setGame(this);
		teams = new Teams();
		players = new ArrayList<UUID>();
		gameChangeSupport = new GameChangeSupport(this);
		this.gameLoop = Context.gameLoopFactory.createGameLoop();
		gameLoop.setGame(this);
		gameCycle = new GameCycleAdapter();
	}

	protected boolean addPlayer(UUID player) {
		synchronized (PLAYERS_LOCK) {
			if (player == null)
				return false;
			if (players.contains(player))
				return false;
			players.add(player);
			return true;
		}
	}

	protected boolean removePlayer(UUID player) {
		synchronized (PLAYERS_LOCK) {
			if (player == null)
				return false;
			if (!players.contains(player))
				return false;
			players.remove(player);
			return true;
		}
	}
	
	@Override
	public void tick() {
		gameState.onTick();
	}

	@Override
	public void start() {
		if (started) {
			return;
		}
		started = true;
		gameLoop.start();
		getGameState().transitionToGameState(new WaitingGameState());
	}

	@Override
	public void stop() {
		if (!started)
			return;
		started = false;
		gameLoop.stop();
		getGameState().transitionToGameState(new StoppedGameState());
	}

	@Override
	public void join(UUID player) {
		if (!canPlayerJoin(player))
			return;

		if (addPlayer(player)) {
			getGameState().onPlayerJoin(player);
			gameChangeSupport.firePlayerJoin(player);
		}
	}

	@Override
	public void leave(UUID player) {
		removePlayer(player);
		getGameState().onPlayerLeave(player);
		getGameCycle().onPlayerLeave(player);
		gameChangeSupport.firePlayerLeave(player);
		handleLeave(player);
		if (getPlayersCount() == 0) {
			getTeams().resetTeamScores();
			getGameState().transitionToGameState(new WaitingGameState());
		}
	}
	
	@Override
	public void selectLowestTeam(UUID player) {
		Team team = getTeams().findLowestTeam();
		gameChangeSupport.fireTeamSelected(player, team.getName());
	}
	
	private void handleLeave(UUID player) {
		removePlayerFromTeam(player);
		restoreInventory(player);
		restorePlayerData(player);
	}
	
	private void removePlayerFromTeam(UUID player) {
		getTeams().removePlayer(player);
	}
	
	private void restoreInventory(UUID player) {
		new LoadInventoryController().onLoadInventory(player);
	}

	private void restorePlayerData(UUID player) {
		PlayerDataGateway playerDataGateway = new PlayerDataGatewayYaml();
		playerDataGateway.load(player);
	}

	@Override
	public void leaveAll() {
		for (UUID player : getUniquePlayerIds()) {
			leave(player);
		}
	}

	@Override
	public void addGameStateChangeListener(GameStateChangeListener listener) {
		gameChangeSupport.addGameStateChangeListener(listener);
	}

	@Override
	public void removeGameStateChangeListener(GameStateChangeListener listener) {
		gameChangeSupport.removeGameStateChangeListener(listener);
	}

	@Override
	public void addJoinListener(JoinListener listener) {
		gameChangeSupport.addJoinListener(listener);
	}

	@Override
	public void removeJoinListener(JoinListener listener) {
		gameChangeSupport.removeJoinListener(listener);
	}

	@Override
	public void addLeaveListener(LeaveListener listener) {
		gameChangeSupport.addLeaveListener(listener);
	}

	@Override
	public void removeLeaveListener(LeaveListener listener) {
		gameChangeSupport.removeLeaveListener(listener);
	}

	@Override
	public void addTeamSelectListener(TeamSelectListener listener) {
		gameChangeSupport.addTeamSelectListener(listener);
	}

	@Override
	public void removeTeamSelectListener(TeamSelectListener listener) {
		gameChangeSupport.removeTeamSelectListener(listener);
	}

	@Override
	public void addTeamScoreListener(TeamScoreListener listener) {
		gameChangeSupport.addTeamScoreListener(listener);
	}

	@Override
	public void removeTeamScoreListener(TeamScoreListener listener) {
		gameChangeSupport.removeTeamScoreListener(listener);
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public int getMinimumPlayersToStart() {
		return minimumPlayersToStart;
	}

	@Override
	public void setMinimumPlayersToStart(int minimumPlayersToStart) {
		this.minimumPlayersToStart = minimumPlayersToStart;
	}

	@Override
	public int getMaximumAmountOfPlayers() {
		return getTeams().getMaximumAmountOfPlayers();
	}

	@Override
	public int getPlayingTimeInSeconds() {
		return playingTimeInSeconds;
	}

	@Override
	public void setPlayingTimeInSeconds(int playingTimeInSeconds) {
		this.playingTimeInSeconds = playingTimeInSeconds;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Location getLobby() {
		return lobby;
	}

	@Override
	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	@Override
	public int getPlayersCount() {
		return players.size();
	}

	@Override
	public GameState getGameState() {
		return gameState;
	}

	@Override
	public void setGameState(GameState gameState) {
		GameState oldGameState = this.gameState;
		gameState.setGame(this);
		this.gameState = gameState;
		gameChangeSupport.fireGameStateChanged(oldGameState, gameState);
	}

	@Override
	public boolean isMaximumAmountOfPlayersReached() {
		return getMaximumAmountOfPlayers() == getPlayersCount();
	}

	@Override
	public boolean canPlayerJoin(UUID player) {
		return getGameState().canPlayerJoin(player);
	}

	@Override
	public List<UUID> getUniquePlayerIds() {
		List<UUID> players = new ArrayList<UUID>();
		synchronized (PLAYERS_LOCK) {
			for (UUID player : this.players) {
				players.add(player);
			}
		}
		return players;
	}

	@Override
	public Teams getTeams() {
		return teams;
	}

	@Override
	public GameCycle getGameCycle() {
		return gameCycle;
	}

	@Override
	public void setGameCycle(GameCycle gameCycle) {
		this.gameCycle = gameCycle;
	}

	@Override
	public GameChangeSupport getChangeSupport() {
		return gameChangeSupport;
	}

	@Override
	public void onLoad() {
		getGameCycle().onLoad();
	}

	@Override
	public void onUnload() {
		getGameCycle().onUnload();
		leaveAll();
	}

}
