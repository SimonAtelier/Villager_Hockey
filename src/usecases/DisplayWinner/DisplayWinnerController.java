package usecases.DisplayWinner;

import context.Context;
import usecases.DisplayWinner.DisplayWinner.DisplayWinnerRequest;
import usecases.DisplayWinner.DisplayWinner.DisplayWinnerResponse;

public class DisplayWinnerController {

	public void onDisplayWinner(String game) {
		DisplayWinnerView view = new DisplayWinnerViewImpl();
		DisplayWinnerResponse presenter = new DisplayWinnerPresenter(view);
		DisplayWinner useCase = new DisplayWinnerUseCase();
		useCase.setGameGateway(Context.gameGateway);
		useCase.execute(createRequest(game), presenter);
	}
	
	private DisplayWinnerRequest createRequest(String game) {
		DisplayWinnerRequestModel requestModel = new DisplayWinnerRequestModel();
		requestModel.setGame(game);
		return requestModel;
	}
	
}
