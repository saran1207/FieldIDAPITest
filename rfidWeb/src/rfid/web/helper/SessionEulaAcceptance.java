package rfid.web.helper;

import com.n4systems.model.eula.CurrentEulaLoader;
import com.n4systems.model.eula.EULA;
import com.n4systems.model.eula.EulaAcceptance;
import com.n4systems.model.eula.LatestEulaAcceptanceLoader;

public class SessionEulaAcceptance {
	private boolean latestEulaAccepted;
	
	public SessionEulaAcceptance(CurrentEulaLoader currentEulaLoader, LatestEulaAcceptanceLoader latestEulaAcceptanceLoader) {
		EulaAcceptance eulaAccepted = latestEulaAcceptanceLoader.load();
		EULA latestEula = currentEulaLoader.load();
		
		if (eulaAccepted != null && eulaAccepted.getEula().equals(latestEula)) {
			latestEulaHasBeenAccepted();
		}
	}

	public void latestEulaHasBeenAccepted() {
		this.latestEulaAccepted = true;
	}

	public boolean isLatestEulaAccepted() {
		return latestEulaAccepted;
	}
}
