
//判null
fun getName():String?{
    return null
}

fun main(args: Array<String>) {
    //?前面表达式为空执行后面的。
//    val name:String = getName()?:return
//    println(name.length)

    val value:String ?="Helloworld"
    //!!不是null
    println(value!!.length)

    //强制转化
    val parent:Parent = Parent()
    val child:Child?= parent as? Child
    println(child)


    val string:String ?= "hello"
    if(string != null){
        println(string.length)
    }

    //转化异常


}