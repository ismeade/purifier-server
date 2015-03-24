package com.ade.purifier.service.web;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.deploy.ContextEnvironment;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.deploy.NamingResources;
import org.apache.catalina.startup.Tomcat;
import org.apache.click.ClickServlet;
import org.apache.naming.resources.DirContextURLStreamHandler;

import javax.naming.directory.DirContext;

/**
 *
 * Created by ismeade on 2014/11/13.
 */
public class TomcatServer {

    public void startServer() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setBaseDir(System.getProperty("user.dir") + "/webapps");

        // 访问时候localhost:8080/purifier/*.htm
        Context context = tomcat.addContext("/", System.getProperty("user.dir") + "/webapps" + "/management");
        // 加载servlet
        Tomcat.addServlet(context, "dateServlet", new DatePrintServlet());
        context.addServletMapping("/date", "dateServlet");
        Tomcat.addServlet(context, "ClickServlet", new ClickServlet());
        context.addServletMapping("*.htm", "ClickServlet");
        context.addWelcomeFile("/login.htm");

        tomcat.start();
        tomcat.getServer().await();

    }

    public static void main(String[] args) {
        try {
            new TomcatServer().startServer();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
