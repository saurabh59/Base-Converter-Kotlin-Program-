package converter
import java.math.BigInteger
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

fun integerConverter(integer:String, a:Int, b:Int):String {
    var power = 0.0
    var decimal = BigInteger("0")
    for (x in integer.reversed()) {
        val int:Int = if (x.isLetter()) {
            10 + (x.uppercaseChar() - 'A')
        } else {
            x.toString().toInt()
        }
        decimal += BigInteger((int * a.toDouble().pow(power)).toLong().toString())
        power++
    }
    var q = decimal
    var s = ""
    while (q >= b.toBigInteger()) {
        q = decimal / b.toBigInteger()
        val r = decimal % b.toBigInteger()
        if (r > BigInteger("9")) {
            val rEncoded = 'A' + (r.toInt() - 10)
            s += rEncoded
            decimal = q
            continue
        } else {
            s += r.toString()
            decimal = q
            continue
        }
    }
    s += if (q > BigInteger("9")) {
        val qBig = 'A' + (q.toInt() - 10)
        qBig.toString()
    } else {
        q.toString()
    }
    return s.reversed()
}
fun isNumber(s: String): Boolean {
    return when(s.toIntOrNull())
    {
        null -> false
        else -> true
    }
}
fun fractionalConverter(fractional:String, source:Int, target:Int):String {
    var power = -1.0
    var fractionalDecimal = BigDecimal("0")
    for (x in fractional.subSequence(0,6)) {
        if (x.isLetter()) {
            val r = (10 + (x.uppercaseChar() - 'A')).toString()
            fractionalDecimal += BigDecimal(r) * source.toDouble().pow(power).toBigDecimal()
            power--
        } else {
            fractionalDecimal += BigDecimal(x.toString()) * source.toDouble().pow(power).toBigDecimal()
            power--
        }
    }
    fractionalDecimal = fractionalDecimal.setScale(6, RoundingMode.CEILING)
    var product: BigDecimal
    var s = ""
    var m:String
    var n = BigDecimal("1")
    var counter = 0
    while (s.length != 5 && n != BigDecimal("0")) {
        product = fractionalDecimal * BigDecimal(target.toString())
        m = product.toString().split(".")[0]
        n = BigDecimal(product.toString().split(".")[1])
        if (m.toInt() > 9) {
            m = ('A' + (m.toInt() - 10)).toString()
        }
        s += m
        fractionalDecimal = BigDecimal("0." + product.toString().split(".")[1])
        counter += 1
    }
    return if (s.length < 5) {
        s + "0".repeat (5 - n.toString().length)
    } else {
        s
    }
}

fun main() {
    println("Enter two number in format: {source base} {target base} (To quit type /exit)")
    var mainMenu = readLine()!!
    while (mainMenu != "/exit") {
        val (a, b) = mainMenu.split(" ").map { it.toInt() }
        println("Enter number in base $a and to convert to base $b (To go back type /back)")
        var subMenu = readLine()!!
        while (subMenu != "/back") {
            if ("." in subMenu) {
                val (integer, fractional) = subMenu.split(".")
                if (isNumber(fractional)) {
                    if (fractional.toInt() == 0) {
                        println("Conversion result: ${integerConverter(integer, a, b)}.00000")
                    }
                    else {
                        val fracResult = fractionalConverter(fractional, a, b).take(5)
                        kotlin.io.println("Conversion result: ${integerConverter(integer, a, b)}.$fracResult")
                        kotlin.io.println()
                    }
                }
                else {
                    val fracResult = fractionalConverter(fractional, a, b).take(5)
                    kotlin.io.println("Conversion result: ${integerConverter(integer, a, b)}.$fracResult")
                    kotlin.io.println()
                }

            } else {
                println("Conversion result: ${integerConverter(subMenu, a, b)}")
            }
            println("Enter number in base $a and to convert to base $b (To go back type /back)")
            subMenu = readLine()!!
            continue
        }
        println()
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        mainMenu = readLine()!!
        continue
    }
}