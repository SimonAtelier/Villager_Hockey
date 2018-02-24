package config;

import java.util.List;

public interface Configuration extends WinTitleConfiguration, MapTitleConfiguration, GoalTitleConfiguration {

	String getPrefix();
	
	int getLobbyTime();
	
	String getLobbyGameMode();
	
	public boolean isAutobalanceEnabled();
	
	public boolean isAchievementsEnabled();
	
	String getVillagerName();
	
	boolean isVillagerAIEnabled();
	
	boolean isUseRandomVillagerNamesEnabled();
	
	public List<String> getRandomVillagerNames();
		
}