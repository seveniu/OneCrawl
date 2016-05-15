package commands

import com.seveniu.service.OrgCacheManager
import com.seveniu.util.AppContext

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


class reCache {

    @Usage("reCache ")
    @Command
    def org(InvocationContext context) {
        OrgCacheManager orgCacheManager= AppContext.getBean(OrgCacheManager.class)
        orgCacheManager.reCache();
    }

}
