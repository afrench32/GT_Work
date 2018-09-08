package edu.gatech.seclass.sdpsalarycalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class SDPSalaryCalcActivity extends AppCompatActivity {

    private EditText salary;
    private Spinner initialCity;
    private Spinner destinationCity;
    private Button runButton;
    private EditText resultSalary;
    private TextView labelDestinationCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdpsalary_calc);

        salary = (EditText) findViewById(R.id.salary);
        initialCity = (Spinner) findViewById(R.id.initialCity);
        destinationCity = (Spinner) findViewById(R.id.destinationCity);
        runButton = (Button) findViewById(R.id.runButton);
        resultSalary = (EditText) findViewById(R.id.resultSalary);
        labelDestinationCity = (TextView) findViewById(R.id.labelDestinationCity);

    }

    public void handleClick(View view) {
        String sal = salary.getText().toString();
        String ic = initialCity.getSelectedItem().toString();
        String dc = destinationCity.getSelectedItem().toString();

        resultSalary.setText("");
        salary.setError(null);
        labelDestinationCity.setError(null);

        boolean salaryError = false;
        boolean cityError = false;

        if (sal.length() <= 0) {
            salaryError = true;
        }
        else if (Integer.parseInt(sal) <= 0) {
            salaryError = true;
        }
        // TODO: use XML somehow
        if (salaryError) {
            salary.setError("Salary Required");
        }
        if (ic.contentEquals(dc)) {
            cityError = true;
            // TODO: use XML somehow
            labelDestinationCity.setError("Cities Must Be Different");
        }

        if (!salaryError && !cityError) {
                String finalSal = convertSalary(sal, ic, dc);
                resultSalary.setText(finalSal);
        }
    }

    public String convertSalary(String salary, String initalCity, String destinationCity) {

        Map<String, Double> col_indices = new HashMap<String, Double>();
        col_indices.put("Atlanta, GA", 158.0);
        col_indices.put("Athens, GA", 140.0);
        col_indices.put("New York City, NY", 227.0);
        col_indices.put("Austin, TX", 151.0);
        col_indices.put("Seattle, WA", 197.0);
        col_indices.put("San Francisco, CA", 243.0);
        col_indices.put("Washington D.C.", 220.0);
        col_indices.put("Boston, MA", 201.0);
        col_indices.put("Tampa, FL", 143.0);
        col_indices.put("Las Vegas, NV", 148.0);

        // TODO: check for errors here? Or unnecessary?
        double oldSal = Double.parseDouble(salary);

        if (!col_indices.containsKey(initalCity) || !col_indices.containsKey(destinationCity)) {
            return "ERROR";
        }

        long newSal = Math.round(oldSal *
                (col_indices.get(destinationCity) / col_indices.get(initalCity)));

        return String.valueOf(newSal);

    }
}
