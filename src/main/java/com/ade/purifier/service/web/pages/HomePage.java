package com.ade.purifier.service.web.pages;

import org.apache.click.Page;
import org.apache.click.element.CssImport;
import org.apache.click.util.Bindable;
import org.apache.click.util.ClickUtils;

/**
 * Created by ismeade on 2014/11/14.
 */
public class HomePage extends Page {

    private static final long serialVersionUID = 1L;

    @Bindable
    protected String title = "Page";

    public HomePage() {
        String className = getClass().getName();

        String shortName = className.substring(className.lastIndexOf('.') + 1);
        String title = ClickUtils.toLabel(shortName);
        addModel("title", title);

        String srcPath = className.replace('.', '/') + ".java";
        addModel("srcPath", srcPath);
    }

    @Override
    public void onInit() {
        super.onInit();

        getHeadElements().add(new CssImport("/style.css"));
    }

    @Override
    public String getTemplate() {
        return "/home-template.htm";
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
