package com.example.curtainpricecalc

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ViewUtils.hideKeyboard
import kotlin.math.ceil
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val RINGS_FACTOR = 3.5
    private val THREE_FLEET_FACTOR = 2.5

    private lateinit var styleSpinner: Spinner
    private lateinit var materialWidthSpinner: Spinner
    private lateinit var widthEntry: EditText
    private lateinit var heightEntry: EditText
    private lateinit var priceLabel1Entry: EditText
    private lateinit var priceLabel2Entry: EditText
    private lateinit var priceLabel3Entry: EditText
    private lateinit var priceLabel4Entry: EditText
    private lateinit var calculateButton: Button
    private lateinit var resultTable : TableLayout
    private lateinit var price_table_value_1: TextView
    private lateinit var price_table_value_2: TextView
    private lateinit var price_table_value_3: TextView
    private lateinit var price_table_value_4: TextView
    private lateinit var price_table_value_5: TextView
    private lateinit var price_table_value_6: TextView
    private lateinit var price_table_value_7: TextView
    private lateinit var hideRow1: TableRow
    private lateinit var hideRow2: TableRow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views by their IDs
        styleSpinner = findViewById(R.id.style_spinner)
        materialWidthSpinner = findViewById(R.id.material_width_spinner)
        widthEntry = findViewById(R.id.width_entry)
        heightEntry = findViewById(R.id.height_entry)
        priceLabel1Entry = findViewById(R.id.price_label_1_entry)
        priceLabel2Entry = findViewById(R.id.price_label_2_entry)
        priceLabel3Entry = findViewById(R.id.price_label_3_entry)
        priceLabel4Entry = findViewById(R.id.price_label_4_entry)
        calculateButton = findViewById(R.id.calculate_button)
        resultTable = findViewById(R.id.result_table)
        price_table_value_1 = findViewById(R.id.price_table_value_1)
        price_table_value_2 = findViewById(R.id.price_table_value_2)
        price_table_value_3 = findViewById(R.id.price_table_value_3)
        price_table_value_4 = findViewById(R.id.price_table_value_4)
        price_table_value_5 = findViewById(R.id.price_table_value_5)
        price_table_value_6 = findViewById(R.id.price_table_value_6)
        price_table_value_7 = findViewById(R.id.price_table_value_7)
        hideRow1 = findViewById(R.id.row1)
        hideRow2 = findViewById(R.id.row2)

        resultTable.visibility = TableLayout.GONE


        // Set up spinners
        ArrayAdapter.createFromResource(
            this,
            R.array.style_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            styleSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.material_width_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            materialWidthSpinner.adapter = adapter
        }

        // Set click listener for the calculate button
        calculateButton.setOnClickListener {
            calculateResult()
        }
    }

    private fun calculationMethod1(width: Double): Double {
        val requiredYards = (width / 36)
        return String.format("%.2f", requiredYards).toDouble()
    }

    private fun calculationMethod2(width: Double, height: Double): Double {
        val requiredPartitions = ceil(width / 60)
        var requiredYards = (height / 36)
        requiredYards = String.format("%.2f", requiredYards).toDouble()
        var totalYards = requiredYards * requiredPartitions
        totalYards = String.format("%.2f", totalYards).toDouble()
        return totalYards
    }

    private fun priceSelector(): Double {
        val materialWidthOption = materialWidthSpinner.selectedItem.toString()
        return if (materialWidthOption == "110 inches") {
            priceLabel1Entry.text.toString().toDouble()
        } else {
            priceLabel2Entry.text.toString().toDouble()
        }
    }

    private fun ielotResult(requiredYards: Double, width: Double) {
        resultTable.visibility = TableLayout.VISIBLE
        hideRow1.visibility = TableRow.VISIBLE
        hideRow2.visibility = TableRow.VISIBLE

        val price = priceSelector()
        val stipPrice = priceLabel3Entry.text.toString().toDouble()
        val ielotPrice = priceLabel4Entry.text.toString().toDouble()
        val method = materialWidthSpinner.selectedItem.toString()

        price_table_value_1.text = "${requiredYards} Yards"
        price_table_value_2.text = "${requiredYards} Yards"

        val ielotCount = if (method == "110 inches") {
            ceil(requiredYards * 5).toInt()
        } else {
            ((ceil(width / 60) * 60) / 36 * 5).toInt()
        }
        var ielotTotalPrice = ielotCount * ielotPrice
        ielotTotalPrice = String.format("%.2f", ielotTotalPrice).toDouble()


        price_table_value_3.text = "${ielotCount} IELOTS"

        var price_for_material = requiredYards * price
        price_for_material = String.format("%.2f", price_for_material).toDouble()
        price_table_value_4.text = "Rs. ${price_for_material}"


        var price_for_strips = requiredYards * stipPrice
        price_for_strips = String.format("%.2f", price_for_strips).toDouble()
        price_table_value_5.text = "Rs. ${price_for_strips}"

        price_table_value_6.text = "Rs. ${ielotTotalPrice}"

        var total = (requiredYards*price )+(requiredYards * stipPrice)+(ceil(requiredYards * 5) * ielotPrice)
        total = String.format("%.2f", total).toDouble()
        price_table_value_7.text = "Rs. ${total}"

    }

    private fun threeFleetResult(requiredYards: Double) {
        resultTable.visibility = TableLayout.VISIBLE
        hideRow1.visibility = TableRow.GONE
        hideRow2.visibility = TableRow.GONE

        val price = priceSelector()
        val stipPrice = priceLabel3Entry.text.toString().toDouble()



        price_table_value_1.text = "${requiredYards} Yards"
        price_table_value_2.text = "${requiredYards} Yards"

        var price_for_material = requiredYards * price
        price_for_material = String.format("%.2f", price_for_material).toDouble()
        price_table_value_4.text = "Rs. ${price_for_material}"


        var price_for_strips = requiredYards * stipPrice
        price_for_strips = String.format("%.2f", price_for_strips).toDouble()
        price_table_value_5.text = "Rs. ${price_for_strips}"


        var total = (requiredYards*price) + (requiredYards * stipPrice)
        total = String.format("%.2f", total).toDouble()
        price_table_value_7.text = "Rs. ${total}"

    }

    fun  Context.hideKeyboard(activity: AppCompatActivity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    private fun calculateResult() {

        //Close the keyboard
           hideKeyboard(this)


        // Get values from EditText fields
        val width = widthEntry.text.toString().toFloatOrNull() ?: 0f
        val height = heightEntry.text.toString().toFloatOrNull() ?: 0f
        val price1 = priceLabel1Entry.text.toString().toFloatOrNull() ?: 0f
        val price2 = priceLabel2Entry.text.toString().toFloatOrNull() ?: 0f
        val price3 = priceLabel3Entry.text.toString().toFloatOrNull() ?: 0f
        val price4 = priceLabel4Entry.text.toString().toFloatOrNull() ?: 0f

        // Get selected spinner values
        val selectedStyle = styleSpinner.selectedItem.toString()
        val selectedWidth = materialWidthSpinner.selectedItem.toString()

        try {
            val inputWidth = widthEntry.text.toString().toDouble()
            val inputHeight = heightEntry.text.toString().toDouble()

            if (inputHeight > 110) {

                Toast.makeText(this, "Window height is higher than material height for 110 length material.", Toast.LENGTH_SHORT).show()
                resultTable.visibility = TableLayout.GONE

            } else {
                val styleOption = styleSpinner.selectedItem.toString()
                val materialWidthOption = materialWidthSpinner.selectedItem.toString()

                val requiredWidth = if (styleOption == "Rings") {
                    inputWidth * RINGS_FACTOR
                } else {
                    inputWidth * THREE_FLEET_FACTOR
                }

                val requiredYards = if (materialWidthOption == "110 inches") {
                    calculationMethod1(requiredWidth)
                } else {
                    calculationMethod2(requiredWidth, inputHeight)
                }

                if (styleOption == "Rings") {
                    ielotResult(requiredYards, requiredWidth)
                } else {
                    threeFleetResult(requiredYards)
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            resultTable.visibility = TableLayout.GONE
        }
    }


}
