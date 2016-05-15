package commands

import com.seveniu.Application
import com.seveniu.conf.DBConf
import groovy.sql.Sql
import org.crsh.cli.Argument

/**
 * User: seveniu
 * Date: 6/2/15
 * Time: 11:54 PM
 * Project: boot-web
 *
 */

import org.crsh.cli.Command;
import org.crsh.cli.Usage

@Usage("dao eg: UserDao@get(1)")
class data {

    @Usage("query db")
    @Command
    Object q(@Argument String sql) {
        println(sql + " hello+")
        sql = "SELECT * FROM users WHERE id = 1";
//        println(dataSource)
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(Application.ctx.getBean(DataSource.class));
//        def  name = jdbcTemplate.query(sql, new RowMapper() {
//            @Override
//            Object mapRow(ResultSet rs, int rowNum) throws SQLException {
//                rs.n
//                return rs.getString("firstName")
//            }
//        });
//        println(name)
        DBConf dbConf = Application.ctx.getBean(DBConf)
        println(dbConf.driver)
        def session = Sql.newInstance(dbConf.url,dbConf.username,dbConf.password,dbConf.driver)
//        jdbcTemplate.query(sql,{rs,rowNum -> println(rs)})
        def match = sql =~/SELECT([\s\S]+)FROM/
        println(match[0][1])
        session.eachRow(sql) { row ->
            println row.getAt(0) + " - "+ row.getAt(0) + "::" + row.getAt(1)
        }
        return sql;
    }
}
