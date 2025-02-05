package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.EntitySetOfEntitiesPair;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StatsScreenController {

    @FXML
    public GridPane chartsGrid;

    @FXML
    public ComboBox<String> statsComboBox;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    public void initialize() {
        List<Expense> expenseList = expenseRepository.findAll();
        List<Employee> employeeList = employeeRepository.findAll();

        List<EntitySetOfEntitiesPair<Employee, Expense>> listOfExpensesByEmployee = new ArrayList<>();

        for(Employee employee : employeeList){
            Set<Expense> expensesOfEmployee = expenseList.stream()
                    .filter(expense -> expense.getTravelLog().getEmployee().getId().equals(employee.getId()))
                    .collect(Collectors.toSet());

            listOfExpensesByEmployee.add(new EntitySetOfEntitiesPair<>(employee, expensesOfEmployee));
        }

        ChartViewer chartViewer = new ChartViewer(getSpendingByEmployeeChart(listOfExpensesByEmployee));
        chartsGrid.add(chartViewer,1,1);
    }

    private JFreeChart getSpendingByEmployeeChart(List<EntitySetOfEntitiesPair<Employee, Expense>> listOfExpensesByEmployee){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(EntitySetOfEntitiesPair<Employee, Expense> pair : listOfExpensesByEmployee){
            Optional<BigDecimal> totalSpent = pair.getSetOfEntities().stream().map(Expense::getAmount).reduce(BigDecimal::add);
            totalSpent.ifPresent(bigDecimal -> dataset.addValue(bigDecimal,
                    "Total spent",
                    pair.getEntity().getFirstName() + " " + pair.getEntity().getLastName()));
        }

        return ChartFactory.createBarChart("Total spent by employee", "Employees","Total spent",dataset);
    }
}
