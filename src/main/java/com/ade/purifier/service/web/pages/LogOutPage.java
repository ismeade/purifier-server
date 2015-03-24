package com.ade.purifier.service.web.pages;

import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.PageLink;

public class LogOutPage extends HomePage{
	
	/** */
	private static final long serialVersionUID = 1L;
	
	private Form form = new Form("form");
	
	public LogOutPage(){
		setTitle("LogOut");
		
		addControl(form);
		
		FieldSet fieldSet = new FieldSet("LogOut");
		form.add(fieldSet);
		
		Label label = new Label("outLable", "已经安全登出，单机下边连接返回登录页面。");
		fieldSet.add(label);
		
		PageLink pageLink = new PageLink("reIndex", "返回登录页面", LoginPage.class);
		fieldSet.add(pageLink);
		
	}
	
	@Override
	public void onInit() {
		getContext().getSession().invalidate();
	}

}
