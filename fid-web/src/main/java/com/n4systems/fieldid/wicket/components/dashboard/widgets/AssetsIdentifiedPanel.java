package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

import com.n4systems.services.reporting.ReportingService;
import com.n4systems.util.FlotDataSet;

public class AssetsIdentifiedPanel extends Panel {
	
	@SpringBean
	private ReportingService reportingService;
	
    public AssetsIdentifiedPanel(String id) {
        super(id);
        Flot graph = new Flot("graph");
        graph.setOutputMarkupId(true).setMarkupId(graph.getId());
		add(graph);
		Form form = new Form("form" );
		form.add(new AjaxSubmitButton("update") {
			@Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		        FlotDataSet data = reportingService.getAssetsIdentified(new Date(), new Date(), null);
				target.appendJavascript("updateGraph("+data.toJavascriptString()+");");
		        
//		        String s = "[[1196463600000, 0], [1196550000000, 0], [1196636400000, 0], [1196722800000, 77], [1196809200000, 3636], [1196895600000, 3575], [1196982000000, 2736], [1197068400000, 1086], [1197154800000, 676], [1197241200000, 1205], [1197327600000, 906], [1197414000000, 710], [1197500400000, 639], [1197586800000, 540], [1197673200000, 435], [1197759600000, 301], [1197846000000, 575], [1197932400000, 481], [1198018800000, 591], [1198105200000, 608], [1198191600000, 459], [1198278000000, 234], [1198364400000, 1352], [1198450800000, 686], [1198537200000, 279], [1198623600000, 449], [1198710000000, 468], [1198796400000, 392], [1198882800000, 282], [1198969200000, 208], [1199055600000, 229], [1199142000000, 177], [1199228400000, 374], [1199314800000, 436], [1199401200000, 404], [1199487600000, 253], [1199574000000, 218], [1199660400000, 476], [1199746800000, 462], [1199833200000, 448], [1199919600000, 442], [1200006000000, 403], [1200092400000, 204], [1200178800000, 194], [1200265200000, 327], [1200351600000, 374], [1200438000000, 507], [1200524400000, 546], [1200610800000, 482], [1200697200000, 283], [1200783600000, 221], [1200870000000, 483], [1200956400000, 523], [1201042800000, 528], [1201129200000, 483], [1201215600000, 452], [1201302000000, 270], [1201388400000, 222], [1201474800000, 439], [1201561200000, 559], [1201647600000, 521], [1201734000000, 477], [1201820400000, 442], [1201906800000, 252], [1201993200000, 236], [1202079600000, 525], [1202166000000, 477], [1202252400000, 386], [1202338800000, 409], [1202425200000, 408], [1202511600000, 237], [1202598000000, 193], [1202684400000, 357], [1202770800000, 414], [1202857200000, 393], [1202943600000, 353], [1203030000000, 364], [1203116400000, 215], [1203202800000, 214], [1203289200000, 356], [1203375600000, 399], [1203462000000, 334], [1203548400000, 348], [1203634800000, 243], [1203721200000, 126], [1203807600000, 157], [1203894000000, 288]]";		        
		        //target.appendJavascript("updateGraph("+s+");");
			} 			
		});
		add(form);
    }
}



class Flot extends WebMarkupContainer implements IWiQueryPlugin {

  private String id;
  private JsStatement statement = new JsStatement();
  private Options options;

  public Flot(String id) {
      super(id);
      this.id = id;
      this.options = new Options();
  }

  public Options getOptions() {
      return options;
  }

  public void setOptions(Options options) {
      this.options = options;
  }

 
  public void setChartData(double[][] data) {

      if (data != null) {

          String varValue = "d1";
          statement.append("var " + varValue + "= [");
          for (int i = 0; i < data.length; i++) {
              double[] ds = data[i];
              statement.append("[" + ds[0] + "," + ds[1] + "]");
              if (!(i == data.length - 1)) {
                  statement.append(",");
              }
          }
          statement.append("]; ");

          statement.append(" $.plot(" + new JsQuery(this).$().append("," + "[" + varValue + "]" + ")").render());
      }
  }

  @Override
  public void contribute(WiQueryResourceManager wiQueryResourceManager) {
//      wiQueryResourceManager.addJavaScriptResource(FlotJavaScriptResourceReference.get());
//      wiQueryResourceManager.addJavaScriptResource(ExCanvasJavaScriptResourceReference.get());
  }

  @Override
  public JsStatement statement() {
      return this.statement;
  }
}
