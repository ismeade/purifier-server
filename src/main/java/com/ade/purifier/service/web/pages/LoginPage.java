package com.ade.purifier.service.web.pages;

import com.ade.purifier.orm.ObjectContextFactory;
import com.ade.purifier.orm.dao.SysUserDao;
import com.ade.purifier.orm.model.SysUser;
import com.ade.purifier.utils.StringUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

public class LoginPage extends HomePage {

	/** */
	private static final long serialVersionUID = 1L;
	
	private Form form = new Form("form");
	
	/** Constructor */
	public LoginPage(){
		setTitle("Login");
		
		addControl(form);
		FieldSet fieldSet = new FieldSet("Login");
		form.add(fieldSet);
		TextField userName = new TextField("userName", true);
		userName.setMaxLength(20);
		userName.setFocus(true);
		fieldSet.add(userName);
		
		PasswordField passWord = new PasswordField("passWord", true);
		passWord.setMaxLength(20);
		fieldSet.add(passWord);
		
		form.add(new Submit("ok", " OK ", this, "onOkClicked"));
		form.add(new Submit("cancel", this, "onCancelClicked"));
	}
	
	/**
	* Handle the OK button click event.
	*
	* @return true
	*/
	public boolean onOkClicked() {
        ObjectContext context = ObjectContextFactory.createObjectContext();
        SysUserDao sysUserDao = new SysUserDao(context);
		if (form.isValid()) {
			String userName = form.getFieldValue("userName");
			String passWord = form.getFieldValue("passWord");
			SysUser user = sysUserDao.getSysUser(userName);
			String msg = "";
			if (user == null) {
				msg = "用户不存在.";
				addModel("msg", msg);
			} else if (!StringUtils.MD5(passWord).equals(user.getPassword())) {
				msg = "密码错误.";
				addModel("msg", msg);
			} else {
                System.out.println("save");
                getContext().getRequest().getSession().setAttribute("objectContext", context);
				getContext().getRequest().getSession().setAttribute("user", user);
				setRedirect(IndexPage.class);
			}
			form.clearValues();
    }
		return true;
	}
	
	/**
	* Handle the Cancel button click event.
	*
	* @return false
	*/
	public boolean onCancelClicked() {
		setRedirect(LoginPage.class);
		return false;
	}

}
