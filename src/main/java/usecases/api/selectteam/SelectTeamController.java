package usecases.api.selectteam;

import java.util.UUID;

import context.Context;
import game.event.TeamSelectListener;
import minigame.view.ColoredTeamArmourView;
import usecases.api.jointeam.JoinTeamController;
import usecases.api.leaveteam.LeaveTeam;
import usecases.api.leaveteam.LeaveTeam.LeaveTeamResponse;
import usecases.api.leaveteam.LeaveTeamPresenter;
import usecases.api.leaveteam.LeaveTeamRequestModel;
import usecases.api.leaveteam.LeaveTeamUseCase;
import usecases.api.leaveteam.LeaveTeamView;
import usecases.api.leaveteam.LeaveTeamViewImpl;

public class SelectTeamController implements TeamSelectListener {

	@Override
	public void onTeamSelected(UUID player, String game, String team) {
		executeLeaveTeamUseCase(player);
		new JoinTeamController().onTeamSelected(player, game, team);
	}
	
	private void executeLeaveTeamUseCase(UUID uniquePlayerId) {
		ColoredTeamArmourView teamArmourView = Context.viewFactory.createColoredTeamArmourView();
		
		LeaveTeamView view = new LeaveTeamViewImpl();
		LeaveTeamResponse presenter = new LeaveTeamPresenter(view, teamArmourView);
		LeaveTeamRequestModel request = new LeaveTeamRequestModel();
		request.setPlayer(uniquePlayerId);
		
		LeaveTeam useCase = new LeaveTeamUseCase();
		useCase.setGameGateway(Context.gameGateway);
		useCase.setPlayerGateway(Context.playerGateway);
		useCase.execute(request, presenter);
	}

}
