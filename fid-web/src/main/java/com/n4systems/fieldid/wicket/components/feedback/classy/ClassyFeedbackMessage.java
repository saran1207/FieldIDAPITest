package com.n4systems.fieldid.wicket.components.feedback.classy;

import org.apache.wicket.Component;

import java.io.Serializable;

// Represents
public abstract class ClassyFeedbackMessage<T extends Component>  implements Serializable {

    public abstract T createComponent(String id);

    // Returns a non stylized message that can be displayed in feedback panels that don't know about ClassyFeedbackMessage
    public abstract String getPlainMessage();

    public String toString() {
        return getPlainMessage();
    }

}
