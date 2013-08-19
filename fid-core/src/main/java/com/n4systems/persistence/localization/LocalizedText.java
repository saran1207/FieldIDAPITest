package com.n4systems.persistence.localization;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * USAGE NOTE : you MUST override the columns text & labelId via the @AttributeOverrides annotation so hibernate will know which columns in your table you
 * are using.  i could just use a "best guess" technique which might work some of the time but that could lead to confusing errors so this fail fast approach is taken.
 *
 * e.g.
 *  @AttributeOverrides( {
 *      @AttributeOverride(name=LocalizedText.TEXT_COLUMN, column = @Column(name="name") ),
 *      @AttributeOverride(name=LocalizedText.LOCALIZATION_ID_COLUMN, column = @Column(name="lid"))
 *  })
 *
 * @Embedded
 * private LocalizedText name;
 *
 * your entity must have a varchar/String column AND a bigint/Long column used as a nullable foreign key for translated text DB table.
 *
 **/

@Embeddable
public class LocalizedText implements Serializable {

    public static final String TEXT_COLUMN = "defaultText";
    public static final String LOCALIZATION_ID_COLUMN = "text";

    public LocalizedText() {
        this(null);
    }

    public LocalizedText(String defaultText) {
        this.defaultText = defaultText;
    }

    @Column(name = "text-override_this")
    public String defaultText;

    @Column(name= "translationId-override_this", nullable = true)
    @Type(type = "com.n4systems.persistence.localization.LocalizedTextUserType")
    public String text;

    public String getText() {
        return text!=null ? text : defaultText;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

}
