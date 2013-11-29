package com.n4systems.fieldid.wicket.components.form;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public class InlineEditableForm extends Form {

    private boolean editing;

    public InlineEditableForm(String id) {
        super(id);
        add(new AttributeAppender("class", new Model<String>() {
            @Override public String getObject() {
                return editing ? getEditingFormCss() : "";
            }
        }));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (!editing) {
            stopEditing();
        } else {
            startEditing();
        }
    }

    public InlineEditableForm toggleEdit() {
        if (editing) {
            stopEditing();
        } else {
            startEditing();
        }
        return this;
    }

    public InlineEditableForm startEditing() {
        this.editing  = true;
        visitChildren(FormComponent.class, new IVisitor<Component, Object>() {
            @Override public void component(Component component, IVisit<Object> visit) {
                component.setVisible(true);
            }
        });
        return this;
    }

    public InlineEditableForm stopEditing() {
        this.editing = false;
        visitChildren(FormComponent.class, new IVisitor<Component, Object>() {
            @Override public void component(Component component, IVisit<Object> visit) {
                boolean isVisible = hideEmptyFieldsWhenViewing() && !StringUtils.isBlank(component.getDefaultModelObjectAsString());
                component.setVisible(isVisible);
            }
        });
        return this;
    }

    private final IModel<String> getCssClassForFields(final Component component) {
        return new Model<String>() {
            @Override public String getObject() {
                return editing ? getEditingComponentCss() : getNotEditingComponentCss();
            }
        };
    }

    public InlineEditableForm withSaveCancelEditLinks() {
        addSaveLink("save");
        addCancelLink("cancel");
        addEditLink("edit");
        return this;
    }

    @Override
    public MarkupContainer add(Component... childs) {
        MarkupContainer container = super.add(childs);
        for (Component component:childs) {
            addInlineEditingBehavior(component);
        }
        return container;
    }

    private void addInlineEditingBehavior(Component component) {
        if (component instanceof FormComponent) {
            component.add(new AttributeAppender("class",getCssClassForFields(component), " "));
            component.add(new AttributeModifier("disabled", "disabled") {
                @Override public boolean isEnabled(Component component) {
                    return !editing;
                }
            });
        } else if (component instanceof MarkupContainer) {
            MarkupContainer container = (MarkupContainer) component;
            container.visitChildren(Component.class, new IVisitor<Component, Object>() {
                @Override
                public void component(Component component, IVisit<Object> visit) {
                    addInlineEditingBehavior(component);
                }
            });
        }
    }

    protected boolean hideEmptyFieldsWhenViewing() {
        return true;
    }

    protected String getEditingFormCss() {
        return "editable-inputs";
    }

    protected String getNotEditingComponentCss() {
        return "";
    }

    protected String getEditingComponentCss() {
        return "editing";
    }

    public void addSaveLink(String id) {
        add(new AjaxSubmitLink(id) {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                save(target);
            }
            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                // TODO : how to handle errors? are they practically possible?
            }
            @Override public boolean isVisible() {
                return editing;
            }
        }.setOutputMarkupPlaceholderTag(true));
    }

    public void addEditLink(String id) {
        add(new AjaxLink(id) {
            @Override public void onClick(AjaxRequestTarget target) {
                edit(target);
            }

            @Override public boolean isVisible() {
                return !editing;
            }
        }.setOutputMarkupPlaceholderTag(true));
    }

    public void addCancelLink(String id) {
        add(new AjaxLink(id) {
            @Override public void onClick(AjaxRequestTarget target) {
                cancel(target);
            }
            @Override public boolean isVisible() {
                return editing;
            }
        }.setOutputMarkupPlaceholderTag(true));
    }

    protected void cancel(AjaxRequestTarget target) {
        stopEditing();
        target.add(InlineEditableForm.this);   // TODO : add some javascript to refresh map.
    }

    protected void edit(AjaxRequestTarget target) {
        startEditing();
        target.add(InlineEditableForm.this);
    }

    protected void save(AjaxRequestTarget target) {
        stopEditing();
        target.add(InlineEditableForm.this);
    }

}


