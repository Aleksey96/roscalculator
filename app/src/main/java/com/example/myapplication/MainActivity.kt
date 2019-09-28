package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.*
import android.widget.Toast.makeText
import java.lang.Integer.parseInt
import java.lang.Double.parseDouble
import java.lang.String.valueOf
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val display=windowManager.defaultDisplay
        val metrics=DisplayMetrics()
        display.getMetrics(metrics)

        findViewById<Button>(R.id.button).setOnClickListener {
            this.setCalculatedValue()
        }
    }

    //STANDART
    //---------------------------------------------------------------------------------------------------------------
    fun calculatePercentage(doublePlanCollectionMinus180: Double, doubleFactCollectionMinus180: Double): Double =
        (if ((doublePlanCollectionMinus180 / doubleFactCollectionMinus180) < 0.75) {
            0.75
        } else {
            0.75
        })

    fun getPercentage(post: String, doublePlanCollectionMinus180: Double, doubleFactCollectionMinus180: Double): Double =
        if (post.equals("ст.менеджер")) {
            calculatePercentage(doublePlanCollectionMinus180, doubleFactCollectionMinus180)
        } else {
            0.75
        }

    fun getSumMoney(post: String, doublePlanCollectionMinus180: Double, doubleFactCollectionMinus180: Double,
                    doubleRealtyOnBalance: Double): Double =
        if ((doubleFactCollectionMinus180 + doubleRealtyOnBalance / 2) >= doublePlanCollectionMinus180 *
            getPercentage(post, doublePlanCollectionMinus180, doubleFactCollectionMinus180)
        ) {
            (doubleFactCollectionMinus180 + doubleRealtyOnBalance / 2) * 0.01
        } else {
            0.0
        }

    fun getStandart(post: String, doublePlanCollectionMinus180: Double, doubleFactCollectionMinus180: Double,
                    doubleRealtyOnBalance: Double, doubleCarSum: Double): Double {
        return doubleCarSum * 0.01 + getSumMoney(
            post,
            doublePlanCollectionMinus180,
            doubleFactCollectionMinus180,
            doubleRealtyOnBalance
        )
    }
    //----------------------------------------------------------------------------------------------------------------


    //TARGET
    //----------------------------------------------------------------------------------------------------------------
    fun getSumBetween0_75(intFactCollectionPlus180: Double, intPlanCollectionPlus180: Double): Double =
        if (intFactCollectionPlus180 >= intPlanCollectionPlus180 * 0.75)
        {intPlanCollectionPlus180 * 0.75 * 0.01}
        else
        {0.0}


    fun getSumBetween75_100(post: String, doubleFactCollectionPlus180: Double, doublePlanCollectionPlus180: Double): Double {
        var expression1: Double
        var expression2: Double
        var expression4: Double

        if (doubleFactCollectionPlus180 <= doublePlanCollectionPlus180)
            expression1 = (doubleFactCollectionPlus180 - doublePlanCollectionPlus180 * 0.75) * 0.02
        else
            expression1 = (doublePlanCollectionPlus180 - doublePlanCollectionPlus180 * 0.75) * 0.02

        if (doubleFactCollectionPlus180 > doublePlanCollectionPlus180 * 0.75)
            expression2 = expression1
        else
            expression2 = 0.0

        if (doubleFactCollectionPlus180 > doublePlanCollectionPlus180 * 0.75)
            expression4 = expression1
        else
            expression4 = 0.0

        if (post.equals("ст.менеджер"))
            return expression4
        else
            return expression2

    }

    fun getSumBetween100_160(doubleFactCollectionPlus180: Double, doublePlanCollectionPlus180: Double): Double {
        var expression1: Double
        if (doubleFactCollectionPlus180 <= doublePlanCollectionPlus180 * 1.6)
            expression1 = (doubleFactCollectionPlus180 - doublePlanCollectionPlus180) * 0.05
        else
            expression1 = ((doublePlanCollectionPlus180 * 1.6)-doublePlanCollectionPlus180)*0.05
        if (doubleFactCollectionPlus180 > doublePlanCollectionPlus180)
            return expression1
        else
            return 0.0
    }

    fun getSumAfter160(doublePlanCollectionPlus180: Double, doubleFactCollectionPlus180: Double): Double {
        if (doubleFactCollectionPlus180 > doublePlanCollectionPlus180 * 1.6)
            return (doubleFactCollectionPlus180 - doublePlanCollectionPlus180 * 1.6) * 0.1
        else
            return 0.0
    }

    fun getTarget(post: String, doubleFactCollectionPlus180: Double, doublePlanCollectionPlus180: Double): Double {
        return getSumBetween0_75(doubleFactCollectionPlus180, doublePlanCollectionPlus180) +
                getSumBetween75_100(post, doubleFactCollectionPlus180, doublePlanCollectionPlus180) +
                getSumBetween100_160(doubleFactCollectionPlus180, doublePlanCollectionPlus180) +
                getSumAfter160(doublePlanCollectionPlus180, doubleFactCollectionPlus180)
    }
    //----------------------------------------------------------------------------------------------------------------


    //FIX
    //----------------------------------------------------------------------------------------------------------------
    fun includRB(post: String, confiscationRB: Int): Double =
        if (!post.equals("ст.менеджер") && confiscationRB != 0) {
            (confiscationRB * 5000).toDouble()
        } else {
            0.0
        }

    fun setBalance(post: String, carBalance: Int): Double =
        if (!post.equals("ст.менеджер") && carBalance > 0) {
            (carBalance * 5000).toDouble()
        } else {
            0.0
        }

    fun includBP(post: String, confiscationBP: Int): Double =
        if (!post.equals("ст.менеджер") && confiscationBP != 0) {
            (confiscationBP * 5000).toDouble()
        } else {
            0.0
        }

    fun setIC(post: String, strIC: Int): Double =
        if (!post.equals("ст.менеджер") && strIC != 0) {
            (strIC * 5000).toDouble()
        } else {
            0.0
        }

    fun setGSM(post: String, param1: Double, param2: Double): Double =
        if (!post.equals("ст.менеджер")) {
            ((param1/100 * 0.50) + (param2/100 * 0.50)) * 13200
        } else {
            0.0
        }

    fun getFix(
        post: String, carBalance: Int, confiscationBP: Int, confiscationRB: Int,
        strIC: Int, param1: Double, param2: Double, strSalary: Double
    ): Double {
        var result = includBP(post, confiscationBP) + includRB(post, confiscationRB) + setBalance(post, carBalance) +
                setGSM(post, param1, param2) + setIC(post, strIC)
        if (result > strSalary) {
            return strSalary
        } else
            return result
    }
    //---------------------------------------------------------------------------------------------------------------

    fun setCalculatedValue() {
        var planCollectionMinus180 = findViewById<EditText>(R.id.editText8)

        var factCollectionMinus180 = findViewById<EditText>(R.id.editText16)

        var planCollectionPlus180 = findViewById<EditText>(R.id.editText7)

        var factCollectionPlus180 = findViewById<EditText>(R.id.editText6)

        var confiscationCarRB = findViewById<EditText>(R.id.editText20)

        var confiscationCarBP = findViewById<EditText>(R.id.editText19)

        var spinnerDivision = findViewById<Spinner>(R.id.spinner3)

        var providerIC = findViewById<EditText>(R.id.editText18)

        var spinnerKRUK = findViewById<EditText>(R.id.editText25)

        var spinnerPost = findViewById<Spinner>(R.id.spinner2)

        var spinnerKOT = findViewById<EditText>(R.id.editText26)

        var parametr2 = findViewById<EditText>(R.id.editText28)

        var parametr1 = findViewById<EditText>(R.id.editText27)

        var salary = findViewById<EditText>(R.id.editText21)//доход

        var realtyOnBalance = findViewById<EditText>(R.id.editText22)

        var realiseCarSum = findViewById<EditText>(R.id.editText5)

        var carOnBalance = findViewById<EditText>(R.id.editText4)

        try {

            var doublePlanCollectionMinus180 = parseDouble(planCollectionMinus180.text?.toString() ?: "0")

            var doubleFactCollectionMinus180 = parseDouble(factCollectionMinus180.text.toString())

            var doublePlanCollectionPlus180 = parseDouble(planCollectionPlus180.text.toString())

            var doubleFactCollectionPlus180 = parseDouble(factCollectionPlus180.text.toString())

            var intConfiscationCarRB = parseInt(confiscationCarRB.text.toString())

            var intConfiscationCarBP = parseInt(confiscationCarBP.text.toString())

            var doubleRealtyOnBalance = parseDouble(realtyOnBalance.text.toString())

            var intCarBalance = parseInt(carOnBalance.text.toString())

            var doubleCarSum = parseDouble(realiseCarSum.text.toString())

            var division = parseInt(valueOf(spinnerDivision.getSelectedItem()))

            var doubleSalary = parseDouble(salary?.text?.toString() ?: "0")

            var intIC = parseInt(providerIC.text.toString())

            var doubleParametr1 = parseDouble(parametr1.text.toString())

            var doubleParametr2 = parseDouble(parametr2.text.toString())

            var KRUK = parseDouble(spinnerKRUK.text.toString())

            var post = valueOf(spinnerPost.getSelectedItem())

            var KOT = parseDouble(spinnerKOT.text.toString())


                var resultTarget = getTarget(post, doubleFactCollectionPlus180, doublePlanCollectionPlus180)
                var resultStandart = getStandart(
                    post,
                    doublePlanCollectionMinus180,
                    doubleFactCollectionMinus180,
                    doubleRealtyOnBalance,
                    doubleCarSum
                )
                var resultFix = getFix(
                    post, intCarBalance, intConfiscationCarBP, intConfiscationCarRB,
                    intIC, doubleParametr1, doubleParametr2, doubleSalary
                )

                var expression: Double
                var expression1: Double
                var expression2: Double
                var expression3: Double
                var expression4: Double

                var result = (resultFix + resultStandart + resultTarget) * KRUK * KOT

                if (result > doubleSalary * 2)
                    expression1 = doubleSalary * 2
                else
                    expression1 = result

                if (result > doubleSalary * 2.5)
                    expression2 = doubleSalary * 2.5
                else
                    expression2 = result

                if (division == 2)
                    expression3 = expression2
                else
                    expression3 = expression1

                if (result > doubleSalary * 3)
                    expression4 = doubleSalary * 3
                else
                    expression4 = result

                if (division == 1)
                    expression = expression4
                else
                    expression = expression3

                if (expression > 2 * doubleSalary)

                    findViewById<TextView>(R.id.textView33).text = String.format(Locale.US,"%.2f",(2 * doubleSalary))
                else
                    findViewById<TextView>(R.id.textView33).text = String.format(Locale.US,"%.2f",expression)
        }
        catch(ex:NumberFormatException) {
            findViewById<EditText>(R.id.editText8).setText("")

            findViewById<EditText>(R.id.editText16).setText("")

            findViewById<EditText>(R.id.editText7).setText("")

            findViewById<EditText>(R.id.editText6).setText("")

            findViewById<EditText>(R.id.editText20).setText("")

            findViewById<EditText>(R.id.editText19).setText("")

            findViewById<EditText>(R.id.editText18).setText("")

            findViewById<EditText>(R.id.editText25).setText("")

            findViewById<EditText>(R.id.editText26).setText("")

            findViewById<EditText>(R.id.editText28).setText("")

            findViewById<EditText>(R.id.editText27).setText("")

            findViewById<EditText>(R.id.editText21).setText("")

            findViewById<EditText>(R.id.editText22).setText("")

            findViewById<EditText>(R.id.editText5).setText("")

            findViewById<EditText>(R.id.editText4).setText("")

            makeText(
                this,
                "Введены некорректные значения",
                Toast.LENGTH_LONG
            ).show()
        }
        }

    }


