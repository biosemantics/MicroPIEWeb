package edu.arizona.biosemantics.micropie.web.client;

import com.google.gwt.core.client.GWT;  
import com.google.gwt.uibinder.client.UiBinder;  
import com.google.gwt.uibinder.client.UiField;  
import com.google.gwt.user.client.ui.Composite;  
import com.google.gwt.user.client.ui.HTML;  
import com.google.gwt.user.client.ui.VerticalPanel;  
import com.google.gwt.user.client.ui.Widget;

public class MyBinderWidget extends Composite {

     private static MyBinderWidgetUiBinder uiBinder = GWT
               .create(MyBinderWidgetUiBinder.class);

     interface MyBinderWidgetUiBinder extends UiBinder<Widget, MyBinderWidget> { }

     @UiField VerticalPanel myPanelContent;

     public MyBinderWidget() {
          initWidget(uiBinder.createAndBindUi(this));

          // HTML html1 = new HTML();
          // html1.setHTML("<a href='http://www.google.com'>Click me!</a>");
          // myPanelContent.add(html1);
          
          // HTML html2 = new HTML();
          // html2.setHTML("This is my sample <b>content</b>!");
          // myPanelContent.add(html2);

     }

}