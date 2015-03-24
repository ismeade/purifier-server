package com.ade.purifier.service.web.pages;

import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.PageLink;

public class NotAuthorityPage extends HomePage{

	/** */
	private static final long serialVersionUID = 1L;
	
	private Form form = new Form("form");

	public NotAuthorityPage (){
		
		addControl(form);
		
		FieldSet fieldSet = new FieldSet("NotAuthority");
		form.add(fieldSet);
		
		Label label = new Label("outLable", "权限不足。");
		fieldSet.add(label);
		
		PageLink pageLink = new PageLink("reIndex", "返回首页", IndexPage.class);
		fieldSet.add(pageLink);
		
	}
	
}
