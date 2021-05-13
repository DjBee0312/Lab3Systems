package ua.kpi.comsys.lab3system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import kotlin.math.absoluteValue
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun calculate(view: View){
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val textView = findViewById<TextView>(R.id.id_answer)
        val number = editText.text.toString().toInt()
        val ans = Fermat(number)
        textView.apply {
            text = ans
        }
    }

    fun calculate2(view: View){
        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val spinner3 = findViewById<Spinner>(R.id.spinner3)
        val selected1 = spinner1.selectedItem.toString().toDouble()
        val selected2 = spinner2.selectedItem.toString().toDouble()
        val selected3 = spinner3.selectedItem.toString().toInt()
        val textView = findViewById<TextView>(R.id.id_answer2)

        val answer = Perceptron(selected1,selected2,selected3)

        textView.apply {
            text = answer
        }
    }

    fun calculate3(view: View){
        val editText1 = findViewById<EditText>(R.id.editTextNumber)
        val editText2 = findViewById<EditText>(R.id.editTextNumber2)
        val editText3 = findViewById<EditText>(R.id.editTextNumber3)
        val editText4 = findViewById<EditText>(R.id.editTextNumber4)
        val editText5 = findViewById<EditText>(R.id.editTextNumber5)
        val textView = findViewById<TextView>(R.id.id_answer3)
        val x1 = editText1.text.toString().toDouble()
        val x2 = editText2.text.toString().toDouble()
        val x3 = editText3.text.toString().toDouble()
        val x4 = editText4.text.toString().toDouble()
        val y = editText5.text.toString().toDouble()
        val ans = Evo(x1,x2,x3,x4,y)
        textView.apply {
            text = ans
        }
    }

    private fun Fermat(number : Int) : String
    {
        //number = ((a + b) * (a - b))) = a^2 - b^2        30
        if (number % 2 == 0){
            return "Please enter odd number!"
        }
        val numberSqrt = kotlin.math.sqrt(number.toDouble()).toInt() + 1     //5,449287429383 + 1 = 6
        var k = 0
        var iter = 0
        var bPow = 0
        while (k == 0)
        {
            bPow = ((numberSqrt + iter).toDouble()).pow(2.00).toInt() - number  //6 -> 36  -> 6     //7 -> 49 -> 19
            if((kotlin.math.sqrt(bPow.toDouble()))% 1.0 == 0.0){
                k = iter
                break
            } else iter++
        }
        val a : Int = numberSqrt + k
        val b : Int = kotlin.math.sqrt(bPow.toDouble()).toInt()
        return mutableListOf(a, b).toString()
    }

    private fun Evo(x1_base: Double,
                    x2_base: Double,
                    x3_base: Double,
                    x4_base: Double,
                    y_base: Double) : String{

        val populationZero: MutableList<MutableList<Double>> = mutableListOf()
        val population: MutableList<MutableList<Double>> = mutableListOf()
        val listOfFitnesses: MutableList<Double> = mutableListOf()
        val populationOfChild: MutableList<MutableList<Double>> = mutableListOf()
        var bestPopulation: MutableList<Double> = mutableListOf()

        fun fitness(population: MutableList<Double>): Double {
            val fitness: Double = y_base -
                    population[0] * x1_base -
                    population[1] * x2_base -
                    population[2] * x3_base -
                    population[3] * x4_base
            return fitness.absoluteValue
        }

        fun populationZeroFind() {
            for (i in 0..3) {
                populationZero.add(mutableListOf())
                for (j in 0..3) {
                    populationZero[i].add((1..8).random().toDouble())
                }
            }
        }

        fun findFitnessOfPopulation() {
            listOfFitnesses.clear()
            if (population.isEmpty()) {
                populationZero.mapTo(population) { it }
            }
            for (i in 0..3) {
                listOfFitnesses.add(fitness(population[i]))
            }
        }

        fun findRoulette() {
            populationOfChild.clear()
            var roulette = 0.00
            val roulettePercent: MutableList<Double> = mutableListOf()
            val circleRoulette: MutableList<Double> = mutableListOf()
            listOfFitnesses.forEach { roulette += 1 / it }
            for (i in 0..3) {
                roulettePercent.add(1 / listOfFitnesses[i] / roulette)
            }

            for (i in 0..3) {
                if (i == 0) {
                    circleRoulette.add(roulettePercent[i])
                } else {
                    circleRoulette.add(circleRoulette[i - 1] + roulettePercent[i])
                }
            }

            var i = 0
            populationOfChild.clear()
            while (i < 4) {
                val piu: Double = (1..100).random().toDouble() / 100
                var thisChild = 0
                for (k in 0..3) {
                    if (piu >= circleRoulette[k]) {
                        thisChild = k
                    }
                }
                populationOfChild.add(population[thisChild])
                i++
            }
        }

        fun crossingOver() {
            population.clear()
            for (p in 0..3) {
                val c: MutableList<Double> = mutableListOf()
                c.clear()
                for (j in 0..3) {
                    if (p % 2 == 0) {
                        if (j < 2) {
                            c.add(populationOfChild[p][j])
                        } else c.add(populationOfChild[p + 1][j])
                    } else
                        if (j < 2) {
                            c.add(populationOfChild[p][j])
                        } else c.add(populationOfChild[p - 1][j])
                }
                population.add(c)
            }
        }

        fun bestFitnessFind(): Boolean {
            findFitnessOfPopulation()
            listOfFitnesses.forEach { if (it == 0.0) return true }
            return false
        }

        fun life() {
            var q = 0
            while (!bestFitnessFind() && q < 10) {
                findFitnessOfPopulation()
                findRoulette()
                crossingOver()
                q++
            }
        }

        fun result(): MutableList<Double> {
            populationZeroFind()
            life()
            while ((!listOfFitnesses.contains(0.0)) &&
                    population[0] == populationOfChild[0] &&
                    population[1] == populationOfChild[1] &&
                    population[2] == populationOfChild[2] &&
                    population[3] == populationOfChild[3]
            ) {
                populationZero.clear()
                population.clear()
                listOfFitnesses.clear()
                populationOfChild.clear()
                populationZeroFind()
                life()
            }
            for (i in 0..3) {
                if (listOfFitnesses[i] == 0.0) {
                    bestPopulation = population[i]
                }
            }
            return bestPopulation
        }
        
        val answer : MutableList<Double>  =  result();
        if (answer.isEmpty()) {
            return "No possible answer in range [1;y/2]";
        }
        return answer.toString();
    }

    private fun Perceptron (speed: Double, time: Double, iterations: Int): String{
        var W1 = 0.00
        var W2 = 0.00
        val P = 4.00
        val points = arrayListOf(Pair(0.00, 6.00), Pair(1.00, 5.00), Pair(3.00, 3.00), Pair(2.00, 4.00))

        fun check(): Boolean {
            for (i in 0 .. 3){
                var y = W1 * points[i].first + W2 * points[i].second
                if ((i < 2 && y < P) || (i >= 2 && y > P) ) return false
            }
            return true
        }

        fun result(): Pair<Double, Double> {
            val startTime = System.currentTimeMillis()
            for (i in 0..iterations) {
                if ((System.currentTimeMillis() - startTime) <= time * 1000) {
                    for (k in 0 until points.size) {
                        val y = W1 * points[k].first + W2 * points[k].second
                        val delta = P - y
                        W1 += delta * points[k].first * speed
                        W2 += delta * points[k].second * speed
                        if (check()) {
                            return Pair(W1, W2)
                        }
                    }
                }
            }
            return Pair(W1, W2)
        }

        val res =  result()
        if (res.first.isNaN() || res.first == Double.NEGATIVE_INFINITY || res.first == Double.POSITIVE_INFINITY || res.second.isNaN() || res.second == Double.NEGATIVE_INFINITY || res.second == Double.POSITIVE_INFINITY){
            return ("W1 is $W1, W2 is $W2 => ERROR")
        }
        return res.toString()
    }
}