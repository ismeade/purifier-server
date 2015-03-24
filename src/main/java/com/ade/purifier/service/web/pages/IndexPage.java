package com.ade.purifier.service.web.pages;

public class IndexPage extends BorderPage {
	
	/** */
	private static final long serialVersionUID = 1L;
	
	public IndexPage(){
		setTitle("index");
        String path = getContext().getRequest().getContextPath();
        String path2 = getContext().getResourcePath();
        System.out.println("1###" + path);
        System.out.println("2###" + path2);
    }

}
