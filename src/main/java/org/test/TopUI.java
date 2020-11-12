package org.test;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ExternalResource;
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
@Title("Main Frame")
public class TopUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        final TextField message = new TextField();

        // User inputs
        message.setCaption("Type your message here:");
        Button sendMessageButton = new Button("Send message to inner frame");
        Button scrollButton = new Button("Scroll inner frame (ALT-S)");

        // Inner frame
        BrowserFrame browser = new BrowserFrame("Inner frame", new ExternalResource(("/inner/")));
        browser.setWidth("400px");
        browser.setHeight("400px");


        // Button handlers
        sendMessageButton.addClickListener(e -> {
            JavaScript.getCurrent().execute("document.getElementsByTagName('iframe')[0].contentWindow.handleMessage('"+message.getValue()+"');");
        });
        scrollButton.addClickListener(e -> {
            JavaScript.getCurrent().execute("document.getElementsByTagName('iframe')[0].contentWindow.scrollToActive();");
        });
        scrollButton.setClickShortcut(ShortcutAction.KeyCode.S, ShortcutAction.ModifierKey.ALT);

        // Publish JS method for focus
        JavaScript.getCurrent().addFunction("topFocus", args -> {
            message.focus(); // We focus the textfield
        });

        // UI layout
        layout.addComponents(message, sendMessageButton, scrollButton, browser);
        setContent(layout);

        // Focus
        message.focus();
    }

    @WebServlet(urlPatterns = "/*", name = "MyTopUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TopUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
