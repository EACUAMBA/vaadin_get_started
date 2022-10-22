package com.example.application.views;

import com.example.application.data.service.CRMService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//DashboardView is mapped to the "dashboard" path and uses MainLayout as a parent layout.
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle(("Dashboard ! Vaadin CRM"))
public class DashboardView extends VerticalLayout {
    private final CRMService crmService;

    //Takes CrmService as a constructor parameter and saves it as a field.
    public DashboardView(CRMService crmService){
        this.crmService = crmService;
        addClassName("dashboard-view");
        //Centers the contents of the layout.
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getContactStats(), getCompaniesChart());
    }

    private Component getContactStats(){
        //Calls the service to get the number of contacts.
        Span stats = new Span(crmService.countContacts() + " contacts");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    private Chart getCompaniesChart(){
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        crmService.findAllCompanies()
                .forEach(company -> {
                    dataSeries.add(
                            //Calls the service to get all companies, then creates a DataSeriesItem for each, containing the company name and employee count.
                            new DataSeriesItem(company.getName(), company.getEmployeesCount())
                    );
                });
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }
}
