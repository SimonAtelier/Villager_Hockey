package achievements;

public class Condition {

	private int activationValue;
	private String propertyKey;
	private ActivationRule activationRule;
	
	public Condition(String propertyKey, ActivationRule activationRule, int activationValue) {
		this.activationValue = activationValue;
		this.propertyKey = propertyKey;
		this.activationRule = activationRule;
	}

	public int getActivationValue() {
		return activationValue;
	}

	public String getPropertyKey() {
		return propertyKey;
	}
	
	public ActivationRule getActivationRule() {
		return activationRule;
	}
	
	public boolean isActive(int value) {
		return activationRule.isActive(value, activationValue);
	}

}
