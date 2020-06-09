
//类构造方法
class girl(person:String,appear:String,vo:String): person(person,appear,vo)

open class person(var personal:String,var appearence:String,var voice:String){
    //初始化
    init {
        println("new ${this.javaClass.simpleName},$personal,$appearence,$voice")
    }
}
//Any为所有类的父类

fun main(args: Array<String>) {
    val mgirl: girl = girl("good","A+","great")
}
