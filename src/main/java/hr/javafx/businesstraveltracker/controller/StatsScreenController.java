package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.StatsChartType;
import hr.javafx.businesstraveltracker.model.*;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.TravelLogRepository;
import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class StatsScreenController {

    @FXML
    public GridPane chartsGrid;

    @FXML
    public ComboBox<StatsChartType> statsComboBox;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    private final TravelLogRepository travelLogRepository = new TravelLogRepository();

    private static final String TOTAL_SPENT = "Total spent";

    public void initialize() {
        List<Expense> expenseList = expenseRepository.findAll();
        List<Employee> employeeList = employeeRepository.findAll();
        List<TravelLog> travelLogList = travelLogRepository.findAll();

        ChartViewer chartViewer = new ChartViewer(getSpendingByEmployeeChart(employeeList, expenseList));
        chartsGrid.add(chartViewer,1,1);

        statsComboBox.getItems().addAll(StatsChartType.values());
        statsComboBox.getSelectionModel().select(StatsChartType.SPENDING_BY_EMPLOYEES);
        statsComboBox.setCellFactory(item -> new ListCell<>(){
            @Override
            protected void updateItem(StatsChartType statsChartType, boolean b) {
                super.updateItem(statsChartType, b);
                setText(b || statsChartType == null ? "" : statsChartType.getName());
            }
        });
        statsComboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(StatsChartType statsChartType, boolean b) {
                super.updateItem(statsChartType, b);
                setText(b || statsChartType == null ? "" : statsChartType.getName());
            }
        });
        statsComboBox.setOnAction(event ->{
            if(statsComboBox.getValue().equals(StatsChartType.SPENDING_BY_EMPLOYEES))
                chartViewer.setChart(getSpendingByEmployeeChart(employeeList, expenseList));
            else if(statsComboBox.getValue().equals(StatsChartType.SPENDING_BY_TRAVEL_LOGS))
                chartViewer.setChart(getSpendingByTravelLogChart(travelLogList, expenseList));
            else if(statsComboBox.getValue().equals(StatsChartType.SPENDING_BY_MONTH))
                chartViewer.setChart(getSpendingByMonth(expenseList));
        });
    }

    private JFreeChart getSpendingByEmployeeChart(List<Employee> employeeList, List<Expense> expenseList){
        List<EntitySetOfEntitiesPair<Employee, Expense>> listOfExpensesByEmployee = new ArrayList<>();

        for(Employee employee : employeeList){
            Set<Expense> expensesOfEmployee = expenseList.stream()
                    .filter(expense -> expense.getTravelLog().getEmployee().getId().equals(employee.getId()))
                    .collect(Collectors.toSet());

            listOfExpensesByEmployee.add(new EntitySetOfEntitiesPair<>(employee, expensesOfEmployee));
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(EntitySetOfEntitiesPair<Employee, Expense> pair : listOfExpensesByEmployee){
            Optional<BigDecimal> totalSpent = pair.getSetOfEntities().stream().map(Expense::getAmount).reduce(BigDecimal::add);
            totalSpent.ifPresent(bigDecimal -> dataset.addValue(bigDecimal,
                    TOTAL_SPENT,
                    pair.getEntity().getFirstName() + " " + pair.getEntity().getLastName()));
        }

        JFreeChart chart = ChartFactory.createBarChart("Total spent by employee", "Employees",TOTAL_SPENT,dataset);
        styleChart(chart);
        return chart;
    }

    private JFreeChart getSpendingByTravelLogChart(List<TravelLog> travelLogList, List<Expense> expenseList){
        List<EntitySetOfEntitiesPair<TravelLog, Expense>> listOfExpensesByTravelLog = new ArrayList<>();

        for(TravelLog travelLog: travelLogList){
            Set<Expense> expensesOfTravelLog = expenseList.stream()
                    .filter(expense -> expense.getTravelLog().getId().equals(travelLog.getId()))
                    .collect(Collectors.toSet());

            listOfExpensesByTravelLog.add(new EntitySetOfEntitiesPair<>(travelLog, expensesOfTravelLog));
        }

        DefaultPieDataset<String> defaultPieDataset = new DefaultPieDataset<>();
        for(EntitySetOfEntitiesPair<TravelLog, Expense> pair : listOfExpensesByTravelLog){
            Optional<BigDecimal> totalSpent = pair.getSetOfEntities().stream().map(Expense::getAmount).reduce(BigDecimal::add);
            totalSpent.ifPresent(bigDecimal -> defaultPieDataset.setValue(pair.getEntity().getDestination() + " (" + CustomDateTimeFormatter.formatDate(pair.getEntity().getStartDate()) +
                            " - " + CustomDateTimeFormatter.formatDate(pair.getEntity().getEndDate()),bigDecimal)
                    );
        }

        JFreeChart chart = ChartFactory.createPieChart("Total spent on each business trip" , defaultPieDataset, true, true, false);
        styleChart(chart);
        return chart;
    }

    private JFreeChart getSpendingByMonth(List<Expense> expenseList){
        List<YearMonth> yearMonthList = expenseList.stream()
                .map(expense -> YearMonth.of(expense.getDate().getYear(), expense.getDate().getMonth()))
                .distinct().toList();

        Map<YearMonth, Set<Expense>> expensesByMonth = new HashMap<>();
        for(YearMonth yearMonth : yearMonthList){
            Set<Expense> expensesOfTheMonth = expenseList.stream()
                    .filter(expense -> yearMonth.equals(YearMonth.of(expense.getDate().getYear(), expense.getDate().getMonth())))
                    .collect(Collectors.toSet());
            expensesByMonth.put(yearMonth, expensesOfTheMonth);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        expensesByMonth.forEach((yearMonth, expenseSet) ->{
            Optional<BigDecimal> totalSpent = expenseSet.stream().map(Expense::getAmount).reduce(BigDecimal::add);
            totalSpent.ifPresent(bigDecimal -> dataset.addValue(bigDecimal,
                    TOTAL_SPENT,
                    yearMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH) + ", " + yearMonth.getYear()));
        });

        JFreeChart chart = ChartFactory.createLineChart("Total spending by month", "Month", TOTAL_SPENT, dataset);
        styleChart(chart);
        return chart;
    }

    private void styleChart(JFreeChart chart) {
        chart.setPadding(new RectangleInsets(24, 24, 24, 24));
    }
}
