package com.n4systems.export.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public abstract class AbstractEventConverter<T extends AbstractEvent> extends AbstractEntityConverter<T> {

	@Override
	protected void marshalEntity(T event, HierarchicalStreamWriter writer, MarshallingContext context) {
		writeNode(writer, context, "AssetId", event.getAsset().getMobileGUID());
		writeNode(writer, context, "EventType", event.getType());
		
		super.marshalEntity(event, writer, context);
		
		writeNode(writer, context, "AssetStatus", event.getAssetStatus());
		writeNode(writer, context, "Comments", event.getComments());
		
		writeAttributes(writer, event);
		writeResults(writer, context, event);
	}

	private void writeResults(HierarchicalStreamWriter writer, MarshallingContext context, AbstractEvent event) {
		writer.startNode("Results");
		if (event.getEventForm() != null && !event.getResults().isEmpty()) {
			List<CriteriaResult> sectionResults;
			for (CriteriaSection section: event.getEventForm().getSections()) {
				sectionResults = collectResultsForSection(section, event.getResults());
				
				if (sectionResults.isEmpty())
					continue;
				
				writer.startNode("Section");
				writer.addAttribute("Name", section.getName());
				for (CriteriaResult result: sectionResults) {
					context.convertAnother(result);
				}
				writer.endNode();
			}
		}
		writer.endNode();
	}

	private void writeAttributes(HierarchicalStreamWriter writer, AbstractEvent event) {
		writer.startNode("Attributes");
		for (Map.Entry<String, String> attrib: event.getInfoOptionMap().entrySet()) {
			writer.startNode("Attribute");
			writer.addAttribute("name", attrib.getKey());
			writer.addAttribute("value", attrib.getValue());
			writer.endNode();
		}
		writer.endNode();
	}
	
	private List<CriteriaResult> collectResultsForSection(CriteriaSection section, Set<CriteriaResult> results) {
		List<CriteriaResult> sectionResults = new ArrayList<CriteriaResult>();
		for (Criteria criteria: section.getCriteria()) {
			for (CriteriaResult result: results) {
				if (result.getCriteria().equals(criteria)) {
					sectionResults.add(result);
					break;
				}
			}
		}
		return sectionResults;
	}

}
