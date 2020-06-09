
val string:String = "Helloworld"
val fromChars: String = String(charArrayOf('H','e','l','l','o','w','o','r','l','d'))

fun main(args: Array<String>) {
//    值
    println(string==fromChars)
//    对象
    println(string===fromChars)

    println("输出"+string)

    val arg1:Int = 0
    val arg2:Int = 1
    println("$arg1 + $arg2 = ${arg1+arg2}")

    val sayHello: String  = "hello \"zch\" "
    println(sayHello)

    val rawString:String="""
        \t
        \n
        dsfsaad
        $arg1
        """
    println(rawString)
    println(rawString.length)

}