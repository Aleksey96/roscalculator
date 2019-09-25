package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.makeText
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Integer.parseInt
import java.lang.Double.parseDouble
import java.lang.String.valueOf


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
    fun calculatePercentage(intPlanCollectionMinus180: Int, intFactCollectionMinus180: Int): Double =
        (if ((intPlanCollectionMinus180 / intFactCollectionMinus180) < 0.75) {
            0.75
        } else {
            (intPlanCollectionMinus180 / intFactCollectionMinus180).toDouble()
        })

    fun getPercentage(post: String, intPlanCollectionMinus180: Int, intFactCollectionMinus180: Int): Double =
        if (post.equals("ст.менеджер")) {
            calculatePercentage(intPlanCollectionMinus180, intFactCollectionMinus180)
        } else {
            0.75
        }

    fun getSumMoney(post: String, intPlanCollectionMinus180: Int, intFactCollectionMinus180: Int, doubleRealtyOnBalance: Double): Double =
        if ((intFactCollectionMinus180 + doubleRealtyOnBalance / 2) >= intPlanCollectionMinus180 *
            getPercentage(post, intPlanCollectionMinus180, intFactCollectionMinus180)
        ) {
            (intFactCollectionMinus180 + doubleRealtyOnBalance / 2) * 0.01
        } else {
            0.0
        }

    fun getStandart(post: String, intPlanCollectionMinus180: Int, intFactCollectionMinus180: Int, doubleRealtyOnBalance: Double, doubleCarSum: Double): Double {
        return doubleCarSum * 0.01 + getSumMoney(
            post,
            intPlanCollectionMinus180,
            intFactCollectionMinus180,
            doubleRealtyOnBalance
        )
    }
    //----------------------------------------------------------------------------------------------------------------


    //TARGET
    //----------------------------------------------------------------------------------------------------------------
    fun getSumBetween0_75(intFactCollectionPlus180: Int, intPlanCollectionPlus180: Int): Double =
        if (intFactCollectionPlus180 >= intPlanCollectionPlus180 * 0.75)
        {intPlanCollectionPlus180 * 0.75 * 0.01}
        else
        {0.0}


    fun getSumBetween75_100(post: String, intFactCollectionPlus180: Int, intPlanCollectionPlus180: Int): Double {
        var expression1: Double
        var expression2: Double
        var expression4: Double

        if (intFactCollectionPlus180 <= intPlanCollectionPlus180)
            expression1 = (intFactCollectionPlus180 - intPlanCollectionPlus180 * 0.75) * 0.02
        else
            expression1 = (intPlanCollectionPlus180 - intPlanCollectionPlus180 * 0.75) * 0.02

        if (intFactCollectionPlus180 > intPlanCollectionPlus180 * 0.75)
            expression2 = expression1
        else
            expression2 = 0.0

        if (intFactCollectionPlus180 > intPlanCollectionPlus180 * 0.75)
            expression4 = expression1
        else
            expression4 = 0.0

        if (post.equals("ст.менеджер"))
            return expression4
        else
            return expression2

    }

    fun getSumBetween100_160(intFactCollectionPlus180: Int, intPlanCollectionPlus180: Int): Double {
        var expression1: Double
        if (intFactCollectionPlus180 <= intPlanCollectionPlus180 * 1.6)
            expression1 = (intFactCollectionPlus180 - intPlanCollectionPlus180) * 0.05
        else
            expression1 = intPlanCollectionPlus180 * 1.55
        if (intFactCollectionPlus180 > intPlanCollectionPlus180)
            return expression1
        else
            return 0.0
    }

    fun getSumAfter160(intPlanCollectionPlus180: Int, intFactCollectionPlus180: Int): Double {
        if (intFactCollectionPlus180 > intPlanCollectionPlus180 * 1.6)
            return (intFactCollectionPlus180 - intPlanCollectionPlus180 * 1.6) * 0.1
        else
            return 0.0
    }

    fun getTarget(post: String, intFactCollectionPlus180: Int, intPlanCollectionPlus180: Int): Double {
        return getSumBetween0_75(intFactCollectionPlus180, intPlanCollectionPlus180) +
                getSumBetween75_100(post, intFactCollectionPlus180, intPlanCollectionPlus180) +
                getSumBetween100_160(intFactCollectionPlus180, intPlanCollectionPlus180) +
                getSumAfter160(intPlanCollectionPlus180, intFactCollectionPlus180)
    }
    //----------------------------------------------------------------------------------------------------------------


    //FIX
    //----------------------------------------------------------------------------------------------------------------
    fun includRB(post: String, confiscationRB: Int): Int =
        if (!post.equals("ст.менеджер") && confiscationRB != 0) {
            confiscationRB * 5000
        } else {
            0
        }

    fun setBalance(post: String, carBalance: Int): Int =
        if (!post.equals("ст.менеджер") && carBalance > 0) {
            carBalance * 5000
        } else {
            0
        }

    fun includBP(post: String, confiscationBP: Int): Int =
        if (!post.equals("ст.менеджер") && confiscationBP != 0) {
            confiscationBP * 5000
        } else {
            0
        }

    fun setIC(post: String, strIC: Int): Int =
        if (!post.equals("ст.менеджер") && strIC != 0) {
            strIC * 5000
        } else {
            0
        }

    fun setGSM(post: String, param1: Double, param2: Double): Double =
        if (!post.equals("ст.менеджер")) {
            ((param1 * 0.50) + (param2 * 0.50)) * 13200
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

        var realiseCarCount = findViewById<EditText>(R.id.editText5)

        var realiseCarSum = findViewById<EditText>(R.id.editText23)

        var carOnBalance = findViewById<EditText>(R.id.editText4)



        try {
            var intPlanCollectionMinus180 = parseInt(planCollectionMinus180.text?.toString() ?: "0")

            var intFactCollectionMinus180 = parseInt(factCollectionMinus180.text.toString())

            var intPlanCollectionPlus180 = parseInt(planCollectionPlus180.text.toString())

            var intFactCollectionPlus180 = parseInt(factCollectionPlus180.text.toString())

            var intConfiscationCarRB = parseInt(confiscationCarRB.text.toString())

            var intConfiscationCarBP = parseInt(confiscationCarBP.text.toString())

            var doubleRealtyOnBalance = parseDouble(realtyOnBalance.text.toString())

            var intCarCount = parseInt(realiseCarCount.text.toString())

            var intCarBalance = parseInt(carOnBalance.text.toString())

            var doubleCarSum = parseDouble(realiseCarSum.text.toString())

            var division = parseInt(valueOf(spinnerDivision.getSelectedItem()))

            var doubleSalary = parseDouble(salary?.text?.toString() ?: "0")

            var intIC = parseInt(providerIC.getText().toString())

            // по умолчанию выводится первый элемент списка
            var doubleParametr1 = parseDouble(parametr1.text.toString())

            var doubleParametr2 = parseDouble(parametr2.text.toString())

            var KRUK = parseDouble(spinnerKRUK.text.toString())

            var post = valueOf(spinnerPost.getSelectedItem())

            var KOT = parseDouble(spinnerKOT.text.toString())

            if (!doubleSalary.equals("") && !doubleCarSum.equals("") && !intCarCount.equals("") && !intPlanCollectionMinus180.equals("")
                and !intPlanCollectionPlus180.equals("") && !intFactCollectionMinus180.equals("") && !intFactCollectionPlus180.equals("")
                and !doubleRealtyOnBalance.equals("") && !intCarBalance.equals("") && !intIC.equals("") && !intConfiscationCarBP.equals("")
                && !intConfiscationCarRB.equals("")) {

                var resultTarget = getTarget(post, intFactCollectionPlus180, intPlanCollectionPlus180)
                var resultStandart = getStandart(
                    post,
                    intPlanCollectionMinus180,
                    intFactCollectionMinus180,
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

                findViewById<TextView>(R.id.textView33).text=expression.toString()
                } else {
                    var toast = makeText(this,"Не все поля заполнены", Toast.LENGTH_LONG)
                    toast.show()
                }

            }
        catch(ex:NumberFormatException) {
            makeText(
                this,
                "Преобразование прошло некорректно",
                Toast.LENGTH_LONG
            ).show()
        }
        }

    }


