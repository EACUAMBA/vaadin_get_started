package com.example.application.views;

import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

//AppLayout is a Vaadin layout with a header and a responsive drawer.
public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames("text-l", "m-m");
        HorizontalLayout header = new HorizontalLayout(
                //DrawerToggle is a menu button that toggles the visibility of the sidebar.
                new DrawerToggle(),
                logo
        );

        //Centers the components in the header along the vertical axis.
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.addClassNames("py-0", "px-m");

        //Adds the header layout to the application layout’s nav bar, the section at the top of the screen.
        addToNavbar(header);
    }


    public void createDrawer() {
        //Creates a RouterLink with the text "List" and ListView.class as the destination view.
        RouterLink listLink = new RouterLink("List", ListView.class);
        RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
        //Sets setHighlightCondition(HighlightConditions.sameLocation()) to avoid highlighting the link for partial route matches. (Technically, every route starts with an empty route, so without this, it would always show up as active, even though the user isn’t on the view.)
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        //Wraps the link in a VerticalLayout and adds it to the AppLayout drawer.
        addToDrawer(
                new VerticalLayout(
                        listLink,
                        dashboardLink
                ));
    }
}
