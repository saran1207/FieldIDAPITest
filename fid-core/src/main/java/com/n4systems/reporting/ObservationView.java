package com.n4systems.reporting;

import java.io.Serializable;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.Observation;
import com.n4systems.model.SubEvent;

public class ObservationView implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String part;
		private String type;
		private String section;
		private String criteria;
		private String status;
		private String observation;
		
		public ObservationView() {}

		public ObservationView(AbstractEvent insp, CriteriaResult result, Observation obs) {
			
			if(insp instanceof Event) {
				// master events just use their type as the part name
				part = insp.getAsset().getType().getName();
			} else if(insp instanceof SubEvent) {
				// sub event need to use their specific part name
				part = ((SubEvent)insp).getName();
			}
			
			type = obs.getType().getDisplayTitle();
			section = insp.findSection(result.getCriteria()).getTitle();
			criteria = result.getCriteria().getDisplayText();
			status = obs.getState().getDisplayName();
			observation = obs.getText();
		}
		
		public String getPart() {
			return part;
		}

		public void setPart(String part) {
			this.part = part;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSection() {
			return section;
		}

		public void setSection(String section) {
			this.section = section;
		}

		public String getCriteria() {
			return criteria;
		}

		public void setCriteria(String criteria) {
			this.criteria = criteria;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getObservation() {
			return observation;
		}

		public void setObservation(String observation) {
			this.observation = observation;
		}
	}