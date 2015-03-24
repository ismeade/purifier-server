package com.ade.purifier.service.web.pages;

import org.apache.click.Page;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;
import org.apache.click.util.Bindable;

import java.util.Iterator;

/**
 *
 * Created by ismeade on 2014/11/14.
 */
public class BorderPage extends Page {

    private static final long serialVersionUID = 1L;

    private transient Menu rootMenu;

    @Bindable
    protected String title = "Page";

    public BorderPage() {
//        String className = getClass().getName();
//
//        String shortName = className.substring(className.lastIndexOf('.') + 1);
//        String title = ClickUtils.toLabel(shortName);
//        addModel("title", title);
//
//        String srcPath = className.replace('.', '/') + ".java";
//        addModel("srcPath", srcPath);
    }

    @Override
    public void onInit() {
        super.onInit();

        getHeadElements().add(new CssImport("/style.css"));

        Iterator<Element> iterator = getHeadElements().iterator();
        while (iterator.hasNext()) {
            System.out.println("TTTTTT " + iterator.next().toString());
        }


        MenuFactory menuFactory = new MenuFactory();
        rootMenu = menuFactory.getRootMenu();
        addControl(rootMenu);
    }

    @Override
    public void onDestroy() {
        if (rootMenu != null) {
            removeControl(rootMenu);
        }
    }

    @Override
    public String getTemplate() {
        return "/border-template.htm";
    }

    /**
     * 是否登录验证
     */
    public boolean onSecurityCheck() {
        System.out.println("session:" + getContext().hasSession());
        if (getContext().hasSession()) {
            return true;
        } else {
            setRedirect(ReLoginPage.class);
            return false;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }
}