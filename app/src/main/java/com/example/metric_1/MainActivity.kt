package com.example.metric_1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var metricSpinner: Spinner
    private lateinit var fromUnitSpinner: Spinner
    private lateinit var toUnitSpinner: Spinner
    private lateinit var valueEditText: EditText
    private lateinit var convertButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        metricSpinner = findViewById(R.id.metricSpinner)
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner)
        toUnitSpinner = findViewById(R.id.toUnitSpinner)
        valueEditText = findViewById(R.id.valueEditText)
        convertButton = findViewById(R.id.convertButton)
        resultTextView = findViewById(R.id.resultTextView)

        val metrics = resources.getStringArray(R.array.metrics_array)
        val lengthUnits = resources.getStringArray(R.array.length_units_array)
        val weightUnits = resources.getStringArray(R.array.weight_units_array)
        val tempUnits = resources.getStringArray(R.array.temperature_units_array)

        val metricsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, metrics)
        metricSpinner.adapter = metricsAdapter

        metricSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> setUnitSpinner(lengthUnits)
                    1 -> setUnitSpinner(weightUnits)
                    2 -> setUnitSpinner(tempUnits)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        convertButton.text = getString(R.string.convert_button)

        convertButton.setOnClickListener {
            val inputValue = valueEditText.text.toString().toDoubleOrNull()
            if (inputValue != null) {
                val result = convertValue(inputValue)
                resultTextView.text = getString(R.string.result_prefix, result.toString())
            } else {
                Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUnitSpinner(units: Array<String>) {
        val unitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        fromUnitSpinner.adapter = unitAdapter
        toUnitSpinner.adapter = unitAdapter
    }

    private fun convertValue(value: Double): Double {
        val fromUnit = fromUnitSpinner.selectedItem.toString()
        val toUnit = toUnitSpinner.selectedItem.toString()
        var result = value

        when (fromUnit) {
            "Meter" -> {
                when (toUnit) {
                    "Centimeter" -> result *= 100
                    "Kilometer" -> result /= 1000
                }
            }
            "Centimeter" -> {
                when (toUnit) {
                    "Meter" -> result /= 100
                    "Kilometer" -> result /= 100000
                }
            }
            "Kilometer" -> {
                when (toUnit) {
                    "Meter" -> result *= 100
                    "Centimeter" -> result *= 100000
                }
            }
            "Kilogram" -> {
                when (toUnit) {
                    "Gram" -> result *= 1000
                    "Milligram" -> result *= 1000
                }
            }
            "Gram" -> {
                when (toUnit) {
                    "Kilogram" -> result /= 1000
                    "Milligram" -> result *= 1000
                }
            }
            "Milligram" -> {
                when (toUnit) {
                    "Kilogram" -> result /= 1000000
                    "Gram" -> result /= 1000
                }
            }
            "Celsius" -> {
                when (toUnit) {
                    "Fahrenheit" -> result = (value * 9 / 5) + 32
                    "Kelvin" -> result = value + 273.15
                }
            }
            "Fahrenheit" -> {
                when (toUnit) {
                    "Celsius" -> result = (value - 32) * 5 / 9
                    "Kelvin" -> result = (value + 459.67) * 5 / 9
                }
            }
            "Kelvin" -> {
                when (toUnit) {
                    "Celsius" -> result = value - 273.15
                    "Fahrenheit" -> result = (value * 9 / 5) - 459.67
                }
            }

        }

        return result
    }


}


