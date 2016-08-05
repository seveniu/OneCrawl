# OneCrawl 爬虫服务器

基于 [webmagic](https://github.com/code4craft/webmagic)

Feature

* 中心话爬虫服务器, 可连接多个客户端
* 支持定向爬虫

### 运行

    mac linux : ./gradlew bootRun -Pprofile=dev

    windows : ./gradlew.bat bootRun -Pprofile=dev

    jar : 
    ./gradlew assemble
    java -jar build/libs/one-crawl-0.1.0.jar --spring.profiles.active=dev


### 接口
``` thrift
  string reg(1:ConsumerConfig consumerConfig)// 返回 uuid
  TaskStatus addTask(1:string uuid,2:TaskInfo task)
  string getRunningTasks(1:string uuid)
  ResourceInfo getResourceInfo(1:string uuid)
  string getTaskSummary(1:string uuid)
```
具体查看 conf/OneCrawl.thrift

### 相关项目
**SDK : [OneCrawlSdk](https://github.com/seveniu/OneCrawlSdk)** 
    
    嵌入到客户端使用
    通过调用 OneCrawl 接口, 注册客户端,添加任务,获取任务状态,以及结果

**模板管理(爬虫客户端DEMO) : [OneCrawlTemplate](https://github.com/seveniu/OneCrawlTemplate)**
    
    管理爬虫模板
    通过chrome 插件生成模板,并存储

**chrome 插件 : [OneCrawlTemplateExtension](https://github.com/seveniu/OneCrawlTemplateExtension)**
    
    配合 模板管理使用

### 工具类项目
**FileDownload : [FileDownload](https://github.com/seveniu/FileDownload)**
    
    文件下载工具

**FileUpload : [FileUpload](https://github.com/seveniu/FileUpload)**
    
    文件上传工具