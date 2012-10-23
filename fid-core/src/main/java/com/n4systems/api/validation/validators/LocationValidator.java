package com.n4systems.api.validation.validators;

import com.n4systems.api.conversion.event.LocationSpecification;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.location.*;
import com.n4systems.model.security.SecurityFilter;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LocationValidator implements FieldValidator {

    @Override
    public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
        if (fieldValue == null) {
            return ValidationResult.pass();
        }

        try {
            Location location = getLocation(new LocationSpecification(String.valueOf(fieldValue)), createPredefinedLocationTreeLoader(filter).load());
            if (location==null) {
                return ValidationResult.fail(InvalidLocationValidatorFail, String.valueOf(fieldValue), fieldName);
            }
        } catch (IllegalArgumentException e) {
            return ValidationResult.fail(InvalidLocationSpecificationValidatorFail, String.valueOf(fieldValue), fieldName);
        }
        return ValidationResult.pass();
    }

    private PredefinedLocationTreeLoader createPredefinedLocationTreeLoader(SecurityFilter filter) {
        return new PredefinedLocationTreeLoader(new PredefinedLocationListLoader(filter));
    }

    public Location getLocation(LocationSpecification locationSpecification, PredefinedLocationTree predefinedLocationTree) {
        if (!locationSpecification.getHierarchy().isEmpty()) {
            PredefinedLocation predefinedLocation = getNode(locationSpecification, predefinedLocationTree);
            if (predefinedLocation==null) {
                return null;
            }
            return new Location(predefinedLocation, locationSpecification.getFreeForm());
        } else {
            return new Location(locationSpecification.getFreeForm());
        }
    }

    private PredefinedLocation getNode(LocationSpecification locationSpecification, PredefinedLocationTree predefinedLocationTree) {
        Iterator<String> i = locationSpecification.getHierarchy().iterator();
        Set<PredefinedLocationTreeNode> nodes = predefinedLocationTree.getNodes();
        PredefinedLocationTreeNode node = null;
        while (i.hasNext()) {
            node = findNode(nodes, i.next());
            if (node==null) {
                return null;
            }
            nodes = node.getChildren();
        }
        return node.getNodeValue();
    }

    private PredefinedLocationTreeNode findNode(Set<PredefinedLocationTreeNode> nodes, String name) {
        for (PredefinedLocationTreeNode node:nodes) {
            if (name!=null && name.equalsIgnoreCase(node.getName())) {
                return node;
            }
        }
        return null;
    }
}
