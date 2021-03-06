# Android_Practice

* [四大组件之Activity](ActivityLife/)
  * [Android登录模式](LaunchMode/)
* [四大组件之Service](ServiceSolution/)
  * [Service后台监测进度](ServiceApp/)
  * [AIDL跨进程监督进度条—进程间通信IPC](AIDLApp/)
  * [AIDL实现—服务端主动发送返回数据](SimpleAIDL/)
* [四大组件之BroadCast](BroadcastTest/)
  * [系统广播,自定义广播](BroadDemo/)
* [四大组件之ContentProvider](myContentProvider/)
  * [跨进程数据传输——ContentProvider实例](ContentProvider/)
  * [联系人读取和添加——获取系统ContentProvider](GetPhoneApp/)
* [Fragment创建及使用](FragmentTest/)
* [Android网络编程](NetworkApp/)
  * Get请求，Post请求
  * 对Json数据格式的解析：JSONObject解析、GSON解析
  * [Socket编程](UdpApp/)
    * UDP通信—Client与Server通信
    * TCP通信—简易聊天室（使用消息队列，多线程处理..）
    * Http与Https—访问页面（Https中:证书验证，域名验证，正确访问Https页面，X509Certificate....）
* [Android消息处理机制——Handler](HandlerApp/)
  * Handler运行机制，Message消息队列,  Looper消息循环
  * [一个使用Handler运行机制的简单计时器应用](TimeApp/)
  * [请求网络图片功能的小应用](ImageApp/)
* [AsyncTask异步处理](HandlerApp/)
  * 下载任务处理
  * Android6.0后运行时权限处理
* [Android数据存储](DataStoreApp/)
  * SharePreferences获取登录信息
  * 外部存储（6.0后动态申请权限）
  * 内部存储
  * [SQLite数据库——学生管理App应用](StudentAppp/)
  * [商品存储app——使用GreenDao](StoreApp/)
* [OkHttp&Retrofit](OkhttpApp/) 
  * OkHttp工具类的封装
  * Get、Post、PostMultiPart、PostJson的实现
  * 自定义Header添加
  * Retrofit工具类的封装
* [App内部通讯组件—EventBus](EventBusApp/) 
  * EventBus实现事件发布：事件、订阅、发布。
  * ThreadMode：POSTING、MAIN、MAINORDERED、BACKGROUND、ASYNC
  * 粘性事件：可以先发布后订阅。
* [Android加载图片库——Glide](LoadImageApp/) 
* [Android动画](Animation/) 
  *  [Android基本动画](Animation/AnimationApp/) 
    *  逐帧动画
    *  视图动画
    *  属性动画
  *  [Android转场动画](Animation/TranslitionApp/) 
    *  Scene，Transition，TransitionManger使用    
* Android—View事件体系
  * [Android界面及简单控件的布局设计](View/UIApp/)
    * [一个简单的点餐App](View/Wm_app/)：三种菜单的创建：选项菜单(OptionMenu)、上下文菜单(ContextMenu)、弹出式菜单(PopupMenu)
    * [三种对话框的创建](View/DialogApp/)
      * AlertDialog、自定义Dialog、PopupWindow
  * 高级控件
    * [ListView的使用](View/ListViewApp/)
      * ArrayAdapter、SimpleAdapter、BaseAdapter
      * ListView优化（使用ViewHolder）  
    * [ViewPager实例——广告图的无限轮播实现](View/ViewPagerApp/)
      * 实现魅族Banner效果的显示效果。
      * 实现无限轮播效果。
    * [卡片布局](View/Cardapp/)
    * [RecyclerView布局实现](View/RecyclerViewApp/)
  *  [自定义View的实现—ProgressRound进度条实现](View/ViewApp/)
     * 自定义属性的声明与获取
     * 测量onMeasure
     * 绘制onDraw
     * 状态的保存与恢复 
  *  [Android事件分发机制](View/TouchSystemApp/)
     * Activity、ViewGroup拦截、View 
  *  [SurfaceView学习—飞翔的小鸟小游戏](View/BirdGameApp/)
     *  使用创建的SurfaceHolder对象监听Surface的创建，再开启子线程，在线程中使用循环处理获取Canvas进行绘制。
* [自动注入神器——ButterKnife](ButterApp/)
* [综合案例——课程推荐App](CourseApp/) 
  * ExpandListView的使用
  * AsyncTask异步请求网络操作的封装
  * SQLite数据库操作的封装
* [项目实例——下饭食堂点餐App](RestApp/) 
* [项目实例——电子书阅读器App](BookReader/) 
* [项目实例——饭后计步器](stepApp/) 
    * 绑定式服务的应用、AIDL通信
    * 自定义圆形进度条
* [Kotlin语法](Basic_Kotlin/) 
    * 数据类型
    * 区间，数组，字符串
    * 对象，判空