package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Similar to form component updating behavior but works on form component panels.
// Add it to a form component panel, you will still need a way to trigger the onchange() method
// of that div. For example see autoComplete.js where it evals that method when the event occurs that indicates an autocomplete option has been chosen.
public abstract class FormComponentPanelUpdatingBehavior extends AjaxEventBehavior {

    private static final Logger log = LoggerFactory.getLogger(AjaxFormComponentUpdatingBehavior.class);

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct.
     *
     * @param event
     *            event to trigger this behavior
     */
    public FormComponentPanelUpdatingBehavior(final String event)
    {
        super(event);
    }

    /**
     *
     * @see org.apache.wicket.behavior.AbstractAjaxBehavior#onBind()
     */
    @Override
    protected void onBind()
    {
        super.onBind();

        if (!(getComponent() instanceof FormComponent))
        {
            throw new WicketRuntimeException("Behavior " + getClass().getName() +
                    " can only be added to an instance of a FormComponent");
        }
    }

    /**
     *
     * @return FormComponent
     */
    protected final FormComponent<?> getFormComponent()
    {
        return (FormComponent<?>)getComponent();
    }

    /**
     * @see org.apache.wicket.ajax.AjaxEventBehavior#getEventHandler()
     */
    @Override
    protected final CharSequence getEventHandler()
    {
        return generateCallbackScript(new AppendingStringBuffer("wicketAjaxPost('").append(
                getCallbackUrl()).append(
                "', wicketSerializeForm(document.getElementById('" + getComponent().getMarkupId() +
                        "',false))"));
    }

    /**
     * @see org.apache.wicket.ajax.AjaxEventBehavior#onCheckEvent(java.lang.String)
     */
    @Override
    protected void onCheckEvent(String event)
    {
        if ("href".equalsIgnoreCase(event))
        {
            throw new IllegalArgumentException(
                    "this behavior cannot be attached to an 'href' event");
        }
    }

    /**
     *
     * @see org.apache.wicket.ajax.AjaxEventBehavior#onEvent(org.apache.wicket.ajax.AjaxRequestTarget)
     */
    @Override
    protected final void onEvent(final AjaxRequestTarget target)
    {
        final FormComponent<?> formComponent = getFormComponent();

        if (getEvent().toLowerCase().equals("onblur") && disableFocusOnBlur())
        {
            target.focusComponent(null);
        }

        try
        {
            formComponent.visitChildren(FormComponent.class, new IVisitor<Component, Void>() {
                @Override
                public void component(Component object, IVisit<Void> visit) {
                    ((FormComponent)object).inputChanged();
                    ((FormComponent)object).validate();
                }
            });
            formComponent.inputChanged();
            formComponent.validate();
            if (formComponent.hasErrorMessage())
            {
                formComponent.invalid();

                onError(target, null);
            }
            else
            {
                formComponent.valid();
                if (getUpdateModel())
                {
                    formComponent.updateModel();
                }

                onUpdate(target);
            }
        }
        catch (RuntimeException e)
        {
            onError(target, e);

        }
    }

    /**
     * @return true if the model of form component should be updated, false otherwise
     */
    protected boolean getUpdateModel()
    {
        return true;
    }

    /**
     * Determines whether the focus will not be restored when the event is blur. By default this is
     * true, as we don't want to re-focus component on blur event.
     *
     * @return <code>true</code> if refocusing should be disabled, <code>false</code> otherwise
     */
    protected boolean disableFocusOnBlur()
    {
        return true;
    }

    /**
     * Listener invoked on the ajax request. This listener is invoked after the component's model
     * has been updated.
     *
     * @param target
     */
    protected abstract void onUpdate(AjaxRequestTarget target);

    /**
     * Called to handle any error resulting from updating form component. Errors thrown from
     * {@link #onUpdate(AjaxRequestTarget)} will not be caught here.
     *
     * The RuntimeException will be null if it was just a validation or conversion error of the
     * FormComponent
     *
     * @param target
     * @param e
     */
    protected void onError(AjaxRequestTarget target, RuntimeException e)
    {
        if (e != null)
        {
            throw e;
        }
    }

}
