
val arrayOfInt:IntArray = intArrayOf(1,3,5,7)
val arrayOfChar:CharArray = charArrayOf('H','e','l','l','o','w','o','r','l','d')
val arrayOfString: Array<String> = arrayOf("我","是","zch")


fun main(args: Array<String>) {
    println(arrayOfInt.size)
    for(int in arrayOfInt){
        print("$int ")
    }
    println()

    println(arrayOfChar.joinToString())
    println(arrayOfInt.slice(1..2))
}