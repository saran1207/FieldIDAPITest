package com.n4systems.fieldid.viewhelpers.navigation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.util.properties.HierarchicalProperties;

public class NavOptionsController {

	private List<NavOption> navOptions = new ArrayList<NavOption>();
	private List<NavOption> filteredNavOptions;
	private String currentAction;
	private NavOption currentActionOption;
	private SessionUser user;
	private SystemSecurityGuard securityGuard;
	private boolean onSetup = false;
	private boolean returnToReport = false;
	private String entityIdentifier;
	private boolean onSafetyNetwork = false;

	private boolean onSetupSettings = false;
	private boolean onSetupOwners = false;
	private boolean onSetupAssets = false;
	private boolean onSetupTemplate = false;

	public NavOptionsController(SessionUser user, SystemSecurityGuard securityGuard) {
		this.user = user;
		this.securityGuard = securityGuard;
	}

	public NavOptionsController(SessionUser user, SystemSecurityGuard securityGaurd, String pageType, String currentAction) {
		this(user, securityGaurd);
		findNavOptions(pageType);
		setupCurrentAction(currentAction);
	}

	private void setupCurrentAction(String currentAction) {
		this.currentAction = currentAction;
		for (NavOption option : navOptions) {
			if (option.getName().equals(currentAction)) {
				this.currentActionOption = option;
				return;
			}
		}
	}

	private void findNavOptions(String pageType) {
		try {
			HierarchicalProperties p = new HierarchicalProperties();
			InputStream propertyFile = getClass().getResourceAsStream(pageType + ".properties");
			if (propertyFile == null) {
				return;
			}

			p.load(propertyFile);
			HierarchicalProperties commonProps = p.getProperties("common");
			onSetup = commonProps.getBoolean("onSetup");
			onSetupSettings = commonProps.getBoolean("onSetupSettings");
			onSetupOwners = commonProps.getBoolean("onSetupOwners");
			onSetupAssets = commonProps.getBoolean("onSetupAssets");
			onSetupTemplate = commonProps.getBoolean("onSetupTemplate");
			onSafetyNetwork = commonProps.getBoolean("onSafetyNetwork");
			returnToReport = commonProps.getBoolean("returnToReport");
			entityIdentifier =  commonProps.getString("title.entity_identifier");
			String commonTitle =  commonProps.getString("title");

			for (HierarchicalProperties props : p.getPropertiesList("option")) {
				Map<String, String> urlParams = new HashMap<String, String>();

				HierarchicalProperties urlProps = props.getProperties("urlParams");
				for (Object key : urlProps.keySet()) {
					urlParams.put((String) key, urlProps.getString((String) key));
				}

				String navTitle = props.getString("title");
				navOptions.add(new NavOption(
											props.getString("label"), 
											props.getString("name"), 
											props.getString("action"), 
											props.getInteger("order"), 
											props.getString("permissionRequired"), 
											props.getString("extendedFeatureRequired"), 
											props.getString("type"), 
											urlParams, 
											props.getString("conditionalView"),
                                            props.getBoolean("wicket"),
											(navTitle != null) ? navTitle : commonTitle));
			}

			Collections.sort(navOptions, new Comparator<NavOption>() {
				public int compare(NavOption o1, NavOption o2) {
					return o1.getOrder().compareTo(o2.getOrder());
				}
			});
		} catch (IOException e) {
			// no file just has an empty set of options.
		}
	}

	public List<NavOption> getOptions() {
		return navOptions;
	}

	public List<NavOption> getFilteredOptions() {
		if (filteredNavOptions == null) {
			filteredNavOptions = new ArrayList<NavOption>();

			for (NavOption option : navOptions) {
				if (userHasRequiredPermissionForOption(option) && hasCorrectExtendedFeaturesForOption(option)) {
					filteredNavOptions.add(option);
				}
			}
		}
		return filteredNavOptions;
	}
	
	private boolean userHasRequiredPermissionForOption(NavOption option) {
		if (user != null) {
			if (option.getPermissionRequired() == null) {
				return true;
			}
			
			if (user.hasAccess(option.getPermissionRequired())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasCorrectExtendedFeaturesForOption(NavOption option) {
		if (option.getExtendedFeatureRequired() == null) {
			return true;
		}
		if (securityGuard.isExtendedFeatureEnabled(ExtendedFeature.valueOf(option.getExtendedFeatureRequired()))) {
			return true;
		}
		return false;
	}

	public String getCurrentAction() {
		return currentAction;
	}

	public NavOption getCurrentActionOption() {
		return currentActionOption;
	}

	public boolean entityLoaded() {
		if (currentActionOption != null && currentActionOption.isEntity()) {
			return true;
		}
		return false;
	}

	public boolean isOnSetup() {
		return onSetup;
	}

	public boolean isReturnToReport() {
		return returnToReport;
	}

	public String getTitle() {
		return currentActionOption != null ? currentActionOption.getTitle() : null;
	}

	public String getEntityIdentifier() {
		return entityIdentifier;
	}
	
	public boolean isOnSafetyNetwork() {
		return onSafetyNetwork;
	}

	public boolean isOnSetupSettings() {
		return onSetupSettings;
	}

	public boolean isOnSetupOwners() {
		return onSetupOwners;
	}

	public boolean isOnSetupAssets() {
		return onSetupAssets;
	}

	public boolean isOnSetupTemplate() {
		return onSetupTemplate;
	}
}
