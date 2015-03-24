package com.ade.purifier.service.web.pages;

import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Label;
import org.apache.click.control.PageLink;

public class ReLoginPage extends HomePage {

	/** */
	private static final long serialVersionUID = 1L;
	
	private Form form = new Form("form");
	
	public ReLoginPage () {
		setTitle("ReLogin");
		
		addControl(form);
		
		FieldSet fieldSet = new FieldSet("ReLogin");
		form.add(fieldSet);
		
		Label label = new Label("reLable", "未登录或用户登录信息已过期，请重新登录。");
		fieldSet.add(label);
		
		PageLink pageLink = new PageLink("reLogin", "返回登录页面", LoginPage.class);
		fieldSet.add(pageLink);
		
	}
	
	@Override
	public void onInit() {
		getContext().getSession().invalidate();
	}

}
