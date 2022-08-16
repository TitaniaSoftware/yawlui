package org.yawlfoundation.yawl.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Inline;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AppShellSettings;
import org.yawlfoundation.yawl.resourcing.resource.Participant;
import org.yawlfoundation.yawl.resourcing.rsInterface.ResourceGatewayException;
import org.yawlfoundation.yawl.ui.menu.DrawerMenu;
import org.yawlfoundation.yawl.ui.service.EngineClient;
import org.yawlfoundation.yawl.ui.service.ResourceClient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


@Route("")
//@PWA(name = "Project Base for Vaadin", shortName = "Project Base", enableInstallPrompt = false)
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
//@Theme(value = Lumo.class)
//@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends AppLayout implements 
        ComponentEventListener<Tabs.SelectedChangeEvent>, AppShellConfigurator {

    private final ResourceClient _resClient = new ResourceClient();
    private final EngineClient _engClient = new EngineClient();
    private Participant _user;

    private Div _footer;


    public MainView() {
        super();
        showLoginForm();
    }


    // called on startup to set the app's favicon
    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon(Inline.Position.PREPEND,
                "icon", "icons/yawlLogo.png", "32x32");
    }


    // notification from DrawerMenu of an item selection
    @Override
    public void onComponentEvent(Tabs.SelectedChangeEvent event) {
        Tab tab = event.getSelectedTab();

        // tab's element's root has two children: an icon and a span (i.e. the label)
        String label = tab.getElement().getChild(0).getChild(1).getText();
        switch (label) {
            case "My Worklist" : setContent(new UserWorklistView(_resClient)); break;
            case "My Profile" : setContent(null); break;
            case "My Team's Worklist" : setContent(null); break;
            case "Case Mgt" : setContent(new CasesView(_resClient, _engClient)); break;
            case "Admin Worklist" : setContent(new AdminWorklistView(_resClient)); break;
            case "Resources" : setContent(null); break;
            case "Org Data" : setContent(null); break;
            case "Non-Human Resources" : setContent(null); break;
            case "Calendar" : setContent(null); break;
            case "Services / Clients"   : setContent(new ServicesView(_resClient, _engClient)); break;
            case "About" : setContent(null); break;
            case "Logout" : event.getSource().setSelectedIndex(0); exit();
        }
    }


    private void showLoginForm() {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.add(new H3("YAWL"));
        LoginForm login = new LoginForm();
        login.setForgotPasswordButtonVisible(false);

        login.addLoginListener(e -> {
            try {
                if (_resClient.authenticate(e.getUsername(), e.getPassword())) {
                    _user = _resClient.getParticipant(e.getUsername());
                    createMenuBar(_user);
                    setContent(getLandingPage(_user));

                } else {
                    setErrorMessage(login, null);        // sets default error msg
                    login.setError(true);                            // show the error
                }
            }
            catch (ResourceGatewayException | IOException |
                   NoSuchAlgorithmException ex) {
                setErrorMessage(login, ex.getMessage());
                login.setError(true);
                ex.printStackTrace();
            }
        });
        setContent(layout);
    }


    // the vaadin way to set the error msg to other than the default
     private void setErrorMessage(LoginForm login, String message) {
         LoginI18n i18n = LoginI18n.createDefault();             // resets to default msg
         if (message != null) {
             i18n.getErrorMessage().setMessage(message);
         }
         login.setI18n(i18n);
     }


    public Participant getCurrentUser() { return _user; }


    private void createMenuBar(Participant p) {
        Image img = new Image("icons/yawlLogo.png", "YAWL Logo");
        img.setHeight("44px");
        addToNavbar(new DrawerToggle(), img);
        DrawerMenu menu = new DrawerMenu(p);
        menu.addSelectedChangeListener(this);
        addToDrawer(menu);
    }


    private Div createFooter() {
        Div tempContent = new Div();
        tempContent.setText("temp content");
        setContent(tempContent);
        Div footer = new Div();
        footer.addClassName("app-footer");
        int lastIndex = getElement().getChildCount();
        footer.setWidthFull();
        footer.setText("YAWL v5.0");
        getElement().insertChild(lastIndex, footer.getElement());
        return footer;
    }


    private Component getLandingPage(Participant p) {
        return p == null ? new AdminWorklistView(_resClient) : new UserWorklistView(_resClient);
    }


    private void exit() {
        try {
            _resClient.disconnect();
            _engClient.disconnect();
        }
        catch (IOException ioe) {
            // ignore
        }
        showLoginForm();
    }

}