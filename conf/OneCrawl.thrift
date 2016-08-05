 namespace java com.seveniu.thriftServer
 struct TaskInfo {
    1:required string id;
    2:required string name;
    3:required string templateId;
    4:required list<string> urls;
    5:required Proxy proxy;
    6:required Javascript javascript
    7:required i32 threadNum;
    8:required string template;
    9:required TemplateType templateType;
    10:required i32 priority = 0;
 }
 struct ConsumerConfig {
    1:required string name;
    2:required string type;
    3:optional string duplicateUrl;
    4:optional string doneUrl;
    5:optional string statisticUrl;
    6:optional string taskUrl;
    7:optional string host;
    8:optional i32 port;
 }

 struct ResourceInfo {
    1:required i32 maxRunning;
    2:required i32 maxWait;
    3:required i32 curRunning;
    4:required i32 curWait;
 }
 exception ServerException {
    1:required i32 code;
    2:required string msg;
 }
 enum Proxy{
    OFF = 1,
    ON = 2
 }
 enum Javascript{
    OFF = 1,
    ON = 2
 }

 enum TaskStatus {
    WAIT=1,
    RUNNING=2,
    STOP=3,
    FAIL=4,
    FULL=5
 }

 enum TemplateType{
    MULTI_LAYER_CONTENT = 1,
    TEST_SINGLE_PAGE = 2
 }
 service CrawlThrift{
  string reg(1:ConsumerConfig consumerConfig)// 返回 uuid
  TaskStatus addTask(1:string uuid,2:TaskInfo task)
  string getRunningTasks(1:string uuid)
  ResourceInfo getResourceInfo(1:string uuid)
  string getTaskSummary(1:string uuid)
 }