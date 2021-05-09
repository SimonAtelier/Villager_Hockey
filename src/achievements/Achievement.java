package achievements;

import java.util.ArrayList;
import java.util.List;

public class Achievement {

	private int points;
	private String id;
	private String name;
	private String description;
	private List<AchieveCondition> achieveConditions;
	
	public Achievement(int points, String id, String name, String description) {
		this.points = points;
		this.id = id;
		this.name = name;
		this.description = description;
		this.achieveConditions = new ArrayList<AchieveCondition>();
	}
	
	public void addAchieveCondition(String propertyKey, ActivationRule activationRule, int activationValue) {
		addAchieveCondition(new AchieveCondition(propertyKey, activationRule, activationValue));
	}

	public void addAchieveCondition(AchieveCondition achieveCondition) {
		achieveConditions.add(achieveCondition);
	}
	
	public List<AchieveCondition> getAchieveConditions() {
		return new ArrayList<AchieveCondition>(achieveConditions);
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
