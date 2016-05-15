import groovy.json.JsonSlurper

/**
 * Created by hf on 11/24/15.
 */

def Map<String, String> test(String str) {
    JsonSlurper jsonSlurper = new JsonSlurper();
    def resultList = jsonSlurper.parseText(str)
//    resultList.

}