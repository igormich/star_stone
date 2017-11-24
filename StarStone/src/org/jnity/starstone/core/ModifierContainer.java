package org.jnity.starstone.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jnity.starstone.modifiers.Modifier;

public class ModifierContainer extends GamePart implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8600962131430277113L;
	private final ArrayList<Modifier> modifiers = new ArrayList<>();
	
	public List<Modifier> getModifiers() {
		return (List<Modifier>) modifiers.clone();
	}

	public void addModifier(Modifier modifier) {
		if(!modifier.canBeDuplicated())
			modifiers.removeIf(m -> m.getClass() == modifier.getClass());
		modifiers.add(modifier);
	}

	public void removeModifier(Modifier modifier) {
		modifiers.remove(modifier);
	}
	
	public boolean hasModifier(Class<? extends Modifier> modifierType) {
		return getModifiers().stream().anyMatch(m -> m.getClass() == modifierType);
	}
	
}
