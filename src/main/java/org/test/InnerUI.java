package org.test;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Title("Sub Frame")
public class InnerUI extends UI {

    private Window dialog;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        Button button = new Button("Focus top frame");

        button.addClickListener(e -> {
            // We create modal window that steals the focus
            if (dialog != null)  getUI().removeWindow(dialog);
            dialog = new Window();
            dialog.setContent(new Label("Modal Dialog"));
            dialog.setModal(true);
            getUI().addWindow(dialog);

            // Refocus the top frame
            JavaScript.getCurrent().execute("top.topFocus()");
        });



        // Publish Javascript callbacks
        JavaScript.getCurrent().addFunction("handleMessage", args -> {
            Notification.show("Inner frame received message: "+args.getString(0));
        });

        JavaScript.getCurrent().addFunction("scrollToActive", args -> {
            Notification.show("Inner frame scrolltoActive.");
        });


        // Layout UI
        layout.addComponents(new Label("This is inner frame"), button);
        setContent(layout);

    }

    @WebServlet(urlPatterns = "/inner/*", name = "MyInnerUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = InnerUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
