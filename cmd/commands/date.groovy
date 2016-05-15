package commands

/**
 * User: seveniu
 * Date: 5/26/15
 * Time: 6:06 PM
 * Project: spring-boot-sample-actuator-ui
 *
 */
import org.crsh.cli.Command;
import org.crsh.cli.Usage;
import org.crsh.cli.Option;

class date {
    @Usage("show the current time")
    @Command
    Object main(
            @Usage("the time format")
            @Option(names=["f","format"])
                    String format) {
        if (format == null)
            format = "EEE MMM d HH:mm:ss z yyyy";
        def date = new Date();
        return date.format(format);
    }

    @Usage("tomorrow")
    @Command
    Object month( ) {
        return new Date().getMonth() + " 月@@@"
    }
}
