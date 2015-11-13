package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "button_groups")
public class ButtonGroup extends EntityWithTenant implements NamedEntity, Listable<Long>, Saveable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private @Localized String name;
	
	@Column(nullable=false)
	private boolean retired = false;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@OrderColumn(name="orderIdx")
    @JoinTable(name="button_groups_buttons", joinColumns = @JoinColumn(name = "button_group_id"), inverseJoinColumns = @JoinColumn(name = "button_id"))
	private List<Button> buttons = new ArrayList<Button>();

	public ButtonGroup() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	

	private void trimName() {
		name = (name != null) ? name.trim() : null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}
	
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	@Override
	public String getDisplayName() {
		return getName();
	}
	
	public int countOfAvailableButtons() {
		return getAvailableButtons().size();
	}
	
	public List<String> getAvailableButtonStrings() {
		List<String> result = new ArrayList<String>();
		for (Button button : getAvailableButtons()) {
			result.add(button.getDisplayText());
		}
		return result;
	}
	
	public List<Button> getAvailableButtons() {
		List<Button> availableButton = new ArrayList<Button>();
		for( Button button : buttons) {
			if( !button.isRetired() ) {
				availableButton.add(button);
			}
		}
		return availableButton;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getId() == null || !(obj instanceof ButtonGroup)) {
			return super.equals(obj);
		} else {
			return equals((ButtonGroup) obj);
		} 
	}
	
	
	public boolean equals(ButtonGroup buttonGroup) {
		return (getId().equals(buttonGroup.getId())) ? true : getAvailableButtons().equals(buttonGroup.getAvailableButtons());
	}

    @Override
    public int hashCode() {
        return id == null ? 0 : id.intValue();
    }

	public Button getButton(String buttonName) {
		for (Button button : getAvailableButtons() ) {
			if (button.getDisplayName().equalsIgnoreCase(buttonName)) {
				return button;
			}
		}
		return null;
	}

}

