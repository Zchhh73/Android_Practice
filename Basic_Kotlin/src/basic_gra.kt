import kotlin.math.min

var aBoolean: Boolean = true
val anotherBoolean: Boolean = false

val anInt:Int = 8
val anotherInt:Int = 0xFF
val moreInt:Int = 0b00000011
val maxInt:Int = Int.MAX_VALUE
val minInt:Int = Int.MIN_VALUE

val aLong:Long = 123213131323
val another:Long = 123
val maxLong:Long = Long.MAX_VALUE
val minLong:Long = Long.MIN_VALUE

val aFloat: Float = 2.0F
val anotherFloat:Float = 1E3f
val maxFloat:Float = Float.MAX_VALUE
val minFloat:Float = -Float.MAX_VALUE

val aDouble:Double = 3.0
val maxDouble:Double = Double.MAX_VALUE
val minDouble:Double = Double.MIN_VALUE

val aShort:Short = 127
val maxShort:Short = Short.MAX_VALUE
val minShort:Short = Short.MIN_VALUE

val maxbyte:Byte = Byte.MAX_VALUE
val minbyte:Byte = Byte.MIN_VALUE



fun main(args:Array<String>){
    println(anotherInt)
    println(moreInt)

    println(maxInt)
    println(Math.pow(2.0,31.0)-1)
    println(minInt)
    println(-Math.pow(2.0,31.0))

    println(maxLong)
    println(minLong)

    println(aFloat)
    println(anotherFloat)
    println(maxFloat)
    println(minFloat)

    println(maxShort)
    println(minShort)

    println(maxbyte)
    println(minbyte)


}
