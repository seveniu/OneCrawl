package commands
/**
 * User: seveniu
 * Date: 5/26/15
 * Time: 5:32 PM
 * Project: spring-boot-sample-actuator-ui
 *
 */
import org.crsh.cli.Usage
import org.crsh.cli.Command
import org.crsh.command.InvocationContext


class hello {

    @Usage("Say Hello")
    @Command
    def main(InvocationContext context) {
        return "xxxxxxxx"
    }

}
