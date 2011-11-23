package com.n4systems.fieldid.wicket;

import static com.google.common.base.Preconditions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import com.google.common.base.Predicate;

public class WicketHarness {
	
	private final IWicketTester delegate;
	private String pathContext;
	private Map<String, FormTester> formTesters;	
	
	public WicketHarness(String pathContext, IWicketTester tester) { 
		this.delegate = tester;		
		setPathContext(pathContext);
		this.formTesters = new HashMap<String,FormTester>();
	}
	
	protected String getPathContext(String... ids) {
		return getPathFor(ids) + ":";		
	}

	private void setPathContext(String context) {
		this.pathContext = context;
	}
	
	public String getLastRenderedPageAsHtml() { 
		return getWicketTester().getServletResponse().getDocument();
	}

	protected void click(String... ids) {
		getWicketTester().executeAjaxEvent(getPathFor(ids), "onclick");	
	}

	public FormTester newFormTester(boolean resolvePath, String id) {
		String path = resolvePath ? getPathFor(id) : id;
		return getWicketTester().newFormTester(path);
	}
	
	@Deprecated   // use short form "get" instead...
	protected Component getComponent(String... ids) {
		return get(ids);
	}
		
	public Component get(String... ids) {
		return getWicketTester().getComponentFromLastRenderedPage(getPathFor(ids));
	}
	
	protected boolean isComponentVisible(String... ids) {
		// CAVEAT: can be false positive if you give ids that don't exist. 
		// you might think it's invisible when it really just never was created.
		return getComponent(ids) != null;
	}
	
	protected WicketTester getWicketTester() {
		return delegate.getWicketTester();
	}
			
	protected String getPathFor(String... ids) {
		checkArgument(ids!=null);
		StringBuffer buff = new StringBuffer(getPathContext());		
		for(String id:ids) {
			buff.append(id);			
			buff.append(":");
		}
		return buff.substring(0,buff.length()-1);	
	}

	private String getPathContext() {
		return pathContext;
	}
	
	public void submitForm(String... ids) {
		FormTester formTester = getFormTester(ids);
		formTester.submit();
		// can only submit once per form tester. 
		formTesters.remove(getPathFor(ids));
	}

	public FormTester getFormTester(String... ids) {
		String formPath = getPathFor(ids);
	
		FormTester formTester = formTesters.get(formPath);
		
		if (formTester==null) { 
			formTester = newFormTester(false, formPath);
			formTesters.put(formPath, formTester);
		}
		return formTester;
	}

	protected <X> void selectByDisplayValue(FormTester tester, DropDownChoice<X> dropDown, Predicate<X> predicate) {
		List<? extends X> choices = dropDown.getChoices();
		for(int i = 0; i < choices.size(); i++) {
			if(predicate.apply(choices.get(i))) {
				tester.select(dropDown.getId(), i);
				return;
			}
		}
		throw new IllegalArgumentException("Display value not found in " +  dropDown.getId());
	}
	
}
