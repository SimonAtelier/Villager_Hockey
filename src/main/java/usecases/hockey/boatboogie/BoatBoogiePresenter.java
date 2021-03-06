package usecases.hockey.boatboogie;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;

import context.Context;
import entities.Location;
import minigame.view.TitleView;
import minigame.view.TitleViewModel;
import usecases.hockey.boatboogie.BoatBoogie.BoatBoogieResponse;
import util.LocationConvert;

public class BoatBoogiePresenter implements BoatBoogieResponse {

	private BoatBoogieResponseModel responseModel;
	
	@Override
	public void onPresentBoats(BoatBoogieResponseModel responseModel) {
		setResponseModel(responseModel);
		
		List<UUID> players = responseModel.getPlayers();
		List<Location> locations = responseModel.getLocations();
		
		for (int i = 0; i < players.size(); i++) {
			org.bukkit.Location l = LocationConvert.toBukkitLocation(locations.get(i));
			Boat boat = (Boat) l.getWorld().spawnEntity(l, EntityType.BOAT);
			boat.setWoodType(TreeSpecies.DARK_OAK);
			boat.addPassenger(Bukkit.getPlayer(players.get(i)));
		}
		
		broadcastTitle();
	}
	
	private void broadcastTitle() {
		TitleView titleView = Context.viewFactory.createTitleView();
		titleView.setTitleViewModel(createTitleViewModel());
		
		for (UUID player : responseModel.getPlayers())
			titleView.display(player);
	}
	
	private TitleViewModel createTitleViewModel() {
		TitleViewModel titleViewModel = Context.viewFactory.createTitleViewModel();
		titleViewModel.setTitle("�cBOAT BOOGIE!");
		titleViewModel.setSubtitle("�eSPECIAL ROUND!");
		titleViewModel.setFadeInTimeInSeconds(1);
		titleViewModel.setStayTimeInSeconds(2);
		titleViewModel.setFadeOutTimeInSeconds(1);
		return titleViewModel;
	}
	
	private void setResponseModel(BoatBoogieResponseModel responseModel) {
		this.responseModel = responseModel;
	}

}
