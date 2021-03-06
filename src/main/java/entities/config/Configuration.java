package entities.config;

import java.util.List;

public interface Configuration extends WinTitleConfiguration, MapTitleConfiguration, GoalTitleConfiguration {

	boolean isSpectatorMessagesEnabled();
	
	int getSpectatorMessagesRadius();
	
	String getPrefix();
	
	String getChatWithAllLabel();
	
	int getLobbyTime();
	
	String getLobbyGameMode();
	
	boolean isAutobalanceEnabled();
	
	boolean isAchievementsEnabled();
	
	boolean isCosmeticsEnabled();
	
	String getVillagerName();
	
	boolean isVillagerAIEnabled();
	
	boolean isUseRandomVillagerNamesEnabled();
	
	List<String> getRandomVillagerNames();
	
	String getScoreboardTitle();
	
	String getScoreboardServerAdress();
		
}
