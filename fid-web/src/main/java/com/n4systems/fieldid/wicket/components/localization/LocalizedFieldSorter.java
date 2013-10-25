package com.n4systems.fieldid.wicket.components.localization;


import com.google.common.collect.Lists;
import com.n4systems.model.*;
import com.n4systems.model.utils.ClassMap;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LocalizedFieldSorter {

    private Map<Class<?>,List<String>> map = new ClassMap<List<String>>();

    public LocalizedFieldSorter() {
        // if the class doesn't itself have @Localized Strings but it's children do, then just add an empty list.
        add(AssetStatus.class, "name");
        add(AssetType.class, Lists.newArrayList("name", "descriptionTemplate", "manufactureCertificateText", "warnings"));
        add(AssetTypeGroup.class, Lists.newArrayList("name"));
        add(Button.class, "displayText");
        add(ButtonGroup.class, "name");
        add(EventBook.class, "name");
        add(EventStatus.class, "name");
        add(EventType.class,"name");
        add(EventTypeGroup.class, "name");
        add(Score.class, "name");
        add(ScoreGroup.class, "name");
        add(InfoFieldBean.class, "name");
        add(CriteriaSection.class, "title");
        add(ComboBoxCriteria.class, Lists.newArrayList("displayText","recommendations", "deficiencies", "options"));
        add(SelectCriteria.class, Lists.newArrayList("displayText", "recommendations", "deficiencies", "options"));
        add(Criteria.class, Lists.newArrayList("displayText", "recommendations", "deficiencies"));
        add(InfoOptionBean.class, Lists.newArrayList("name"));
    }

    private void add(Class<?> clazz, String fieldName) {
        map.put(clazz,Lists.newArrayList(fieldName));
    }

    private void add(Class<?> clazz, List<String> fieldNames) {
        map.put(clazz,fieldNames);
    }

    public List<String> getFieldsForClass(Class<?> clazz) {
        return map.get(clazz);
    }

    public int getFieldOrdinal(Class<?> clazz, String fieldName) {
        String f = fieldName.trim();
        List<String> fields = getFieldsForClass(clazz);
        if (fields!=null) {
            for (int i=0; i<fields.size();i++) {
                if (fieldName.equals(fields.get(i))) {
                    return i;
                }
            }
        }
        throw new IllegalStateException("can't find translatable fields for class " + clazz.getSimpleName() + " & field '" + fieldName + "'. Make sure you put all the possible fields included in the map at construction time");
    }

    public List<LocalizedField> sort(List<LocalizedField> localizedFields) {
        Collections.sort(localizedFields, new Comparator<LocalizedField>() {
            @Override
            public int compare(LocalizedField o1, LocalizedField o2) {
                if (!o1.getEntity().getClass().equals(o2.getEntity().getClass())) {
                    return o1.getEntity().getClass().hashCode() - o2.getEntity().getClass().hashCode();
                }
                int ordinal1 = getFieldOrdinal(o1.getEntity().getClass(), o1.getName());
                int ordinal2 = getFieldOrdinal(o2.getEntity().getClass(), o2.getName());
                return ordinal1 - ordinal2;
            }
        });
        return localizedFields;
    }
}
