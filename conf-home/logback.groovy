//
// Built on Sat May 09 21:51:51 CEST 2015 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}
appender("FILE", FileAppender) {
    file = "log/testFile.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

appender("IMAGE_RECORD", FileAppender) {
    file = "log/image_record.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    }
}

//root(INFO, ["STDOUT"])
root(ERROR, ["STDOUT"])

logger("com.seveniu",DEBUG,["STDOUT","FILE"],false)
logger("org.spring",ERROR,["STDOUT"],false)
logger("us.codecraft.webmagic",DEBUG,["STDOUT"],false)
